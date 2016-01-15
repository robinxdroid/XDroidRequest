package com.xdroid.request.cache;

import com.xdroid.request.cache.memorycache.LruCache;

/**
 * Manage memory cache
 * @author Robin
 * @since 2015-05-07 23:18:22
 * @param <KeyType>
 * @param <ValueType>
 */
public class MemoryCacheManager <KeyType,ValueType>{
	
	public static MemoryCacheManager<?, ?> mMemoryCacheManager;
	
	private LruCache<KeyType, ValueType> mMemoryCache;
	
	public MemoryCacheManager(){
		if (mMemoryCache==null) {
			mMemoryCache=new LruCache<KeyType,ValueType>(CacheConfig.MEMORY_CACHE_MAX_SIZE){
				@Override
				protected int sizeOf(KeyType key, ValueType value) {
					return super.sizeOf(key, value);
				}
			};
		}
	}
	

	public static <K, V> MemoryCacheManager<?, ?> getInstance() {
		if (mMemoryCacheManager == null) {
			mMemoryCacheManager = new MemoryCacheManager<K, V>();
		}
		return mMemoryCacheManager;
	}
	
	
	/**
	 * set the data to cache
	 * @param key
	 * @param value
	 */
	public void setDataToMemoryCache(KeyType key,ValueType value){
		if (getDataFromMemoryCache(key)==null) {
			mMemoryCache.put(key, value);
		}
	}
	
	/**
	 * read the data from cache
	 * @param key
	 * @return
	 */
	public ValueType getDataFromMemoryCache(KeyType key){
		return mMemoryCache.get(key);
	}
	
	public void deleteAllMemoryCacheData(){
		mMemoryCache.evictAll();
	}
	
	public void deleteOneMemoryCacheData(KeyType key){
		mMemoryCache.remove(key);
	}

}
