package com.xdroid.request.network;

import java.io.IOException;
import java.util.Map;

import com.xdroid.request.base.Request;

/**
 * Http request terminal, known to achieve class:
 * @author Robin
 * @since 2015-07-02 16:13:43
 * 
 */
public interface HttpStack {
    /**
     * Let the Http request for a Request
     * 
     * @param request
     * @param additionalHeaders Attached Http request header
     * @return A Http response
     */
    public HttpResponse performRequest(Request<?> request,
            Map<String, String> additionalHeaders) throws IOException;

}
