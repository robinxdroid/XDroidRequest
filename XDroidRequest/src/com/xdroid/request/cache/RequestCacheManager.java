package com.xdroid.request.cache;

import java.io.File;

import com.xdroid.request.base.Request;
import com.xdroid.request.interfaces.IRequestCacheManager;

/**
 * Request cache manager
 * @author Robin
 * @since 2015-12-30 17:27:33
 *
 */
public class RequestCacheManager implements IRequestCacheManager{
	
	private static volatile RequestCacheManager INSTANCE = null;

	public static RequestCacheManager getInstance() {
		if (INSTANCE == null) {
			synchronized (RequestCacheManager.class) {
				if (INSTANCE == null) {
					INSTANCE = new RequestCacheManager();
				}
			}
		}
		return INSTANCE;
	}
	

	//----------------------------------------Disk-------------------------------------------
	
	@Override
	public File getDiskCacheDirectory() {
		@SuppressWarnings("unchecked")
		DiskCacheManager<CacheData<Entry<?>>> diskCacheManager = (DiskCacheManager<CacheData<Entry<?>>>) DiskCacheManager.getInstance();
		return diskCacheManager.getDiskCacheDirectory();
	}

	@Override
	public long getDiskCacheMaxSize() {
		@SuppressWarnings("unchecked")
		DiskCacheManager<CacheData<Entry<?>>> diskCacheManager = (DiskCacheManager<CacheData<Entry<?>>>) DiskCacheManager.getInstance();
		return diskCacheManager.getDiskCacheMaxSize();
	}

	@Override
	public void setDiskCacheMaxSize(long maxSize) {
		@SuppressWarnings("unchecked")
		DiskCacheManager<CacheData<Entry<?>>> diskCacheManager = (DiskCacheManager<CacheData<Entry<?>>>) DiskCacheManager.getInstance();
		diskCacheManager.setDiskCacheMaxSize(maxSize);
	}

	@Override
	public long getAllDiskCacheSize() {
		@SuppressWarnings("unchecked")
		DiskCacheManager<CacheData<Entry<?>>> diskCacheManager = (DiskCacheManager<CacheData<Entry<?>>>) DiskCacheManager.getInstance();
		return diskCacheManager.getAllDiskCacheSize();
	}

	@Override
	public void deleteAllDiskCacheData() {
		@SuppressWarnings("unchecked")
		DiskCacheManager<CacheData<Entry<?>>> diskCacheManager = (DiskCacheManager<CacheData<Entry<?>>>) DiskCacheManager.getInstance();
		diskCacheManager.deleteAllDiskCacheData();
	}

	@Override
	public Boolean deleteOneDiskCacheData(String originalKey) {
		@SuppressWarnings("unchecked")
		DiskCacheManager<CacheData<Entry<?>>> diskCacheManager = (DiskCacheManager<CacheData<Entry<?>>>) DiskCacheManager.getInstance();
		return diskCacheManager.deleteOneDiskCacheData(originalKey);
	}

	@Override
	public Boolean deleteOneDiskCacheData(Request<?> request) {
		request.getCacheManager().deleteOneMemoryCacheData(request.getCacheKey());
		Boolean deleteSuccess = request.getDiskCacheManager().deleteOneDiskCacheData(request.getCacheKey());
		return deleteSuccess;
	}

	@Override
	public Boolean deleteOneDiskCacheData(Request<?> request, String originalKey) {
		request.getCacheManager().deleteOneMemoryCacheData(originalKey);
		Boolean deleteSuccess = request.getDiskCacheManager().deleteOneDiskCacheData(originalKey);
		return deleteSuccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T  getDataFromDiskCache(String originalKey) {
		DiskCacheManager<CacheData<Entry<?>>> diskCacheManager = (DiskCacheManager<CacheData<Entry<?>>>) DiskCacheManager.getInstance();
		return (T) diskCacheManager.getDataFromDiskCache(originalKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T  getDataFromDiskCache(Request<?> request) {
		return (T) request.getDiskCacheManager().getDataFromDiskCache(request.getCacheKey());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDataFromDiskCache(Request<?> request, String originalKey) {
		return (T) request.getDiskCacheManager().getDataFromDiskCache(originalKey);
	}
	
	//----------------------------------------Memory-------------------------------------------

	@Override
	public void deleteOneMemoryCacheData(String key) {
		@SuppressWarnings("unchecked")
		MemoryCacheManager<String, CacheData<Entry<?>>> memoryCacheManager = (MemoryCacheManager<String, CacheData<Entry<?>>>) MemoryCacheManager.getInstance();
		memoryCacheManager.deleteOneMemoryCacheData(key);
	}

	@Override
	public void deleteOneMemoryCacheData(Request<?> request) {
		request.getCacheManager().deleteOneMemoryCacheData(request.getCacheKey());
	}

	@Override
	public void deleteOneMemoryCacheData(Request<?> request, String key) {
		 request.getCacheManager().deleteOneMemoryCacheData(key);
	}

	@Override
	public void deleteAllMemoryCacheData() {
		@SuppressWarnings("unchecked")
		MemoryCacheManager<String, CacheData<Entry<?>>> memoryCacheManager = (MemoryCacheManager<String, CacheData<Entry<?>>>) MemoryCacheManager.getInstance();
		memoryCacheManager.deleteAllMemoryCacheData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDataFromMemoryCache(String key) {
		MemoryCacheManager<String, CacheData<Entry<?>>> memoryCacheManager = (MemoryCacheManager<String, CacheData<Entry<?>>>) MemoryCacheManager.getInstance();
		return (T) memoryCacheManager.getDataFromMemoryCache(key);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDataFromMemoryCache(Request<?> request) {
		return (T) request.getCacheManager().getDataFromMemoryCache(request.getCacheKey());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDataFromMemoryCache(Request<?> request, String key) {
		return (T) request.getCacheManager().getDataFromMemoryCache(key);
	}

}
