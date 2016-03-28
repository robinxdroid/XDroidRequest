package com.xdroid.request.ex;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.xdroid.request.config.RequestCacheConfig;
import com.xdroid.request.interfaces.OnRequestListener;
import com.xdroid.request.response.NetworkResponse;
import com.xdroid.request.response.Response;
import com.xdroid.request.utils.CLog;
import com.xdroid.request.utils.GenericsUtils;
/**
 * Parse the result by "GSON"
 * 
 * @author Robin
 * @since 2016-01-07 19:55:16
 *
 * @param <T>
 */
public class MultipartGsonRequest<T> extends MultipartRequest<T> {

	private Type mBeanType;
	
	public MultipartGsonRequest() {
		super();
	}

	public MultipartGsonRequest(RequestCacheConfig cacheConfig, String url, String cacheKey,
			OnRequestListener<T> onRequestListener) {
		super(cacheConfig, url, cacheKey, onRequestListener);
		mBeanType = GenericsUtils.getBeanType(onRequestListener);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<T> parseNetworkResponse(NetworkResponse response) {
		String result = new String(response.data);
		
		CLog.d("[Original String Data]:%s",result);

		if (mBeanType.equals(String.class)) {
			T parseResult = (T) result;
			CLog.d("parse network response complete");
			super.onParseNetworkResponse(response, parseResult);

			return Response.success(parseResult, response.headers);
		}

		if (result.startsWith("[") && result.endsWith("]")) {
			T parseResult = (T) fromJsonList(result, mBeanType);
			CLog.d("parse network response complete");
			super.onParseNetworkResponse(response, parseResult);

			return Response.success(parseResult, response.headers);
		}
		if (result.startsWith("{") && result.endsWith("}")) {
			T parseResult = (T) fromJsonObject(result, mBeanType);
			CLog.d("parse network response complete");
			super.onParseNetworkResponse(response, parseResult);

			return Response.success(parseResult, response.headers);
		}
		return null;
	}
	
	public <X> X fromJsonObject(String json, Type cls) {
		Gson gson = new Gson();
		X bean = gson.fromJson(json, cls);
		return bean;
	}

	@SuppressWarnings("unchecked")
	public <X> ArrayList<X> fromJsonList(String json, Type cls) {
		Gson gson = new Gson();
		ArrayList<X> mList = new ArrayList<X>();
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for (final JsonElement elem : array) {
			mList.add((X) gson.fromJson(elem, cls));
		}
		return mList;
	}
}