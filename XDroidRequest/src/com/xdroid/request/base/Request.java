package com.xdroid.request.base;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;

import com.xdroid.request.cache.CacheData;
import com.xdroid.request.cache.DiskCacheManager;
import com.xdroid.request.cache.Entry;
import com.xdroid.request.cache.MemoryCacheManager;
import com.xdroid.request.config.DataType;
import com.xdroid.request.config.HttpMethod;
import com.xdroid.request.config.Priority;
import com.xdroid.request.config.RequestCacheConfig;
import com.xdroid.request.interfaces.IRequest;
import com.xdroid.request.interfaces.IResponseDelivery;
import com.xdroid.request.interfaces.OnRequestListener;
import com.xdroid.request.network.HttpException;
import com.xdroid.request.queue.RequestQueue;
import com.xdroid.request.response.NetworkResponse;
import com.xdroid.request.response.Response;
import com.xdroid.request.retry.DefaultRetryPolicyImpl;
import com.xdroid.request.retry.RetryPolicy;
import com.xdroid.request.utils.CLog;

import android.net.Uri;
import android.text.TextUtils;


/**
 * Base request
 * @author Robin
 * @since 2015-05-07 17:18:06
 * @param <T> Return data type
 */
public abstract class Request <T> implements IRequest<T>,IResponseDelivery<T> ,Comparable<Request<T>>{
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
	
	/*
	 * properties
	 */
	
	/** This request will request's address */
	private String mUrl;
	
    /** An opaque token tagging this request; used for bulk cancellation. */
    private Object mTag;
    
    /** Whether or not this request has been canceled. */
    private boolean mCanceled = false;
	
    /** Default tag for {@link android.net.TrafficStats}. */
    private  int mDefaultTrafficStatsTag;
    
    /** Whether or not a response has been delivered for this request yet. */
    private boolean mResponseDelivered = false;
    
    /** Sequence number of this request, used to enforce FIFO ordering. */
    private Integer mSequence;
    
    /** Priority of this request ,default is "NORMAL" */
    private Priority mPriority=Priority.NORMAL;
    
	/** The request's cache key if this request need to cache */
	private String cacheKey;
	
	/** This request's cookie */
	private String cookie;
	
	/** The method for request */
	private int httpMethod;
	
	/*
	 * Object
	 */
    /** The request queue this request is associated with. */
    private RequestQueue mRequestQueue;

	/** This request's related configuration */
	public RequestCacheConfig mRequestCacheConfig;
	
	private RetryPolicy retryPolicy;
	
	/** Memory cache manager */
	private MemoryCacheManager<String, CacheData<Entry<T>>> mMemorycacheManager;
	
	/** Disk cache manager */
	private DiskCacheManager<CacheData<Entry<T>>> mDiskCacheManager;
	
	/**The callback when this request perform finished */
	private OnRequestListener<T> onRequestListener;
	
	/*===============================================================================
	 *  constructor
	 *===============================================================================
	 */

    public Request(){
        this(null, null, null,null);
    }
	
	/*public Request(Context context,String url) {
		this(context,null, url, url,null);
	}
	
	public Request(Context context,CacheConfig cacheConfig,String url) {
		this(context,cacheConfig, url, url,null);
	}*/
	
	public Request(RequestCacheConfig cacheConfig,String url,String cacheKey,OnRequestListener<T> onRequestListener) {
		this.mRequestCacheConfig=cacheConfig;
		this.mUrl =url;
		this.cacheKey= cacheKey;
		this.onRequestListener=onRequestListener;
		
		if (cacheConfig==null) {
			setRequestCacheConfig(RequestCacheConfig.buildDefaultCacheConfig());
		}
		mDefaultTrafficStatsTag = findDefaultTrafficStatsTag(url);
		
		retryPolicy =new DefaultRetryPolicyImpl();
		
		initCacheManager();
		
	}

	@SuppressWarnings("unchecked")
	private void initCacheManager() {
		mMemorycacheManager = (MemoryCacheManager<String, CacheData<Entry<T>>>) MemoryCacheManager.getInstance();
		mDiskCacheManager = (DiskCacheManager<CacheData<Entry<T>>>) DiskCacheManager.getInstance();
	}
	
	
	/*===============================================================================
	 *  Getters and Setters
	 *===============================================================================
	 */
	
	public RequestCacheConfig getRequestCacheConfig() {
		return mRequestCacheConfig;
	}

