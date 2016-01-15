package com.xdroid.request.response;

import java.util.Map;

import com.xdroid.request.network.HttpException;

/**
 * Http request response package type, containing the request header, request
 * content, error content of the request response
 * 
 * @author Robin
 * @since 2015-07-02 15:07:13
 */
public class Response<T> {
	/**
	 * Http type of response
	 */
	public final T result;

	public final HttpException error;

	public final Map<String, String> headers;

	public boolean isSuccess() {
		return error == null;
	}

	private Response(T result, Map<String, String> headers) {
		this.result = result;
		this.headers = headers;
		this.error = null;
	}

	private Response(HttpException error) {
		this.result = null;
		this.headers = null;
		this.error = error;
	}

	/**
	 * To return a successful HttpRespond
	 * 
	 * @param result
	 *            Http type of response
	 * @param headers
	 *            Request header information
	 */
	public static <T> Response<T> success(T result, Map<String, String> headers) {
		return new Response<T>(result, headers);
	}

	/**
	 * To return a failed HttpRespond
	 * 
	 * @param error
	 */
	public static <T> Response<T> error(HttpException error) {
		return new Response<T>(error);
	}
}
