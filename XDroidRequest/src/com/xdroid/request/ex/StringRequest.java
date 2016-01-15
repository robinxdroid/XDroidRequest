package com.xdroid.request.ex;

import com.xdroid.request.config.RequestCacheConfig;
import com.xdroid.request.interfaces.OnRequestListener;
import com.xdroid.request.response.NetworkResponse;
import com.xdroid.request.response.Response;

/**
 * Get char sequence from network
 * @author Robin
 * @since 2016-01-05 19:15:06
 *
 */
public class StringRequest extends MultipartRequest<String> {
	
	public StringRequest() {
		super();
	}

	public StringRequest(RequestCacheConfig cacheConfig, String url, String cacheKey,
			OnRequestListener<String> onRequestListener) {
		super(cacheConfig, url, cacheKey, onRequestListener);
	}

	@Override
	public Response<String> parseNetworkResponse(NetworkResponse response) {
		   return Response.success(new String(response.data), response.headers);
	}

}
