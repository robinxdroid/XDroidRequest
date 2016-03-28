package com.xdroid.request.interfaces;

import java.io.File;
import java.util.Map;

import com.xdroid.request.base.Request;
import com.xdroid.request.config.DataType;
import com.xdroid.request.network.HttpException;
import com.xdroid.request.response.NetworkResponse;

/**
 * when data processed finish
 * 
 * @author Robin
 * @since 2015-05-07 21:27:07
 */
public interface OnRequestListener<T> {

	/** The preparation for the request */
	public void onRequestPrepare(Request<?> request);

	/** Call this method when request failed */
	public void onRequestFailed(Request<?> request, HttpException httpException);
	
	/** Call this method when request retry  */
	public void onRequestRetry(Request<?> request, int currentRetryCount, HttpException previousError);
	
	/** Call this method to delivered current progress for request */
	public void onRequestDownloadProgress(Request<?> request, long transferredBytesSize, long totalSize);
	
	/** Call this method to delivered upload progress for request */
	public void onRequestUploadProgress(Request<?> request, long transferredBytesSize, long totalSize, int currentFileIndex, File currentFile);
	
	/** Call this method when request finished */
	public void onRequestFinish(Request<?> request, Map<String, String> headers, T result);

	/** Call this method when cache data load finished */
	public void onCacheDataLoadFinish(Request<?> request, Map<String, String> headers, T result);

	/** Parse the network response to an object */
	public boolean onParseNetworkResponse(Request<?> request, NetworkResponse networkResponse,T result);
	
	/**
	 * When the request is completed or the cache data loaded ,call this method
	 * , called only once, the final data delivery function
	 */
	public void onDone(Request<?> request, Map<String, String> headers, T result, DataType dataType);

}
