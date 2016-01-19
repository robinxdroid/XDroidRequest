package com.xdroid.request.config;

/**
 * cache used configuration
 * 
 * @author Robin
 * @since 2015-05-07 13:06:54
 */
public class RequestCacheConfig {
	
	/** Default expiration time */
	public static final long DEFAULT_EXPIRATION_TIME=30*1000;
	
	/** Default timeout */
	public static final long DEFAULT_TIMEOUT=20*1000;

	/**
	 * whether allow cache data
	 */
	private boolean shouldCache;

	/**
	 * use cache data first ,no matter the cache have expired ,then update cache
	 * when request finish
	 */
	private boolean useCacheDataAnyway;

	/**
	 * use cache data if request failed
	 */
	private boolean useCacheDataWhenRequestFailed;

	/**
	 * use cache data if the cache data is not expired
	 */
	private boolean useCacheDataWhenUnexpired;

	/**
	 * use cache data if timeout
	 */
	private boolean useCacheDataWhenTimeout;

	/**
	 * Retry if request failed
	 */
	private boolean retryWhenRequestFailed;
	
	/**
	 * Set cache never expired.
	 */
	private boolean isNeverExpired;

	/**
	 * control expirationtime and timeout
	 */
	private TimeController timeController;

	public RequestCacheConfig() {
		super();
	}

	public RequestCacheConfig(boolean shouldCache, boolean useCacheDataAnyway, boolean useCacheDataWhenRequestFailed,
			boolean useCacheDataWhenUnexpired, boolean useCacheDataWhenTimeout, boolean retryWhenRequestFailed,
			boolean isNeverExpired,TimeController timeController) {
		super();
		this.shouldCache = shouldCache;
		this.useCacheDataAnyway = useCacheDataAnyway;
		this.useCacheDataWhenRequestFailed = useCacheDataWhenRequestFailed;
		this.useCacheDataWhenUnexpired = useCacheDataWhenUnexpired;
		this.useCacheDataWhenTimeout = useCacheDataWhenTimeout;
		this.retryWhenRequestFailed = retryWhenRequestFailed;
		this.isNeverExpired = isNeverExpired;
		this.timeController = timeController;
	}

	public boolean isShouldCache() {
		return shouldCache;
	}

	public RequestCacheConfig setShouldCache(boolean shouldCache) {
		this.shouldCache = shouldCache;
		return this;
	}

	public boolean isUseCacheDataAnyway() {
		return useCacheDataAnyway;
	}

	public RequestCacheConfig setUseCacheDataAnyway(boolean useCacheDataAnyway) {
		this.useCacheDataAnyway = useCacheDataAnyway;
		return this;
	}

	public boolean isUseCacheDataWhenRequestFailed() {
		return useCacheDataWhenRequestFailed;
	}

	public RequestCacheConfig setUseCacheDataWhenRequestFailed(boolean useCacheDataWhenRequestFailed) {
		this.useCacheDataWhenRequestFailed = useCacheDataWhenRequestFailed;
		return this;
	}

	public boolean isUseCacheDataWhenUnexpired() {
		return useCacheDataWhenUnexpired;
	}

	public RequestCacheConfig setUseCacheDataWhenUnexpired(boolean useCacheDataWhenUnexpired) {
		this.useCacheDataWhenUnexpired = useCacheDataWhenUnexpired;
		return this;
	}

	public boolean isUseCacheDataWhenTimeout() {
		return useCacheDataWhenTimeout;
	}

	public RequestCacheConfig setUseCacheDataWhenTimeout(boolean useCacheDataWhenTimeout) {
		this.useCacheDataWhenTimeout = useCacheDataWhenTimeout;
		return this;
	}

	public boolean isRetryWhenRequestFailed() {
		return retryWhenRequestFailed;
	}

	public RequestCacheConfig setRetryWhenRequestFailed(boolean retryWhenRequestFailed) {
		this.retryWhenRequestFailed = retryWhenRequestFailed;
		return this;
	}
	
	public boolean isNeverExpired() {
		return isNeverExpired;
	}

	public RequestCacheConfig setNeverExpired(boolean isNeverExpired) {
		this.isNeverExpired = isNeverExpired;
		return this;
	}

	public TimeController getTimeController() {
		return timeController;
	}

	public RequestCacheConfig setTimeController(TimeController timeController) {
		this.timeController = timeController;
		return this;
	}
	
	@Override
	public String toString() {
		return "RequestCacheConfig [shouldCache=" + shouldCache + ", useCacheDataAnyway=" + useCacheDataAnyway
				+ ", useCacheDataWhenRequestFailed=" + useCacheDataWhenRequestFailed + ", useCacheDataWhenUnexpired="
				+ useCacheDataWhenUnexpired + ", useCacheDataWhenTimeout=" + useCacheDataWhenTimeout
				+ ", retryWhenRequestFailed=" + retryWhenRequestFailed + ", isNeverExpired=" + isNeverExpired
				+ ", timeController=" + timeController + "]";
	}

	/**
	 * create a default cache configuration when cacheConfig is null
	 * @return
	 */
	public static RequestCacheConfig buildDefaultCacheConfig() {
		RequestCacheConfig cacheConfig=new RequestCacheConfig();
		cacheConfig.setShouldCache(true); 
		cacheConfig.setUseCacheDataAnyway(false); 
		cacheConfig.setUseCacheDataWhenRequestFailed(true); 
		cacheConfig.setUseCacheDataWhenTimeout(false);
		cacheConfig.setUseCacheDataWhenUnexpired(true);  
		cacheConfig.setRetryWhenRequestFailed(true);
		cacheConfig.setNeverExpired(false);
		
		TimeController timeController=new TimeController();
		timeController.setExpirationTime(DEFAULT_EXPIRATION_TIME);
		timeController.setTimeout(DEFAULT_TIMEOUT);
		cacheConfig.setTimeController(timeController);
		
		return cacheConfig;
	}
	
	public static RequestCacheConfig buildNoCacheConfig() {
		RequestCacheConfig cacheConfig=new RequestCacheConfig();
		cacheConfig.setShouldCache(false); 
		cacheConfig.setUseCacheDataAnyway(false); 
		cacheConfig.setUseCacheDataWhenRequestFailed(false); 
		cacheConfig.setUseCacheDataWhenTimeout(false);
		cacheConfig.setUseCacheDataWhenUnexpired(false);  
		cacheConfig.setRetryWhenRequestFailed(false);
		cacheConfig.setNeverExpired(false);
		
		TimeController timeController=new TimeController();
		timeController.setExpirationTime(0);
		timeController.setTimeout(DEFAULT_TIMEOUT);
		cacheConfig.setTimeController(timeController);
		
		return cacheConfig;
	}
	
	public static RequestCacheConfig buildImageCacheConfig() {
		RequestCacheConfig cacheConfig=new RequestCacheConfig();
		cacheConfig.setShouldCache(true); 
		cacheConfig.setUseCacheDataAnyway(false); 
		cacheConfig.setUseCacheDataWhenRequestFailed(false); 
		cacheConfig.setUseCacheDataWhenTimeout(false);
		cacheConfig.setUseCacheDataWhenUnexpired(true);  
		cacheConfig.setRetryWhenRequestFailed(true);
		cacheConfig.setNeverExpired(true);
		
		TimeController timeController=new TimeController();
		timeController.setTimeout(DEFAULT_TIMEOUT);
		cacheConfig.setTimeController(timeController);
		
		return cacheConfig;
	}
}
