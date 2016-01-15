package com.xdroid.request.ex;

import static com.xdroid.request.ex.RequestBodyConstants.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Map;

import com.xdroid.request.base.Request;
import com.xdroid.request.config.Priority;
import com.xdroid.request.config.RequestCacheConfig;
import com.xdroid.request.interfaces.OnRequestListener;
import com.xdroid.request.retry.DefaultRetryPolicyImpl;
import com.xdroid.request.utils.CLog;

/**
 * Can submit key/value pair, files, key/value pair and files, JSON, 
 * If the request parameter contains a JSON parameters, send JSON parameters, 
 * this time even contain key/value pair or file parameter will not be sent
 * @author Robin
 * @since 2016-01-07 18:53:19
 *
 * @param <T>
 */
public abstract class MultipartRequest<T> extends Request<T>{

	private static final String PROTOCOL_CHARSET = "utf-8";
	public static final int TIMEOUT_MS = 30000;
	private boolean isFixedStreamingMode;
	
	private RequestParams mRequestParams;
	
	public MultipartRequest() {
		super();
		setPriority(Priority.NORMAL);
		setRetryPolicy(new DefaultRetryPolicyImpl(TIMEOUT_MS, DefaultRetryPolicyImpl.DEFAULT_MAX_RETRIES, DefaultRetryPolicyImpl.DEFAULT_BACKOFF_MULT));
		
		mRequestParams = new RequestParams();
	}
	
	public MultipartRequest(RequestCacheConfig cacheConfig, String url, String cacheKey, OnRequestListener<T> onRequestListener) {
		super(cacheConfig, url, cacheKey, onRequestListener);
		
		setPriority(Priority.NORMAL);
		setRetryPolicy(new DefaultRetryPolicyImpl(TIMEOUT_MS, DefaultRetryPolicyImpl.DEFAULT_MAX_RETRIES, DefaultRetryPolicyImpl.DEFAULT_BACKOFF_MULT));
		setUrl(url);
		
		mRequestParams = new RequestParams();
	}
	
	/*======================================================
	 *  Override Super
	 *====================================================== 
	 */
	
	@Override
	public Map<String, String> getHeaders() {
		return mRequestParams.buildHeaders();
	}
	
	@Override
	public String getParams() {
		return mRequestParams.toString();
	}
	
	public String buildBodyContentType(int curTime) {
		if (mRequestParams.hasJsonInParams()) {
			return String.format( "application/json; charset=%s", "utf-8");
		}
		
		return String.format(CONTENT_TYPE_MULTIPART, PROTOCOL_CHARSET, curTime);
	}
	
	@Override
	public void buildBody(HttpURLConnection connection) {
		connection.setDoOutput(true);
		final String charset =PROTOCOL_CHARSET;
		final int curTime = (int) (System.currentTimeMillis() / 1000);
		final String boundary = BOUNDARY_PREFIX + curTime;
		connection.setRequestProperty(HEADER_CONTENT_TYPE, buildBodyContentType(curTime));
		
		if (isFixedStreamingMode()) {
			int contentLength = getContentLength(boundary, mRequestParams);
			connection.setFixedLengthStreamingMode(contentLength);
		} else {
			connection.setChunkedStreamingMode(0);
		}
		
		// Write parameters
		PrintWriter writer = null;
		try {
			OutputStream out = connection.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(out, charset), true);

			if (mRequestParams.hasJsonInParams()) {
				// append json
				writer.append(mRequestParams.buildJsonParams()).flush();
				
			}else {
				writeFieldToOutputStream(boundary, writer);
				writeFileToOutputStream(boundary, writer, out);
				
				// End of multipart/form-data.
				writer.append(boundary + BOUNDARY_PREFIX).append(CRLF).flush();
			}
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		
	}

	private void writeFieldToOutputStream(final String boundary, PrintWriter writer) {
		// add field
		Map<String, String> stringOrIntParams = mRequestParams.buildParametersToMap();
		for (String key : stringOrIntParams.keySet()) {
			String param = stringOrIntParams.get(key);

			writer.append(boundary).append(CRLF)
					.append(String.format(HEADER_CONTENT_DISPOSITION + COLON_SPACE + FORM_DATA, key)).append(CRLF)
					.append(HEADER_CONTENT_TYPE + COLON_SPACE + CONTENT_TYPE_TEXT)
					.append(CRLF).append(CRLF)
					.append(param).append(CRLF).flush();
		}
	}

	private void writeFileToOutputStream(final String boundary, PrintWriter writer, OutputStream out)
			throws IOException, FileNotFoundException {
		// add file
		Map<String, File> fileParams = mRequestParams.buildFileParameters();
		int currentFileIndex = 1;
		for (String key : fileParams.keySet()) {

			File file = fileParams.get(key);

			if (!file.exists()) {
				CLog.e("File not found: %s", file.getAbsolutePath());
				throw new IOException(String.format("File not found: %s", file.getAbsolutePath()));
			}

			if (file.isDirectory()) {
				CLog.e("File is a directory: %s", file.getAbsolutePath());
				throw new IOException(String.format("File is a directory: %s", file.getAbsolutePath()));
			}

			writer.append(boundary).append(CRLF)
					.append(String.format(
							HEADER_CONTENT_DISPOSITION + COLON_SPACE + FORM_DATA + SEMICOLON_SPACE + FILENAME, key,
							file.getName()))
					.append(CRLF).append(HEADER_CONTENT_TYPE + COLON_SPACE + CONTENT_TYPE_OCTET_STREAM).append(CRLF)
					.append(HEADER_CONTENT_TRANSFER_ENCODING + COLON_SPACE + BINARY).append(CRLF).append(CRLF)
					.flush();

			BufferedInputStream input = null;
			try {
				FileInputStream fis = new FileInputStream(file);
				int transferredBytesSize = 0;
				int totalSize = (int) file.length();
				input = new BufferedInputStream(fis);
				int bufferLength = 0;

				byte[] buffer = new byte[1024];
				while ((bufferLength = input.read(buffer)) > 0) {
					CLog.w("<getBody> thread name : %s" ,Thread.currentThread().getName());
					out.write(buffer, 0, bufferLength);
					transferredBytesSize += bufferLength;
					//super.getRequestQueue().getDelivery().postRequestUploadProgress(this, transferredBytesSize,totalSize);  //UI Thread
					super.onRequestUploadProgress(transferredBytesSize, totalSize,currentFileIndex,file); //Thread
				}
				// Important! Output cannot be closed. Close of writer will
				// close output as well.
				out.flush();
			} finally {
				if (input != null)
					try {
						input.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
			}
			// CRLF is important! It indicates end of binary boundary.
			writer.append(CRLF).flush();
			
			currentFileIndex ++;
		}
	}

	public boolean isFixedStreamingMode() {
		return isFixedStreamingMode;
	}
	
	public void setFixedStreamingMode(boolean isFixedStreamingMode) {
		this.isFixedStreamingMode = isFixedStreamingMode;
	}
	
	public void setRequestParams(RequestParams requestParams){
		this.mRequestParams = requestParams;
	}

	public RequestParams getRequestParams() {
		return mRequestParams;
	}
	
	
}
