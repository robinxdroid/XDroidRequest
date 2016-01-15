package com.xdroid.request.network;

import static com.xdroid.request.ex.RequestBodyConstants.HEADER_USER_AGENT;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import com.xdroid.request.base.Request;
import com.xdroid.request.config.HttpMethod;

import android.text.TextUtils;

/**
 * HttpUrlConnection request body
 * 
 * @author Robin
 * @since 2015-07-02 16:40:21
 */
public class HurlStack implements HttpStack {

	private final UrlRewriter mUrlRewriter;
	private final SSLSocketFactory mSslSocketFactory;
	private String mUserAgent;

	public interface UrlRewriter {
		/**
		 * Rewrite the URL for the request.
		 */
		public String rewriteUrl(String originalUrl);
	}

	public HurlStack() {
		this(null);
	}

	public HurlStack(UrlRewriter urlRewriter) {
		this(urlRewriter, null);
	}

	public HurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
		mUrlRewriter = urlRewriter;
		mSslSocketFactory = sslSocketFactory;
	}

	/**
	 * @param urlRewriter
	 *            Rewriter to use for request URLs
	 * @param sslSocketFactory
	 *            SSL factory to use for HTTPS connections
	 * @param userAgent
	 *            User Agent for HTTPS connections
	 */
	public HurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory, String userAgent) {

		mUrlRewriter = urlRewriter;
		mSslSocketFactory = sslSocketFactory;
		mUserAgent = userAgent;
	}

	@Override
	public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException {
		String url = request.getUrl();
		HashMap<String, String> map = new HashMap<String, String>();
		map.putAll(request.getHeaders());
		map.putAll(additionalHeaders);

		if (mUrlRewriter != null) {
			String rewritten = mUrlRewriter.rewriteUrl(url);
			if (rewritten == null) {
				throw new IOException("URL blocked by rewriter: " + url);
			}
			url = rewritten;
		}
		URL parsedUrl = new URL(url);
		HttpURLConnection connection = openConnection(parsedUrl, request);

		if (!TextUtils.isEmpty(mUserAgent)) {
			connection.setRequestProperty(HEADER_USER_AGENT, mUserAgent);
		}

		for (String headerName : map.keySet()) {
			connection.addRequestProperty(headerName, map.get(headerName));
		}

		setConnectionParametersForRequest(connection, request);

		HttpResponse response = responseFromConnection(connection);
		return response;
	}

	private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// Timeout is actually the timeout of the retry strategy
		int timeoutMs = request.getRetryPolicy().getCurrentTimeout();
		connection.setConnectTimeout(timeoutMs);
		connection.setReadTimeout(timeoutMs);
		connection.setUseCaches(false);
		connection.setDoInput(true);

		// getContentLength()
		// 为“-1”,在2.2版本以上开启了GZIP压缩，导致长度始终为-1，使用如下一行代码禁止GZIP压缩，
		// 但是不推荐,建议与服务器端协商，在请求头中添加content length.
		// connection .setRequestProperty("Accept-Encoding", "identity");

		// use caller-provided custom SslSocketFactory, if any, for HTTPS
		if ("https".equals(url.getProtocol())) {
			if (mSslSocketFactory != null) {
				((HttpsURLConnection) connection).setSSLSocketFactory(mSslSocketFactory);
			} else {
				// Trust all certificates
				HTTPSTrustManager.allowAllSSL();
			}
		}

		return connection;
	}

	/**
	 * Create HttpResponse from a given HttpUrlConnection
	 */
	private HttpResponse responseFromConnection(HttpURLConnection connection) throws IOException {
		HttpResponse response = new HttpResponse();
		int responseCode = connection.getResponseCode();
		if (responseCode == -1) {
			throw new IOException("Could not retrieve response code from HttpUrlConnection.");
		}
		response.setResponseCode(responseCode);
		response.setResponseMessage(connection.getResponseMessage());
		// contentStream
		InputStream inputStream;
		try {
			inputStream = connection.getInputStream();
		} catch (IOException ioe) {
			inputStream = connection.getErrorStream();
		}
		response.setContentStream(inputStream);

		response.setContentLength(connection.getContentLength());
		response.setContentEncoding(connection.getContentEncoding());
		response.setContentType(connection.getContentType());
		// header
		Map<String, String> headerMap = new HashMap<String, String>();
		for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
			if (header.getKey() != null) {
				String value = "";
				for (String v : header.getValue()) {
					value += (v + "; ");
				}
				headerMap.put(header.getKey(), value);
				response.setHeaders(headerMap);
			}
		}
		return response;
	}


	private static void setConnectionParametersForRequest(HttpURLConnection connection, Request<?> request)
			throws IOException {
		switch (request.getHttpMethod()) {
		case HttpMethod.GET:
			connection.setRequestMethod("GET");
			break;
		case HttpMethod.DELETE:
			connection.setRequestMethod("DELETE");
			break;
		case HttpMethod.POST:
			connection.setRequestMethod("POST");
			addBodyIfExists(connection, request);
			break;
		case HttpMethod.PUT:
			connection.setRequestMethod("PUT");
			addBodyIfExists(connection, request);
			break;
		case HttpMethod.HEAD:
			connection.setRequestMethod("HEAD");
			break;
		case HttpMethod.OPTIONS:
			connection.setRequestMethod("OPTIONS");
			break;
		case HttpMethod.TRACE:
			connection.setRequestMethod("TRACE");
			break;
		case HttpMethod.PATCH:
			// connection.setRequestMethod("PATCH");
			// If server doesnt support patch uncomment this
			connection.setRequestMethod("POST");
			connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			addBodyIfExists(connection, request);
			break;
		default:
			throw new IllegalStateException("Unknown method type.");
		}
	}

	/**
	 * If there is body then add
	 */
	private static void addBodyIfExists(HttpURLConnection connection, Request<?> request) throws IOException {
		request.buildBody(connection);
	}
}
