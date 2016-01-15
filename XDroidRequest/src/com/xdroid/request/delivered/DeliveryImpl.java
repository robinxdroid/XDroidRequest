package com.xdroid.request.delivered;

import java.io.File;
import java.util.concurrent.Executor;

import com.xdroid.request.base.Request;
import com.xdroid.request.cache.CacheData;
import com.xdroid.request.cache.Entry;
import com.xdroid.request.network.HttpException;
import com.xdroid.request.response.Response;
import com.xdroid.request.utils.CLog;

import android.os.Handler;

/**
 * HTTP response of the distributor, here used to distribute the response of the
 * asynchronous thread to the UI thread to execute
 * 
 * @author Robin
 * @since 2015-07-02 14:53:25
 */
public class DeliveryImpl implements IDelivery {

	private final Executor mResponsePoster;

	private RequestUploadProgressRunnable mRequestUploadProgressRunnable;
	
	private RequestProgressRunnable mRequestProgressRunnable;

	public DeliveryImpl(final Handler handler) {
		mResponsePoster = new Executor() {
			@Override
			public void execute(Runnable command) {
				handler.post(command);
			}
		};
	}

	public DeliveryImpl(Executor executor) {
		mResponsePoster = executor;
	}

	/*
	 * ========================================================================
	 * Override Delivery
	 * ========================================================================
	 */

