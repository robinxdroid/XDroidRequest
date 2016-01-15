package com.xdroid.request.network;

/**
 * Request error code 
 * @author Robin
 * @since 2015-12-29 14:52:10
 */
public interface HttpError {

	public int ERROR_UNKNOW = 0x01,
			ERROR_NOT_NETWORK = 0x02,
			ERROR_SERVER = 0x03,
			ERROR_SOCKET_TIMEOUT = 0x04,
			ERROR_UNAUTHORIZED = 0x05,
			ERROR_REDIRECT = 0x06,
			ERROR_RESPONSE_NULL = 0x07,
	        ERROR_PARSE = 0x08,
	        ERROR_NO_CONNECTION = 0x09;
}
