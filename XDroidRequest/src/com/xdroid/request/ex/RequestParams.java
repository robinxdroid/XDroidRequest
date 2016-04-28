package com.xdroid.request.ex;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xdroid.request.utils.CLog;

import android.text.TextUtils;

/**
 * Request parameters
 * 
 * @author Robin
 * @since 2016-01-08 14:21:27
 *
 */
public class RequestParams extends ConcurrentHashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	private final Map<String, String> mHeaders = new ConcurrentHashMap<String, String>();
	private String mJsonParams;

	/*
	 * =========================================================================
	 * Constructor
	 * =========================================================================
	 */

	public RequestParams() {
	}

	public RequestParams(String cookie) {
		mHeaders.put("cookie", cookie);
	}

	/*
	 * =========================================================================
	 * Override Super
	 * =========================================================================
	 */

	@Override
	public Object put(String key, Object value) {
		if (value instanceof String || value instanceof Integer || value instanceof File) {
			return super.put(key, value);
		} else {
			CLog.e("Parameters must be \"String\", \"int\" and \"File\" one of the three types");
			return null;
		}
	}

	/*
	 * =========================================================================
	 * Public Method
	 * =========================================================================
	 */

	public void putParams(String key, int value) {
		this.putParams(key, value + "");
	}

	public void putParams(String key, String value) {
		put(key, value);
	}

	public void putParams(String key, File value) {
		put(key, value);
	}

	public void putParams(String jsonString) {
		this.mJsonParams = jsonString;
	}

	public void putHeaders(String key, int value) {
		this.putHeaders(key, value + "");
	}

	public void putHeaders(String key, String value) {
		mHeaders.put(key, value);
	}

	public String buildJsonParams() {
		return mJsonParams;
	}

	/**
	 * Converts params into an application/x-www-form-urlencoded encoded string.
	 */
	public StringBuilder buildParameters() {
		StringBuilder result = new StringBuilder();
		try {
			for (ConcurrentHashMap.Entry<String, Object> entry : this.entrySet()) {
				Object value = entry.getValue();
				if (value == null) {
					continue;
				}
				if (value instanceof String || value instanceof Integer) {
					result.append("&");
					result.append(URLEncoder.encode(entry.getKey(), "utf-8"));
					result.append("=");
					result.append(URLEncoder.encode(String.valueOf(value), "utf-8"));
				} else {
					CLog.e("Filter value,Type : %s,Value : %s", value.getClass().getName());
				}
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding not supported: " + "utf-8", e);
		}

	}

	public Map<String, String> buildParametersToMap() {
		Map<String, String> result = new ConcurrentHashMap<String, String>();
		for (ConcurrentHashMap.Entry<String, Object> entry : this.entrySet()) {
			Object value = entry.getValue();
			if (value == null) {
				continue;
			}
			if (value instanceof String) {
				result.put(entry.getKey(), (String) value);
			} else if (value instanceof Integer) {
				result.put(entry.getKey(), (Integer) value + "");
			}
		}
		return result;
	}

	public Map<String, File> buildFileParameters() {
		Map<String, File> fileParams = new ConcurrentHashMap<String, File>();
		for (ConcurrentHashMap.Entry<String, Object> entry : this.entrySet()) {
			Object value = entry.getValue();
			if (value == null) {
				continue;
			}
			if (value instanceof File) {
				fileParams.put(entry.getKey(), (File) value);
			}
		}
		return fileParams;
	}

	public StringBuilder buildQueryParameters() {
		StringBuilder result = new StringBuilder();
		boolean isFirst = true;
		try {
			for (ConcurrentHashMap.Entry<String, Object> entry : this.entrySet()) {
				Object value = entry.getValue();
				if (value == null) {
					continue;
				}
				if (value instanceof String || value instanceof Integer) {
					if (!isFirst) {
						result.append("&");
					} else {
						result.append("?");
						isFirst = false;
					}
					result.append(URLEncoder.encode(entry.getKey(), "utf-8"));
					result.append("=");
					result.append(URLEncoder.encode(String.valueOf(value), "utf-8"));
				} else {
					CLog.e("Filter value,Type : %s,Value : %s", value.getClass().getName());
				}

			}
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding not supported: " + "utf-8", e);
		}

	}

	public Map<String, String> buildHeaders() {
		return mHeaders;
	}

	public boolean hasFileInParams() {
		return buildFileParameters().size() > 0;
	}

	public boolean hasJsonInParams() {
		return !TextUtils.isEmpty(mJsonParams);
	}

	public boolean hasNameValuePairInParams() {
		return buildParameters().length() > 0;
	}

	@Override
	public String toString() {
		if (!TextUtils.isEmpty(mJsonParams)) {
			return mJsonParams;
		}
		return super.toString();
	}

}
