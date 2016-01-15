package com.xdroid.request.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import com.xdroid.request.base.Request;
import com.xdroid.request.response.NetworkResponse;
import com.xdroid.request.retry.RetryPolicy;
import com.xdroid.request.utils.CLog;

/**
 * The network requests the HttpStack to use the Request client to initiate a network request and returns a NetworkRespond result.
 * @author Robin
 * @since 2015-07-02 16:16:57
 */
public class Network {
    protected final HttpStack mHttpStack;

    public Network(HttpStack httpStack) {
        mHttpStack = httpStack;
    }

    /**
     * Actually executing a request
     * 
     * @param request
     * @return A response not to null
     * @throws HttpException
     */
    public NetworkResponse performRequest(Request<?> request)
            throws HttpException {
        while (true) {
            HttpResponse httpResponse = null;
            byte[] responseContents = null;
            Map<String, String> responseHeaders = new HashMap<String, String>();
            try {
                Map<String, String> headers = new HashMap<String, String>();
                httpResponse = mHttpStack.performRequest(request, headers);

                int statusCode = httpResponse.getResponseCode();
                responseHeaders = httpResponse.getHeaders();
                if (statusCode == HttpStatus.SC_NOT_MODIFIED) { // 304
                    return new NetworkResponse(HttpStatus.SC_NOT_MODIFIED,
                            null,
                            responseHeaders, true);
                }

                if (httpResponse.getContentStream() != null) {
                    /*if (request instanceof FileRequest) {
                        responseContents = ((FileRequest) request)
                                .handleResponse(httpResponse);
                    } else {*/
                	  responseContents = responseToBytes(request,httpResponse);
//                    }
                } else {
                    responseContents = new byte[0];
                }

                if (statusCode < 200 || statusCode > 299) {
                    throw new IOException();
                }
                return new NetworkResponse(statusCode, responseContents,
                        responseHeaders, false);
            } catch (SocketTimeoutException e) {
            	if (request.getRequestCacheConfig().isRetryWhenRequestFailed()) {
            		retryOnException( request, new HttpException("socket timeout",HttpError.ERROR_SOCKET_TIMEOUT));
				}else {
					throw new HttpException("socket timeout",HttpError.ERROR_SOCKET_TIMEOUT);
				}
            } 
            /*catch (ConnectTimeoutException e) {
             	if (request.getCacheConfig().isRetryWhenRequestFailed()) {
             		retryOnException( request, new HttpException("connect timeout"));
             	}else {
             		throw new HttpException(new SocketTimeoutException("connect timeout"));
				}
            } */
            catch (MalformedURLException e) {
                throw new RuntimeException("Bad URL " + request.getUrl(), e);
            } catch (IOException e) {
                int statusCode = 0;
                //NetworkResponse networkResponse = null;
                if (httpResponse != null) {
                    statusCode = httpResponse.getResponseCode();
                } else {
                    //throw new HttpException("NoConnection error", e);
                	throw new HttpException("NoConnection error", HttpError.ERROR_NO_CONNECTION);
             		//retryOnException( request, new HttpException("NoConnection error",e));
                }
                CLog.d("Unexpected response code %s for: %s",statusCode,request.getUrl());
                if (responseContents != null) {
                    //networkResponse = new NetworkResponse(statusCode,responseContents, responseHeaders, false);
                    if (statusCode == HttpStatus.SC_UNAUTHORIZED|| statusCode == HttpStatus.SC_FORBIDDEN) {
                    	if (request.getRequestCacheConfig().isRetryWhenRequestFailed()) {
                    		retryOnException( request, new HttpException("auth error",HttpError.ERROR_UNAUTHORIZED));
                    	} else {
                    		  throw new HttpException("auth error",HttpError.ERROR_UNAUTHORIZED);
						}
                    } else if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    	if (request.getRequestCacheConfig().isRetryWhenRequestFailed()) {
                          	retryOnException( request, new HttpException("redirect error",HttpError.ERROR_REDIRECT));
                    	}else {
                    		  throw new HttpException("redirect error",HttpError.ERROR_REDIRECT);
						}
              
                    } else {
                        throw new HttpException("server error, Only throw ServerError for 5xx status codes.",HttpError.ERROR_SERVER);
                    }
                } else {
                    throw new HttpException("responseContents is null",HttpError.ERROR_RESPONSE_NULL);
                }
            }
        }
    }


    /**
     * Convert HttpResponse to byte[] 
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws HttpException
     */
    private byte[] responseToBytes(Request<?> request,HttpResponse response) throws IOException,
            HttpException {
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(
                ByteArrayPool.get(), (int) response.getContentLength());
        byte[] buffer = null;
        long totalSize = (int) response.getContentLength();
        try {
            InputStream in =response.getContentStream();
            if (in == null) {
                throw new HttpException("server error",HttpError.ERROR_SERVER);
            }
            buffer = ByteArrayPool.get().getBuf(1024);
            int count;
            int transferredBytesSize = 0;
            while ((count = in.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
                transferredBytesSize += count;
                //request.getRequestQueue().getDelivery().postRequestDownloadProgress(request,transferredBytesSize, totalSize);
                request.onRequestDownloadProgress(transferredBytesSize, totalSize);
            }
            return bytes.toByteArray();
        } finally {
            try {
            	  response.getContentStream().close();
            } catch (IOException e) {
                CLog.d("Error occured when calling consumingContent");
            }
            ByteArrayPool.get().returnBuf(buffer);
            bytes.close();
        }
    }

    /**
     * When an exception occurs to try again
     * @param request
     * @param exception
     * @return
     * @throws HttpException
     */
    private static RetryPolicy retryOnException( Request<?> request,HttpException exception) throws HttpException {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        try {
            retryPolicy.retry(exception);
        } catch (HttpException e) {
            throw e;
        }
        
        /*Distribution of retry event*/
        request.getRequestQueue().getDelivery().postRequestRetry(request, retryPolicy.getCurrentRetryCount() ,exception);
        
        return retryPolicy;
    }
}
