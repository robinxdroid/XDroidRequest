package com.xdroid.request.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.xdroid.request.cache.diskcache.DiskLruCache;
import com.xdroid.request.interfaces.OnCacheDataListener;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * manage the disk cache
 * @author Robin
 * @since 2015-05-07 23:31:23
 */
public class DiskCacheManager <ValueType>{
	
	private static DiskCacheManager<?> mCacheManager;

	private  DiskLruCache mDiskLruCache;
	
	private static final int DEFAULT_VALUE_COUNT=1;
	
	public  DiskCacheManager () {
		init();
	}
	
	/*private static volatile DiskCacheManager<CacheData<Entry<?>>> INSTANCE = null;
	
	public static DiskCacheManager<CacheData<Entry<?>>> getInstance() {
		if (INSTANCE == null) {
			synchronized (DiskCacheManager.class) {
				if (INSTANCE == null) {
					INSTANCE = new DiskCacheManager<CacheData<Entry<?>>>();
				}
			}
		}
		return INSTANCE;
	}*/
	
	public static<V> DiskCacheManager<?> getInstance(){
		if (mCacheManager == null) {
			mCacheManager=new DiskCacheManager<V>();
		}
		return mCacheManager;
	}

	private  void init() {
		if (mDiskLruCache==null) {
			open();
		}
		 
	}
	
