/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
