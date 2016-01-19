package com.xdroid.request.cache;

import java.io.Serializable;

/**
 * Used to cache bitmap
 * @author Robin
 * @since 2016-01-19 15:12:40
 *
 */
public class BitmapCache implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private byte[] bitmapBytes = null;
	private String name = null;

	public BitmapCache(byte[] bitmapBytes, String name) {
		this.bitmapBytes = bitmapBytes;
		this.name = name;
	}

	public byte[] getBitmapBytes() {
		return this.bitmapBytes;
	}

	public String getName() {
		return this.name;
	}
}
