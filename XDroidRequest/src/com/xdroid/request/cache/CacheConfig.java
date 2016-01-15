package com.xdroid.request.cache;

import java.io.File;

/**
 * Disk cache configuration
 * @author Robin
 * @since 2015-12-28 16:18:00
 *
 */
public class CacheConfig {
	
	public static long DEFAULT_MAX_SIZE=10*1024*1024;

	public static long DISK_CACHE_MAX_SIZE;
	public static File DISK_CACHE_DIRECTORY;
	public static int DISK_CACHE_APP_VERSION; 
	
	public static int MEMORY_CACHE_MAX_SIZE;
	
}
