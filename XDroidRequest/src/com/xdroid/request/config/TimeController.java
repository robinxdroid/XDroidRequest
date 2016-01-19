package com.xdroid.request.config;

/**
 * control the expiration time and timeout
 * 
 * @author Robin
 * @since 2015-05-07 20:17:47
 */
public class TimeController {

	private long expirationTime;

	private long timeout;

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		return "TimeController [expirationTime=" + expirationTime + ", timeout=" + timeout + "]";
	}
	
}
