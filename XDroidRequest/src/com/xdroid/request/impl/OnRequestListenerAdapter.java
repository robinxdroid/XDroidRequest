package com.xdroid.request.impl;

import java.io.File;
import java.util.Map;

import com.xdroid.request.base.Request;
import com.xdroid.request.config.DataType;
import com.xdroid.request.interfaces.OnRequestListener;
import com.xdroid.request.network.HttpException;
import com.xdroid.request.response.NetworkResponse;

/**
 * implements "OnRequestListener" interface, if you need to rewrite a callback function, can realize this
 * @author Robin
 * @since 2015/5/27 18:55.
 */
public abstract class OnRequestListenerAdapter<T> implements OnRequestListener<T> {

	@Override
	public void onRequestPrepare(Request<?> request) {
	}

	@Override
	public void onRequestFailed(Request<?> request, HttpException httpException) {
	}

	@Override
	public void onRequestRetry(Request<?> request, int currentRetryCount, HttpException previousError) {
	}
	
	@Override
	public void onRequestDownloadProgress(Request<?> request, long transferredBytesSize, long totalSize) {
	}
	
	@Override
	public void onRequestUploadProgress(Request<?> request, long transferredBytesSize, long totalSize, int currentFileIndex,File currentFile) {
	}

	@Override
	public void onRequestFinish(Request<?> request, Map<String, String> headers, T result) {
	}

	@Override
	public void onCacheDataLoadFinish(Request<?> request, Map<String, String> headers, T result) {
	}
	
	@Override
	public void onParseNetworkResponse(Request<?> request, NetworkResponse networkResponse, T result) {
	}

	@Override
	public void onDone(Request<?> request, Map<String, String> headers, T result, DataType dataType) {
	}
}
