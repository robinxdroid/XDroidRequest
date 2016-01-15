package com.xdroid.request.response;

import java.util.Collections;
import java.util.Map;

import com.xdroid.request.network.HttpStatus;

/**
 * The Http response returned from the NetWork, 
 * contains the response that is successful or failed, the request header, the response content, the HTTP status code
 * @author Robin
 * @since 2015-07-02 15:05:31
 */
public class NetworkResponse {

    public NetworkResponse(int statusCode, byte[] data,
            Map<String, String> headers, boolean notModified) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
    }

    public NetworkResponse(byte[] data) {
        this(HttpStatus.SC_OK, data, Collections.<String, String> emptyMap(),
                false);
    }

    public NetworkResponse(byte[] data, Map<String, String> headers) {
        this(HttpStatus.SC_OK, data, headers, false);
    }

    public final int statusCode;

    public final byte[] data;

    public final Map<String, String> headers;

    public final boolean notModified; // If the server returns 304 ( Not Modified ), the true
}