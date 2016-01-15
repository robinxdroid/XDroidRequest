package com.xdroid.request.ex;

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
/**
 * Parse the result by "GSON"
 * 
 * @author Robin
 * @since 2016-01-07 19:55:16
 *
 * @param <T>
 */
public class MultipartGsonRequest<T> extends MultipartRequest<T> {

	private Class<?> mBeanClass;

	public MultipartGsonRequest(Class<?> cls) {
		super();
		this.mBeanClass = cls;
	}

	public MultipartGsonRequest(RequestCacheConfig cacheConfig, String url, String cacheKey, Class<?> cls,
			OnRequestListener<T> onRequestListener) {
		super(cacheConfig, url, cacheKey, onRequestListener);
		this.mBeanClass = cls;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<T> parseNetworkResponse(NetworkResponse response) {
		String result = new String(response.data);

		if (mBeanClass.getName().equals(String.class.getName())) {
			T parseResult = (T) result;
			CLog.d("parse network response complete");
			super.onParseNetworkResponse(response, parseResult);

			return Response.success(parseResult, response.headers);
		}

		if (result.startsWith("[") && result.endsWith("]")) {
			T parseResult = (T) fromJsonList(result, mBeanClass);
			CLog.d("parse network response complete");
			super.onParseNetworkResponse(response, parseResult);

			return Response.success(parseResult, response.headers);
		}
		if (result.startsWith("{") && result.endsWith("}")) {
			T parseResult = (T) fromJsonObject(result, mBeanClass);
			CLog.d("parse network response complete");
			super.onParseNetworkResponse(response, parseResult);

			return Response.success(parseResult, response.headers);
		}
		return null;
	}

	public <X> X fromJsonObject(String json, Class<X> cls) {
		Gson gson = new Gson();
		X bean = gson.fromJson(json, cls);
		return bean;
	}

	public <X> ArrayList<X> fromJsonList(String json, Class<X> cls) {
		Gson gson = new Gson();
		ArrayList<X> mList = new ArrayList<X>();
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for (final JsonElement elem : array) {
			mList.add(gson.fromJson(elem, cls));
		}
		return mList;
	}
}