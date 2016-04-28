package com.xdroid.request.interfaces;

import java.io.File;

import com.xdroid.request.base.Request;
import com.xdroid.request.config.RequestCacheConfig;
import com.xdroid.request.ex.RequestParams;

/**
 * Provider method for "XRequest"
 * 
 * @author Robin
 * @since 2015-08-13 16:53:31
 * @param <ListenerType>
 *            Listen callback type
 *
 */
public interface IXRequest {

	/**
	 * Best during application initialization calls only once
	 * 
	 * @param threadPoolSize
	 */
	public void setRequestThreadPoolSize(int threadPoolSize);

	/**
	 * Add a request to queue to execute
	 * 
	 * @param request
	 *            Target request
	 */
	public void addToRequestQueue(Request<?> request);

	/**
	 * Create a default cache configuration
	 * 
	 * @return
	 */
	public RequestCacheConfig getDefaultCacheConfig();

	/**
	 * Create a no cache configuration
	 * 
	 * @return
	 */
	public RequestCacheConfig getNoCacheConfig();

	/**
	 * To cancel a request that is requesting
	 * 
	 * @param request
	 */
	public void cancelRequest(Request<?> request);

	/**
	 * Cancel all of this request in the request queue , not including is
	 * requested
	 * 
	 * @param request
	 *            Current instance of request
	 * @param tag
	 *            If there is no special Settings, then introduction the
	 *            instance of activity
	 */
	public void cancelAllRequestInQueueByTag(Object tag);

	/**
	 * Start the request，start the thread pool
	 */
	public void start();

	/**
	 * Close the request, quit all threads, release the request queue
	 */
	public void shutdown();

	/*
	 * =======================================================================
	 * GET
	 * =======================================================================
	 */

	public <T> Request<?> sendGet(Object tag, String url, String cacheKey, RequestParams params, RequestCacheConfig cacheConfig, OnRequestListener<T> onRequestListener);

	public <T> Request<?> sendGet(Object tag, String url, RequestParams params, RequestCacheConfig cacheConfig, OnRequestListener<T> onRequestListener);
	
	public <T> Request<?> sendGet(Object tag, String url, String cacheKey, RequestParams params, OnRequestListener<T> onRequestListener);
	
	public <T> Request<?> sendGet(Object tag, String url, RequestParams params, OnRequestListener<T> onRequestListener);
	
	public <T> Request<?> sendGet(Object tag, String url, String cacheKey, OnRequestListener<T> onRequestListener);

	public <T> Request<?> sendGet(Object tag, String url, OnRequestListener<T> onRequestListener);

	/*
	 * =======================================================================
	 * POST
	 * =======================================================================
	 */

	public <T> Request<?> sendPost(Object tag, String url, String cacheKey, RequestParams params, RequestCacheConfig cacheConfig, OnRequestListener<T> onRequestListener);

	public <T> Request<?> sendPost(Object tag, String url, RequestParams params, RequestCacheConfig cacheConfig, OnRequestListener<T> onRequestListener);
	
	public <T> Request<?> sendPost(Object tag, String url, String cacheKey, RequestParams params, OnRequestListener<T> onRequestListener);
	
	public <T> Request<?> sendPost(Object tag, String url, RequestParams params, OnRequestListener<T> onRequestListener);

	/*
	 * =======================================================================
	 * Download
	 * =======================================================================
	 */

	public <T> Request<?> upload(Object tag, String url, String cacheKey, RequestParams params, RequestCacheConfig cacheConfig, OnRequestListener<T> onRequestListener);

	public <T> Request<?> upload(Object tag, String url, RequestParams params, OnRequestListener<T> onRequestListener);

	public <T> Request<?> upload(Object tag, String url, String cacheKey, RequestParams params, OnRequestListener<T> onRequestListener);

	/*
	 * =======================================================================
	 * Download
	 * =======================================================================
	 */

	public Request<?> download(Object tag, String url, String cacheKey, String downloadPath, String fileName, RequestCacheConfig cacheConfig, OnRequestListener<File> onRequestListener);

	public Request<?> download(Object tag, String url, String downloadPath, String fileName, OnRequestListener<File> onRequestListener);
}
