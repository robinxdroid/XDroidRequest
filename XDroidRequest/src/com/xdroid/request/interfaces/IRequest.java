package com.xdroid.request.interfaces;

import java.io.File;
import java.util.Map;

import com.xdroid.request.cache.CacheData;
import com.xdroid.request.cache.Entry;
import com.xdroid.request.network.HttpException;
import com.xdroid.request.response.NetworkResponse;

/**
 *  {@link com.xdroid.request.base.Request}
 * @author Robin
 * @since 2015-05-07 14:06:41
 */
public interface IRequest<T>{
	
	/** Do something before the request */
	public  void requestPrepare();
	
	/** When request finished */
	public void onRequestFinish(Map<String, String> headers,T data);
	
	/** When request failed */
	public void onRequestFailed(HttpException httpException);
	
	/** When request retry */
	public void onRequestRetry(int currentRetryCount, HttpException previousError);
	
	/** Call this method to delivered current progress for request */
	public void onRequestDownloadProgress(long transferredBytesSize, long totalSize);
	
	/** Call this method to delivered upload progress for request */
	public void onRequestUploadProgress(long transferredBytesSize, long totalSize, int currentFileIndex, File currentFile);
	  
	/** When cache data load finished */
	public void onCacheDataLoadFinish(CacheData<Entry<T>> cacheData);
	
	/** Get the current request's cache data */
	public CacheData<Entry<T>> getCache(String key);
	
	/** Parse the network response to an object */
	public void onParseNetworkResponse(NetworkResponse networkResponse,T result);
	
	/** Get the request's cookie */
	public String getCookie();
	
	/** Manual call this method when the request finished , to release the same request in the "mWaitingRequests" map  */
	public void finish() ;
	
}