	public void open(){
		File cacheDir = CacheConfig.DISK_CACHE_DIRECTORY;
         if (!cacheDir.exists()) {  
             cacheDir.mkdirs();  
         } 
         try {
			mDiskLruCache=DiskLruCache.open(cacheDir, CacheConfig.DISK_CACHE_APP_VERSION, DEFAULT_VALUE_COUNT, CacheConfig.DISK_CACHE_MAX_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * set the data to disk cache
	 * @param originalKey
	 */
	public void setDataToDiskCache(String originalKey,ValueType value){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		 try {  
             String key = hashKeyForDisk(originalKey);  
             DiskLruCache.Editor editor = mDiskLruCache.edit(key);  
             if (editor != null) {  
                 OutputStream outputStream = editor.newOutputStream(0);  
                 ObjectOutputStream oos=new ObjectOutputStream(outputStream);
                 if (value!=null) {
                	 
                	 if (value instanceof CacheData<?>) {
						@SuppressWarnings("unchecked")
						CacheData<Entry<?>> cacheData = (CacheData<Entry<?>>) value;
						if (cacheData.getEntry().result instanceof Bitmap) {
							Bitmap bitmap = (Bitmap) cacheData.getEntry().result;
							ByteArrayOutputStream baops = new ByteArrayOutputStream();  
					        bitmap.compress(CompressFormat.PNG, 0, baops);  
					        BitmapCache bitmapCache = new BitmapCache(baops.toByteArray(), "bitmap_cache.png");
					        
					        Entry<BitmapCache> entry = new Entry<BitmapCache>(bitmapCache, cacheData.getEntry().responseHeaders);
					        CacheData<Entry<BitmapCache>> bitmapCacheData = new CacheData<Entry<BitmapCache>>(entry, cacheData.getExpirationTime(), cacheData.getWriteTime(), cacheData.isNeverExpiry());
					    	oos.writeObject(bitmapCacheData);
						}else {
							oos.writeObject(value);
						}
					}else {
						oos.writeObject(value);
					}

				}
                 /*if (oos!=null) {
					oos.close();
				}*/
                 
                 if (getDataFromDiskCache(originalKey)==null) {  
                     editor.commit();  
                 } else {  
                     editor.abort();  
                 }  
             }  
             mDiskLruCache.flush();  
         } catch (IOException e) {  
             e.printStackTrace();  
         } 

	}
	
	/**
	 * set the data to disk cache by async
	 * @param originalKey
	 * @param value
	 * @param onCacheDataListener
	 */
	public void setDataToDiskCacheAsync(final String originalKey,final ValueType value,final OnCacheDataListener<ValueType> onCacheDataListener){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		new AsyncTask<Void, Void, ValueType>() {

			@Override
			protected ValueType doInBackground(Void... params) {
				setDataToDiskCache(originalKey, value);
				return value;
			}
			
			@Override
			protected void onPostExecute(ValueType result) {
				super.onPostExecute(result);
				onCacheDataListener.onFinish(result);
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	/**
	 * read the data from disk cache
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ValueType getDataFromDiskCache(String originalKey){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		try {  
			    String key = hashKeyForDisk(originalKey);  
			    DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);  
			    if (snapShot != null) {  
			        InputStream is = snapShot.getInputStream(0);  
			        ObjectInputStream ois=new ObjectInputStream(is);
			        try {
						ValueType value=(ValueType) ois.readObject();
			        	
			        	if (value instanceof CacheData<?>) {
							CacheData<Entry<?>> cacheData = (CacheData<Entry<?>>) value;
							if (cacheData.getEntry().result instanceof BitmapCache) {
								BitmapCache bitmapCache = (BitmapCache) cacheData.getEntry().result;
								byte[] data = bitmapCache.getBitmapBytes();
								Bitmap resultBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
								
								  Entry<Bitmap> entry = new Entry<Bitmap>(resultBitmap, cacheData.getEntry().responseHeaders);
							      CacheData<Entry<Bitmap>> bitmapCacheData = new CacheData<Entry<Bitmap>>(entry, cacheData.getExpirationTime(), cacheData.getWriteTime(), cacheData.isNeverExpiry());
							      
							      return (ValueType) bitmapCacheData;
							}
						}
			        	
						return value;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
			    }  
			} catch (IOException e) {  
			    e.printStackTrace();  
			    return null;
			}
		return null; 

	}
	
	/**
	 * read the data from disk cache by Async
	 */
	public void getDataFromDiskCacheAsync(final String originalKey,final OnCacheDataListener<ValueType> onCacheDataListener){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		new AsyncTask<Void, Void, ValueType>() {

			@Override
			protected ValueType doInBackground(Void... params) {
				return getDataFromDiskCache(originalKey);
			}
			
			@Override
			protected void onPostExecute(ValueType result) {
				super.onPostExecute(result);
				onCacheDataListener.onFinish(result);
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	/**
	 * delete one data
	 * @param originalKey
	 * @return
	 */
	public Boolean deleteOneDiskCacheData(String originalKey){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		try {
			String key = hashKeyForDisk(originalKey);  
			return mDiskLruCache.remove(key);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void deleteAllDiskCacheData(){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		if (mDiskLruCache != null) {
			try {
				mDiskLruCache.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public long getAllDiskCacheSize(){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		if (mDiskLruCache != null) {
			return mDiskLruCache.size();
		}
		return 0;
	}
	
	public void setDiskCacheMaxSize(long maxSize){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		if (mDiskLruCache != null) {
			mDiskLruCache.setMaxSize(maxSize);
			CacheConfig.DISK_CACHE_MAX_SIZE = maxSize;
		}
	}
	
	public long getDiskCacheMaxSize(){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		if (mDiskLruCache != null) {
			return mDiskLruCache.getMaxSize();
		}
		return 0;
	}
	
	public File getDiskCacheDirectory(){
		if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
			open();
		}
		if (mDiskLruCache != null) {
			return mDiskLruCache.getDirectory();
		}
		return null;
	}
	
	public  static String InputStreamToString(InputStream is) throws Exception{
		int BUFFER_SIZE = 4096;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];  
        int count = -1;  
        while((count = is.read(data,0,BUFFER_SIZE)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        return new String(outStream.toByteArray(),"ISO-8859-1");  
    } 
	
	
	/** 
	 * Using the MD5 algorithm to encrypt the key of the incoming and return. 
	 */  
	  public String hashKeyForDisk(String key) {  
	        String cacheKey;  
	        try {  
	            final MessageDigest mDigest = MessageDigest.getInstance("MD5");  
	            mDigest.update(key.getBytes());  
	            cacheKey = bytesToHexString(mDigest.digest());  
	        } catch (NoSuchAlgorithmException e) {  
	            cacheKey = String.valueOf(key.hashCode());  
	        }  
	        return cacheKey;  
	    }    
	    
	  private String bytesToHexString(byte[] bytes) {
	    	StringBuilder sb = new StringBuilder();  
	        for (int i = 0; i < bytes.length; i++) {  
	            String hex = Integer.toHexString(0xFF & bytes[i]);  
	            if (hex.length() == 1) {  
	                sb.append('0');  
	            }  
	            sb.append(hex);  
	        }  
	        return sb.toString(); 
	     }

	      
	    /** 
	     * Record cache synchronization to the journal file. 
	     */  
	    public void fluchCache() {  
	        if (mDiskLruCache != null) {  
	            try {  
	                mDiskLruCache.flush();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }  

}