	@Override
	public void postRequestResponse(Request<?> request, Response<?> response) {
		// request.markDelivered();
		mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, null));
	}

	@Override
	public void postError(Request<?> request, HttpException error) {
		Response<?> response = Response.error(error);
		mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void postCacheResponse(Request<?> request, T cacheData) {
		mResponsePoster.execute(new ResponseDeliveryRunnable(request, null, (CacheData<Entry<?>>) cacheData));
	}

	@Override
	public void postRequestPrepare(Request<?> request) {
		mResponsePoster.execute(new RequestPrepareRunnable(request));
	}

	@Override
	public void postRequestRetry(Request<?> request, int currentRetryCount, HttpException previousError) {
		mResponsePoster.execute(new RequestRetryRunnable(request, currentRetryCount, previousError));
	}

	@Override
	public void postRequestDownloadProgress(Request<?> request, long transferredBytesSize, long totalSize) {
		if (mRequestProgressRunnable == null) {
			mRequestProgressRunnable = new RequestProgressRunnable(request, transferredBytesSize,
					totalSize);
		}else {
			mRequestProgressRunnable.updateParams(request, transferredBytesSize, totalSize);
		}
		mResponsePoster.execute(mRequestProgressRunnable);
	}

	@Override
	public void postRequestUploadProgress(Request<?> request, long transferredBytesSize, long totalSize, int currentFileIndex, File currentFile) {
		if (mRequestUploadProgressRunnable == null) {
			mRequestUploadProgressRunnable = new RequestUploadProgressRunnable(request, transferredBytesSize,
					totalSize, currentFileIndex,currentFile);
		}else {
			mRequestUploadProgressRunnable.updateParams(request, transferredBytesSize, totalSize, currentFileIndex,currentFile);
		}
		mResponsePoster.execute(mRequestUploadProgressRunnable);
	}

	/*
	 * ========================================================================
	 * Runnable task
	 * ========================================================================
	 */

	/**
	 * Runnable task, for the network request response, access to the cache
	 * results response, failure event, distributed to the UI thread
	 */
	@SuppressWarnings("rawtypes")
	private class ResponseDeliveryRunnable implements Runnable {
		private final Request mRequest;
		private final Response mResponse;
		private final CacheData<Entry<?>> cacheData;

		public ResponseDeliveryRunnable(Request request, Response response, CacheData<Entry<?>> cacheData) {
			mRequest = request;
			mResponse = response;
			this.cacheData = cacheData;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			if (mRequest.isCanceled()) {
				mRequest.finish();
				CLog.e("request is cancel when delivered");
				return;
			}

			if (mResponse != null) {
				CLog.i("Hand in main thread");
				if (mResponse.isSuccess()) {
					mRequest.onRequestFinish(mResponse.headers, mResponse.result);
				} else {
					mRequest.onRequestFailed(mResponse.error);
				}
			} else if (cacheData != null) {
				// When "isUseCacheDataWhenTimeout" is true, you cannot reset
				// the data delivery,
				// otherwise it will be delivered to the cached data again after
				// the request is completed.
				if (!mRequest.getRequestCacheConfig().isUseCacheDataWhenTimeout()) {
					mRequest.resetDelivered();
				}
				mRequest.onCacheDataLoadFinish(cacheData);
			}

			// mRequest.requestFinish();
			// mRequest.finish();

		}
	}

	/**
	 * Runnable task, which is used to distribute the pre request event to the
	 * main thread.
	 */
	private class RequestPrepareRunnable implements Runnable {
		private Request<?> mRequest;

		public RequestPrepareRunnable(Request<?> request) {
			mRequest = request;
		}

		@Override
		public void run() {
			if (mRequest.isCanceled()) {
				mRequest.finish();
				CLog.e("request is cancel when prepare");
				return;
			}

			mRequest.requestPrepare();
		}
	}

	/**
	 * Runnable task, which is used to distribute the pre request event to the
	 * main thread.
	 */
	private class RequestRetryRunnable implements Runnable {
		private Request<?> mRequest;
		private HttpException mPreviousError;
		private int mCurrentRetryCount;

		public RequestRetryRunnable(Request<?> request, int currentRetryCount, HttpException previousError) {
			mRequest = request;
			this.mCurrentRetryCount = currentRetryCount;
			this.mPreviousError = previousError;
		}

		@Override
		public void run() {
			if (mRequest.isCanceled()) {
				mRequest.finish();
				CLog.e("request is cancel when retry");
				return;
			}

			mRequest.onRequestRetry(mCurrentRetryCount, mPreviousError);
		}
	}

	/**
	 * Runnable task, which is used to delivered request progress.
	 */
	private class RequestProgressRunnable implements Runnable {
		private Request<?> mRequest;
		private long mTransferredBytesSize;
		private long mTotalSize;

		public RequestProgressRunnable(Request<?> request, long transferredBytesSize, long totalSize) {
			mRequest = request;
			this.mTransferredBytesSize = transferredBytesSize;
			this.mTotalSize = totalSize;
		}

		@Override
		public void run() {
			if (mRequest.isCanceled()) {
				mRequest.finish();
				CLog.e("request is cancel when on progress");
				return;
			}

			mRequest.onRequestDownloadProgress(mTransferredBytesSize, mTotalSize);
		}
		
		public void updateParams(Request<?> request, long transferredBytesSize, long totalSize) {
			this.mRequest = request;
			this.mTransferredBytesSize = transferredBytesSize;
			this.mTotalSize = totalSize;
		}
	}

	/**
	 * Runnable task, which is used to delivered upload progress.
	 */
	private class RequestUploadProgressRunnable implements Runnable {
		private Request<?> mRequest;
		private long mTransferredBytesSize;
		private long mTotalSize;
		private int mCurrentFileIndex;
		private File mCurrentFile;

		public RequestUploadProgressRunnable(Request<?> request, long transferredBytesSize, long totalSize, int currentFileIndex, File currentFile) {
			this.mRequest = request;
			this.mTransferredBytesSize = transferredBytesSize;
			this.mTotalSize = totalSize;
			this.mCurrentFileIndex = currentFileIndex;
			this.mCurrentFile = currentFile;
		}

		@Override
		public void run() {
			if (mRequest.isCanceled()) {
				mRequest.finish();
				CLog.e("request is cancel when on upload progress");
				return;
			}

			mRequest.onRequestUploadProgress(mTransferredBytesSize, mTotalSize, mCurrentFileIndex, mCurrentFile);
		}

		public void updateParams(Request<?> request, long transferredBytesSize, long totalSize, int currentFileIndex, File currentFile) {
			this.mRequest = request;
			this.mTransferredBytesSize = transferredBytesSize;
			this.mTotalSize = totalSize;
			this.mCurrentFileIndex = currentFileIndex;
			this.mCurrentFile = currentFile;
		}
	}

}
