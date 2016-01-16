package com.xdroid.request.dispatcher;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import com.xdroid.request.RequestContext;
import com.xdroid.request.base.Request;
import com.xdroid.request.cache.CacheData;
import com.xdroid.request.delivered.IDelivery;
import com.xdroid.request.network.HttpError;
import com.xdroid.request.network.HttpException;
import com.xdroid.request.network.Network;
import com.xdroid.request.response.NetworkResponse;
import com.xdroid.request.response.Response;
import com.xdroid.request.utils.CLog;
import com.xdroid.request.utils.NetworkUtils;

import android.annotation.TargetApi;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Process;

/**
 * Provides a thread for performing network dispatch from a queue of requests.
 *@author Robin
 *@since 2015-05-08 12:30
 */
public class NetworkDispatcher extends Thread {
	/** The queue of requests to service. */
    private final BlockingQueue<Request<?>> mQueue;
    
    private final Network mNetwork; 
    private final IDelivery mDelivery;
    
    /** Used for telling us to die. */
    private volatile boolean mQuit = false;
    

    /**
     * Creates a new network dispatcher thread.  You must call {@link #start()}
     * in order to begin processing.
     *
     * @param queue Queue of incoming requests for triage
     */
    public NetworkDispatcher(BlockingQueue<Request<?>> queue,Network network, IDelivery delivery) {
        mQueue = queue;
        mNetwork=network;
        mDelivery=delivery;
    }

    /**
     * Forces this dispatcher to quit immediately.  If any requests are still in
     * the queue, they are not guaranteed to be processed.
     */
    public void quit() {
        mQuit = true;
        interrupt();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void addTrafficStatsTag(Request<?> request) {
        // Tag the request (if API >= 14)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TrafficStats.setThreadStatsTag(request.getTrafficStatsTag());
        }
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (true) {
            final Request<?> request;
            try {
                // Take a request from the queue.
                request = mQueue.take();
            } catch (InterruptedException e) {
                // We may have been interrupted because it was time to quit.
                if (mQuit) {
                    return;
                }
                continue;
            }

            try {
               CLog.d("network-queue-take");

                // If the request was cancelled already, do not perform the
                // network request.
                if (request.isCanceled()) {
                    request.finish();
                    CLog.e("cache-discard-canceled-----------cacheKey:"+request.getCacheKey());
                    continue;
                }

                addTrafficStatsTag(request);

                //Reset the current request has not been paid for
                request.resetDelivered();

                //prepare to request
                mDelivery.postRequestPrepare(request);
                
                //if set "UseCacheDataWhenTimeout" 
                if (request.getRequestCacheConfig().isUseCacheDataWhenTimeout()) {
                	final CacheData<?> cacheData = request.getCache(request.getCacheKey());
                	if (cacheData != null) {
                    	new Timer().schedule(new TimerTask() {
          					
          					@Override
          					public void run() {
          						//hand in main thread to call "onCacheDataLoadFinish"
          						CLog.d("Time has come , Delivered:%s ",request.hasHadResponseDelivered());
          						if (!request.hasHadResponseDelivered()) {
          							mDelivery.postCacheResponse(request, cacheData);
								}
          	               
          					}
          				}, request.getRequestCacheConfig().getTimeController().getTimeout());
					}
           
				}

                // Perform the network request.
                if (NetworkUtils.checkNet(RequestContext.getInstance())){
                    //request.doRequest();
                	NetworkResponse networkResponse=mNetwork.performRequest(request);
                	/*Response<byte[]> response=Response.success(networkResponse.data, networkResponse.headers);*/
                	Response<?> response=request.parseNetworkResponse(networkResponse);
                	mDelivery.postRequestResponse(request, response);
                }else{
                	mDelivery.postError(request, new HttpException("No Network",HttpError.ERROR_NOT_NETWORK));

                }
                
            }catch (HttpException e) {
                CLog.e( "network-http-error NetworkDispatcher Unhandled exception : "+ e.toString());
                mDelivery.postError(request, e);
            } catch (Exception e) {
                CLog.e( "network-http-error NetworkDispatcher Unhandled exception : "+ e.toString());
                mDelivery.postError(request, new HttpException(e.getMessage()));
            }
        }
    }

}
