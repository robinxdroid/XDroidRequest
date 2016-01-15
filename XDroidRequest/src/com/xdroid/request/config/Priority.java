package com.xdroid.request.config;

/**
 * Priority values.  Requests will be processed from higher priorities to lower priorities, in FIFO order.
 * @author Robin
 * @since 2015-05-10 00:58:01
 */
public enum Priority {
    LOW,
    NORMAL,
    HIGH,
    IMMEDIATE
}