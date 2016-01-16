package com.xdroid.request.interfaces;

import java.util.Map;

import com.xdroid.request.base.Request;
import com.xdroid.request.config.DataType;

/**
 *  Used to cache thread or request thread after the completion of the delivery data
 * @author Robin
 * @since 2015-05-08 18:04:42
 * @param <T>
 */
public interface IResponseDelivery <T>{
    /**
     * Parses a response from the network or cache and delivers it.
     */
    public void deliveryResponse(Request<?> request,Map<String, String> headers ,T result, DataType dataType);

}
