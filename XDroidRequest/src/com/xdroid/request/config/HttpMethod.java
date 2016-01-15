package com.xdroid.request.config;

/**
 * For the default request body definition request method
 * @author Robin
 * @since 2015-05-13 19:31:46
 */
public interface HttpMethod {
    int DEPRECATED_GET_OR_POST = -1;
    int GET = 0;
    int POST = 1;
    int PUT = 2;
    int DELETE = 3;
    int HEAD = 4;
    int OPTIONS = 5;
    int TRACE = 6;
    int PATCH = 7;
}