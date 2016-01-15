package com.xdroid.request.example;

import java.io.File;

import com.xdroid.request.XRequest;

import android.annotation.SuppressLint;
import android.app.Application;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		configXReqeustCache();
		
	}

	@SuppressLint("SdCardPath")
	private void configXReqeustCache() {
		//磁盘缓存路径
		File DISK_CACHE_DIR_PATH = new File("/sdcard/xrequest/diskcache");
		//磁盘缓存最大值
		int DISK_CACHE_MAX_SIZE = 30*1024*1024;
		
		//XRequest.initXRequest(getApplicationContext());
		
		XRequest.initXRequest(getApplicationContext(), DISK_CACHE_MAX_SIZE, DISK_CACHE_DIR_PATH);
	}
}