	public Request<?> setRequestCacheConfig(RequestCacheConfig requestCacheConfig) {
		this.mRequestCacheConfig = requestCacheConfig;
		return this;
	}
	
	public MemoryCacheManager<String, CacheData<Entry<T>>> getCacheManager() {
		return mMemorycacheManager;
	}

	public DiskCacheManager<CacheData<Entry<T>>> getDiskCacheManager() {
		return mDiskCacheManager;
	}

	public RetryPolicy getRetryPolicy() {
		return retryPolicy;
	}

	public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
		return this;
	}

	public String getUrl() {
		return mUrl;
	}

	public Request<?> setUrl(String url) {
		this.mUrl = url;
        this.cacheKey= url;
    	return this;
	}

	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	public void setRequestQueue(RequestQueue mRequestQueue) {
		this.mRequestQueue = mRequestQueue;
	}
	
	public Object getTag() {
		return mTag;
	}

	public void setTag(Object mTag) {
		this.mTag = mTag;
	}
	
	  /**
     * Mark this request as canceled.  No callback will be delivered.
     */
    public void cancel() {
        mCanceled = true;
    }

    /**
     * Returns true if this request has been canceled.
     */
    public boolean isCanceled() {
        return mCanceled;
    }
    
    public void markDelivered() {
        mResponseDelivered = true;
    }
    
    public void resetDelivered(){
    	mResponseDelivered=false;
    }

    public boolean hasHadResponseDelivered() {
        return mResponseDelivered;
    }

	public Request<?> setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
		return this;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public int getTrafficStatsTag() {
        return mDefaultTrafficStatsTag;
    }
	
	/**
     * @return The hashcode of the URL's host component, or 0 if there is none.
     */
    private static int findDefaultTrafficStatsTag(String url) {
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            if (uri != null) {
                String host = uri.getHost();
                if (host != null) {
                    return host.hashCode();
                }
            }
        }
        return 0;
    }
    
	public Integer getSequence() {
		return mSequence;
	}
	
	public void setSequence(Integer mSequence) {
		this.mSequence = mSequence;
	}
	
	 public void setPriority(Priority priority) {
		 this.mPriority=priority;
	 }
	
    /**
     * Returns the {@link com.xdroid.request.config.Priority} of this request; {@link com.xdroid.request.config.Priority#NORMAL} by default.
     */
    public Priority getPriority() {
        return mPriority;
    }
	
	public OnRequestListener<T> getOnRequestListener() {
	    return onRequestListener;
    }

    public Request<?> setOnRequestListener(OnRequestListener<T> onRequestListener) {
	    this.onRequestListener = onRequestListener;
	    return this;
    }
    
	public int getHttpMethod() {
	   return httpMethod;
    }

    public void setHttpMethod(int httpMethod) {
	   this.httpMethod = httpMethod;
    }
    

    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset="
                + getParamsEncoding();
    }
   
    
		/*===============================================================================
		 *  Override IRequest
		 *===============================================================================
		 */

		@Override
		public void requestPrepare() {
			if (mCanceled) {
				return;
			}
			
			if (mRequestCacheConfig==null) {
				throw new IllegalArgumentException("please use \"setRequestCacheConfig\" method to set a RequestCacheConfig Instance");
			}
			
			CLog.w("<requestPrepare> thread name: %s" , Thread.currentThread().getName());
			
			CLog.d("network-http-prepare");
			
			if (onRequestListener!=null) {
				onRequestListener.onRequestPrepare(this);
			}
            
		}

		@Override
		public void onRequestFinish(Map<String, String> headers,T result) {
			if (mCanceled) {
				return;
			}
			
		    CLog.w("<onRequestFinish> thread name: %s" , Thread.currentThread().getName());
			
		    CLog.d("network-http-complete");
			
	        // If the data has not been delivered, then delivery  
	        if (!hasHadResponseDelivered()) {
	        	if (onRequestListener!=null) {
					onRequestListener.onRequestFinish(this,headers,result);
				}
	        	deliveryResponse(this, headers,result,DataType.NETWORK_DATA);
	        	
	        	// Post the response back.
		          this.markDelivered();
			}
	        
//	        // Write to cache if applicable.
//	        if (this.getRequestCacheConfig().isShouldCache()) {
//	               handlerCache(headers, result);
//	          }
	        
	         //if already delivered
            if (hasHadResponseDelivered()) {
          	  // release the same request in the "mWaitingRequests" map
                finish();
            }
            
		}


		@Override
		public void onRequestFailed(HttpException httpException) {
			if (mCanceled) {
				return;
			}
			
			CLog.w("<onRequestFailed> thread name: %s" , Thread.currentThread().getName());
			
			CLog.d("network-http-failed : "+httpException.toString());
			
			 if (!hasHadResponseDelivered()) {
		        	if (onRequestListener!=null) {
						onRequestListener.onRequestFailed(this,httpException);
					}
                 if (mRequestCacheConfig.isUseCacheDataWhenRequestFailed()) {
                     //read cache
                     CacheData<Entry<T>> cacheData = getCache(getCacheKey());
                     if (cacheData!=null) {
                    	 //deliveryResponse(this, cacheData.getData().responseHeaders,cacheData.getData().data, DataType.CACHE_DATA);
                    	 onCacheDataLoadFinish(cacheData);
                     }
                 }

                 // Post the response back.
                 this.markDelivered();
				}
			 
			 //if already delivered
	            if (hasHadResponseDelivered()) {
	          	  // release the same request in the "mWaitingRequests" map
	                finish();
	            }
	            
		}
		
		@Override
		public void onRequestRetry(int currentRetryCount, HttpException previousError) {
			if (mCanceled) {
				return;
			}
			
		    CLog.w("<onRequestRetry> thread name: %s" , Thread.currentThread().getName());
			
			CLog.d("network-http-retry");
			
			if (onRequestListener!=null) {
				onRequestListener.onRequestRetry(this, currentRetryCount, previousError);
			}
      
		}
		
		@Override
		public void onRequestDownloadProgress(long transferredBytesSize, long totalSize) {
			if (mCanceled) {
				return;
			}
			
			CLog.w("<onRequestDownloadProgress> thread name: %s" , Thread.currentThread().getName());
			
			if (onRequestListener!=null) {
				onRequestListener.onRequestDownloadProgress(this, transferredBytesSize, totalSize);
			}
			
		}
		
		@Override
		public void onRequestUploadProgress(long transferredBytesSize, long totalSize, int currentFileIndex, File currentFile) {
			if (mCanceled) {
				return;
			}
			
			CLog.w("<onRequestUploadProgress> thread name: %s" , Thread.currentThread().getName());
			
			if (onRequestListener!=null) {
				onRequestListener.onRequestUploadProgress(this, transferredBytesSize, totalSize, currentFileIndex, currentFile);
			}
		}
		
		@Override
		public void onCacheDataLoadFinish(CacheData<Entry<T>> cacheData) {
			if (mCanceled) {
				return;
			}
			
            CLog.w("<onCacheDataLoadFinish> thread name: %s" , Thread.currentThread().getName());
			
		    if (!hasHadResponseDelivered()) {
			      if (onRequestListener!=null) {
				    	onRequestListener.onCacheDataLoadFinish(this, cacheData.getEntry().getResponseHeaders(), cacheData.getEntry().getResult());
				  }
			      deliveryResponse(this, cacheData.getEntry().getResponseHeaders(),cacheData.getEntry().getResult(),DataType.CACHE_DATA);	
				  // Post the response back.
			      this.markDelivered();
			}
			
			 //if already delivered
            if (hasHadResponseDelivered()) {
          	  // release the same request in the "mWaitingRequests" map
                finish();
            }
            
		}
		
		@Override
		public  CacheData<Entry<T>> getCache(String key) {
			CacheData<Entry<T>> 	cacheData=mMemorycacheManager.getDataFromMemoryCache(key);
			if (cacheData!=null) {
				CLog.d("cache-hint-memory");
				return cacheData;
			}
			
			cacheData=mDiskCacheManager.getDataFromDiskCache(key);
			if (cacheData!=null) {
				CLog.d("cache-hint-disk");
				return cacheData;
			}
			return null;
		}
		
		@Override
		public void onParseNetworkResponse(NetworkResponse networkResponse, T result) {
			if (mCanceled) {
				return;
			}
			
			CLog.w("<onParseNetworkResponse> thread name: %s" , Thread.currentThread().getName());
			
			if (onRequestListener != null) {
				onRequestListener.onParseNetworkResponse(this, networkResponse, result);
			}
			
			 // Write to cache if applicable.
	        if (this.getRequestCacheConfig().isShouldCache()) {
	             handlerCache(networkResponse.headers, result);
	        }
	        
		}
		
		@Override
		public String getCookie() {
			return cookie;
		}
		
		@Override
	    public void finish() {
		     if (mRequestQueue != null) {
		         mRequestQueue.finish(this);
		     }
	    }
		
		private void handlerCache(Map<String, String> headers, T result) {
			if (mCanceled) {
				return;
			}
			
			CLog.w("<handlerCache> thread name: %s" , Thread.currentThread().getName());
			
			// write memory cache 
			CacheData<Entry<T>> cacheData=mMemorycacheManager.getDataFromMemoryCache(getCacheKey());
			if (cacheData!=null) {
				cacheData.setWriteTime(System.currentTimeMillis());
				 CLog.d("cache-memory-update");
			 }else {
			    Entry<T> entry=new Entry<T>(result,headers );
				cacheData=new CacheData<Entry<T>>(entry, mRequestCacheConfig.getTimeController().getExpirationTime(), System.currentTimeMillis(), getRequestCacheConfig().isNeverExpired());
			    mMemorycacheManager.setDataToMemoryCache(cacheKey, cacheData);
			    CLog.d("cache-memory-written");
			 }
			 //write disk cache
			 CacheData<Entry<T>> diskCacheData= mDiskCacheManager.getDataFromDiskCache(getCacheKey());
			 if (diskCacheData!=null) {
			    mDiskCacheManager.deleteOneDiskCacheData(getCacheKey());
				CLog.d("cache-disk-delete-old");
			 }
			 Entry<T> entry=new Entry<T>(result,headers );
			 diskCacheData=new CacheData<Entry<T>>(entry, mRequestCacheConfig.getTimeController().getExpirationTime(), System.currentTimeMillis(), getRequestCacheConfig().isNeverExpired());
			 mDiskCacheManager.setDataToDiskCache(getCacheKey(), diskCacheData);
			 CLog.d("cache-disk-written");
			   
		}
		
		/*===============================================================================
		 *  Override IResponseDelivery
		 *===============================================================================
		 */

		@Override
		public void deliveryResponse(Request<?> request,Map<String, String> headers, T result,DataType dataType) {
			CLog.w("<deliveryResponse( onDone )> thread name: %s" , Thread.currentThread().getName());
			
			if (onRequestListener!=null) {
				onRequestListener.onDone(this,headers, result, dataType);
			}
	
			//LOG
			String requestMethod = "";
			switch (getHttpMethod()) {
			case HttpMethod.GET:
				requestMethod = "GET";
				break;
			case HttpMethod.DELETE:
				requestMethod = "DELETE";
				break;
			case HttpMethod.POST:
				requestMethod = "POST";
				break;
			case HttpMethod.PUT:
				requestMethod = "PUT";
				break;
			case HttpMethod.HEAD:
				requestMethod = "HEAD";
				break;
			case HttpMethod.OPTIONS:
				requestMethod = "OPTIONS";
				break;
			case HttpMethod.TRACE:
				requestMethod = "TRACE";
				break;
			case HttpMethod.PATCH:
				requestMethod = "PATCH";
				break;
			}
			
			try {
				CLog.i( "|Reponse Delivered|"+
			            "\n[DataType] : "+dataType+
			            "\n[CacheKey] : "+getCacheKey()+ 
			            "\n[Tag] : " + getTag() +
			            "\n[URL] : "+getUrl()+
						"\n[Method] : "+ requestMethod +
						"\n[Headers] : "+ getHeaders() +
						"\n[Params] : "+ getParams() +
						"\n[Result Headers] : " + headers +
						"\n[Result Data] : "+result );
			} catch (Exception e) {
				e.printStackTrace();
				CLog.e("deliveryResponse（onDone）print log error：%s",e.toString());
			}
			
		}
		
		/*===============================================================================
		 *  Abstract
		 *===============================================================================
		 */
		
		/**
		 * Used for printing log
		 * @return
		 */
		public abstract String getParams();
		
		/**
		 * If the request parameters, the subclass must be rewritten
		 * @param connection For network request "HttpURLConnection"
		 */
		public abstract void buildBody(HttpURLConnection connection);
		
		/**
		 * The network request results resolved as want to format, subclasses must override
		 * @param response
		 * @return
		 */
		public abstract  Response<T> parseNetworkResponse(NetworkResponse response);

		
		/*===============================================================================
		 *  Override Comparable
		 *===============================================================================
		 */
		@Override
		public int compareTo(Request<T> another) {
			Priority left = this.getPriority();
	        Priority right = another.getPriority();

	        // High-priority requests are "lesser" so they are sorted to the front.
	        // Equal priorities are sorted by sequence number to provide FIFO ordering.
	        return left == right ?this.mSequence - another.mSequence : right.ordinal() - left.ordinal();
		}


}
