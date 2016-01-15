package com.xdroid.request.retry;

import com.xdroid.request.network.HttpException;

/**
 * Retry policy for a request.
 * @author Robin
 * @since 2015-08-19 10:00:59
 */
public interface RetryPolicy {
	
    /**
     * Returns the current timeout 
     */
    public int getCurrentTimeout();
    
    /**
     * Currently retry count
     * @return
     */
    public int getCurrentRetryCount();

    /**
     * Prepares for the next retry by applying a backoff to the timeout.
     * @param error The error code of the last attempt.
     * @throws HttpException In the event that the retry could not be performed 
     */
    public void retry(HttpException error) throws HttpException;
}
