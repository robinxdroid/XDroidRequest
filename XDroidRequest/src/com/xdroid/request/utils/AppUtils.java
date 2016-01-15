package com.xdroid.request.utils;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Process;;
/**
 * APP helper
 * 
 * @author Robin
 * @since 2015-12-31 10:03:05
 *
 */
public class AppUtils {

	public static int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}
	/** 
	 * According to the incoming a unique name for the path of the hard disk cache address.
	 */  
	 public static File getDiskCacheDir(Context context, String uniqueName) {  
	        String cachePath;  
	        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
	                || !Environment.isExternalStorageRemovable()) {
	        	File cacheDir=context.getExternalCacheDir();
	        	if (cacheDir!=null) {
	        		cachePath = cacheDir .getPath();    // /sdcard/Android/data/<application package>/cache 
				}else {
					cachePath = context.getCacheDir().getPath();  // /data/data/<application package>/cache 
				}
	            
	        } else {  
	            cachePath = context.getCacheDir().getPath();  // /data/data/<application package>/cache 
	        }  
	        return new File(cachePath + File.separator + uniqueName);  
	    } 

	public static String getCurProcessName(Context context) {
		int pid = Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService("activity");

		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

}
