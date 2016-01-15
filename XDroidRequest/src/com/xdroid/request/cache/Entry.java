package com.xdroid.request.cache;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * The cached data entity, including the request header and the request content
 * @author Robin
 * @since 2015-06-24 11:21:41
 */
@SuppressWarnings("serial")
public class Entry<T>  implements Serializable{

	/** Request's content for cache*/
	public T result;
	/** Request's headers for cache */
    public Map<String, String> responseHeaders = Collections.emptyMap();
    
	public Entry(T result, Map<String, String> responseHeaders) {
		super();
		this.result = result;
		this.responseHeaders = responseHeaders;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Map<String, String> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Map<String, String> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
    
	
    
}
