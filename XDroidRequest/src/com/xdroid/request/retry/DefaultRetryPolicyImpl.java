package com.xdroid.request.retry;

import com.xdroid.request.network.HttpException;
import com.xdroid.request.utils.CLog;

/**
 * Default retry policy for request.
 * @author Robin
 * @since 2015-08-19 10:08:59
 *
 */
public class DefaultRetryPolicyImpl implements RetryPolicy {
    private int mCurrentTimeoutMs;

    private int mCurrentRetryCount;

    private final int mMaxNumRetries;

    private final float mBackoffMultiplier;

    public static final int DEFAULT_TIMEOUT_MS = 2500;

    public static final int DEFAULT_MAX_RETRIES = 2;

    public static final float DEFAULT_BACKOFF_MULT = 1f;

    /*==================================================================
     *  Constructor
     *==================================================================
     */

    public DefaultRetryPolicyImpl() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    public DefaultRetryPolicyImpl(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
    	mCurrentTimeoutMs = initialTimeoutMs;
        mMaxNumRetries = maxNumRetries;
        mBackoffMultiplier = backoffMultiplier;
    }
    
    /*==================================================================
     *  Override RetryPolicy
     *==================================================================
     */

    @Override
    public void retry(HttpException error) throws HttpException {
        mCurrentRetryCount++;
        mCurrentTimeoutMs  += (mCurrentTimeoutMs  * mBackoffMultiplier);
        if (!canContinueToTry()) {
            throw error;
        }
        
   	    CLog.w("request failed , is to try the request again，current retry count = %s ， current timeout = %s", mCurrentRetryCount,mCurrentTimeoutMs );
      
    }

    protected boolean canContinueToTry () {
        return mCurrentRetryCount <= mMaxNumRetries;
    }
    

    public float getBackoffMultiplier() {
        return mBackoffMultiplier;
    }
    
    /*==================================================================
     *  Getter and Setter
     *==================================================================
     */

	@Override
	public int getCurrentTimeout() {
		return mCurrentTimeoutMs;
	}

	@Override
	public int getCurrentRetryCount() {
		return mCurrentRetryCount;
	}
	
	
}
