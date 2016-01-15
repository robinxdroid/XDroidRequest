package com.xdroid.request.dispatcher;

import java.util.concurrent.BlockingQueue;

import com.xdroid.request.base.Request;
import com.xdroid.request.cache.CacheData;
import com.xdroid.request.delivered.IDelivery;

import android.os.Process;
import android.util.Log;

/**
 * Provides a thread for performing cache triage on a queue of requests.
 *@author Robin
 *@since 2015-05-08 16:20:45
 */
public class CacheDispatcher extends Thread {

    private static final boolean DEBUG = true;

	private static final String Tag = "system.out";

    /** The queue of requests coming in for triage. */
    private final BlockingQueue<Request<?>> mCacheQueue;

    /** The queue of requests going out to the network. */
    private final BlockingQueue<Request<?>> mNetworkQueue;

    /** Used for telling us to die. */
    private volatile boolean mQuit = false;
    
    private final IDelivery mDelivery;
    
   /* @SuppressLint("HandlerLeak")
	private Handler handler=new Handler(){
    	@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			HashMap<String, Object> hashMap=(HashMap<String, Object>) msg.obj;
			CacheData<String> cacheData = (CacheData<String>) hashMap.get("data");
    		Request<?> request=(Request<?>) hashMap.get("request");
    		
    		//Reset the current request has not been paid for
            request.resetDelivered();
    		
    		request.onCacheDataLoadFinish(cacheData);
    	};
    };*/

    /**
     * Creates a new cache triage dispatcher thread.  You must call {@link #start()}
     * in order to begin processing.
     *
     * @param cacheQueue Queue of incoming requests for triage
     * @param networkQueue Queue to post requests that require network to
     */
    public CacheDispatcher(BlockingQueue<Request<?>> cacheQueue, BlockingQueue<Request<?>> networkQueue,IDelivery delivery) {
        mCacheQueue = cacheQueue;
        mNetworkQueue = networkQueue;
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

    @Override
    public void run() {
        if (DEBUG) 
        	Log.v(Tag,"start new dispatcher");
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        while (true) {
            try {
                // Get a request from the cache triage queue, blocking until
                // at least one is available.
                final Request<?> request = mCacheQueue.take();
                Log.d(Tag,"cache-queue-take");

                // If the request has been canceled, don't bother dispatching it.
                if (request.isCanceled()) {
                    request.finish();
                    Log.e(Tag, "cache-discard-canceled-----------cacheKey:"+request.getCacheKey());
                    continue;
                }
                // use the cache data always 
                if (request.getRequestCacheConfig().isUseCacheDataAnyway()) {
                	CacheData<?> cacheData = request.getCache(request.getCacheKey());
                	 // Attempt to retrieve this item from cache.
                    if (cacheData == null) {
                        Log.d(Tag,"cache-miss");
                        // Cache miss; send off to the network dispatcher.
                        mNetworkQueue.put(request);
                        continue;
                    }

                    // We have a cache hit; parse its data for delivery back to the request.
                    Log.d(Tag,"cache-hit");
                    
                    //hand in main thread to call "onCacheDataLoadFinish"
                    /*Message msg = handler.obtainMessage(); 
                    HashMap<String, Object> hashMap=new HashMap<>();
                    hashMap.put("data", cacheData);
                    hashMap.put("request", request);
			        msg.obj = hashMap; 
			        handler.sendMessage(msg);*/
			        
			        mDelivery.postCacheResponse(request, cacheData);
                    
                    mNetworkQueue.put(request);
                    
					continue;
				}

                // use the cache data when the cache data is not expired 
                if (request.getRequestCacheConfig().isUseCacheDataWhenUnexpired()) {
                	CacheData<?> cacheData = request.getCache(request.getCacheKey());
                	 // Attempt to retrieve this item from cache.
                    if ( cacheData == null) {
                        Log.d(Tag,"cache-miss");
                        // Cache miss; send off to the network dispatcher.
                        mNetworkQueue.put(request);
                        continue;
                    }

                    // If it is completely expired, just send it to the network.
                    if (cacheData.isExpired()) {
                    	Log.d(Tag,"cache-hit-expired");
                        //request.setCacheEntry(entry);
                        mNetworkQueue.put(request);
                        continue;
                    }

                    // We have a cache hit; parse its data for delivery back to the request.
                    Log.d(Tag,"cache-hit");

                    //hand in main thread to call "onCacheDataLoadFinish"
                    /*Message msg = handler.obtainMessage(); 
                    HashMap<String, Object> hashMap=new HashMap<>();
                    hashMap.put("data", cacheData);
                    hashMap.put("request", request);
			        msg.obj = hashMap; 
			        handler.sendMessage(msg);*/
			        
			        mDelivery.postCacheResponse(request, cacheData);
                    
				}else {
					   mNetworkQueue.put(request);
				}
               

            } catch (InterruptedException e) {
                // We may have been interrupted because it was time to quit.
                if (mQuit) {
                    return;
                }
                continue;
            }
        }
    }
}
