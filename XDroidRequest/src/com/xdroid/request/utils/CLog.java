package com.xdroid.request.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * C Log
 * @author Robin
 * @since 2015-12-28 09:31:51
 */
public class CLog {
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;

    private static int sLevel = LEVEL_VERBOSE;
    
    public static String tagPrefix = "system.out";
    
	public static boolean printTagPrefixOnly = false;

	public static boolean allowD = true;
	public static boolean allowE = true;
	public static boolean allowI = true;
	public static boolean allowV = true;
	public static boolean allowW = true;
	public static boolean allowWtf = true;

	private CLog() {
	}
	
	private static String generateTag(StackTraceElement caller) {
		String tag = "%s.%s(L:%d)";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
		tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
		tag = TextUtils.isEmpty(tagPrefix) ? tag : tagPrefix + ":" + tag;
		tag = printTagPrefixOnly?tagPrefix:tagPrefix + ":" + tag;
		return tag;
	}
	
	public static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}

    /**
     * set log level, the level lower than this level will not be logged
     *
     * @param level
     */
    public static void setLogLevel(int level) {
        sLevel = level;
    }
    
    public static void openLog(){
    	allowD = true;
    	allowE = true;
    	allowI = true;
    	allowV = true;
    	allowW = true;
    	allowWtf = true;
    }
    
    public static void closeLog(){
    	allowD = false;
    	allowE = false;
    	allowI = false;
    	allowV = false;
    	allowW = false;
    	allowWtf = false;
    }
    
    /*=========================================================================
     * Verbose
     *========================================================================= 
     */
    
    /**
     * Send a VERBOSE log message.
     *
     * @param msg
     */
    public static void v(String msg) {
    	if (!allowV)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_VERBOSE) {
            return;
        }
        Log.v(tag, msg);
    }
    
    /**
     * Send a VERBOSE log message.
     *
     * @param msg
     * @param throwable
     */
    public static void v(String msg, Throwable throwable) {
    	if (!allowV)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_VERBOSE) {
            return;
        }
        Log.v(tag, msg, throwable);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param msg
     * @param args
     */
    public static void v(String msg, Object... args) {
    	if (!allowV)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_VERBOSE) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.v(tag, msg);
    }

    /*public static void v(String tag, String msg) {
     	if (!allowV)
			return;
    	
        if (sLevel > LEVEL_VERBOSE) {
            return;
        }
        Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable throwable) {
     	if (!allowV)
			return;
     	
        if (sLevel > LEVEL_VERBOSE) {
            return;
        }
        Log.v(tag, msg, throwable);
    }

    public static void v(String tag, String msg, Object... args) {
     	if (!allowV)
			return;
     	
        if (sLevel > LEVEL_VERBOSE) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.v(tag, msg);
    }*/
    
    /*=========================================================================
     * Debug
     *========================================================================= 
     */
    
    /**
     * Send a DEBUG log message
     *
     * @param msg
     */
    public static void d(String msg) {
    	if (!allowD)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
    	
        if (sLevel > LEVEL_DEBUG) {
            return;
        }
        Log.d(tag, msg);
    }

    /**
     * Send a DEBUG log message
     *
     * @param msg
     * @param args
     */
    public static void d(String msg, Object... args) {
    	if (!allowD)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
    	
        if (sLevel > LEVEL_DEBUG) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.d(tag, msg);
    }

    /**
     * Send a DEBUG log message
     *
     * @param msg
     * @param throwable
     */
    public static void d(String msg, Throwable throwable) {
    	if (!allowD)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_DEBUG) {
            return;
        }
        Log.d(tag, msg, throwable);
    }

    /*public static void d(String tag, String msg) {
    	if (!allowD)
			return;
    	
        if (sLevel > LEVEL_DEBUG) {
            return;
        }
        Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Object... args) {
    	if (!allowD)
			return;
    	
        if (sLevel > LEVEL_DEBUG) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable throwable) {
    	if (!allowD)
			return;
    	
        if (sLevel > LEVEL_DEBUG) {
            return;
        }
        Log.d(tag, msg, throwable);
    }*/
    
    /*=========================================================================
     * Info
     *========================================================================= 
     */
    
    /**
     * Send an INFO log message
     *
     * @param msg
     */
    public static void i(String msg) {
    	if (!allowI)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_INFO) {
            return;
        }
        Log.i(tag, msg);
    }

    /**
     * Send an INFO log message
     *
     * @param msg
     * @param args
     */
    public static void i(String msg, Object... args) {
       	if (!allowI)
    			return;
    		StackTraceElement caller = getCallerStackTraceElement();
    		String tag = generateTag(caller);
    		
        if (sLevel > LEVEL_INFO) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.i(tag, msg);
    }

    /**
     * Send an INFO log message
     *
     * @param msg
     */
    public static void i(String msg, Throwable throwable) {
       	if (!allowI)
    			return;
    		StackTraceElement caller = getCallerStackTraceElement();
    		String tag = generateTag(caller);
    		
        if (sLevel > LEVEL_INFO) {
            return;
        }
        Log.i(tag, msg, throwable);
    }

    /*public static void i(String tag, String msg) {
      	if (!allowI)
    			return;
      	
        if (sLevel > LEVEL_INFO) {
            return;
        }
        Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Object... args) {
      	if (!allowI)
    			return;
      	
        if (sLevel > LEVEL_INFO) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable throwable) {
      	if (!allowI)
    			return;
      	
        if (sLevel > LEVEL_INFO) {
            return;
        }
        Log.i(tag, msg, throwable);
    }*/
    
    /*=========================================================================
     * Warn
     *========================================================================= 
     */
    
    /**
     * Send a WARNING log message
     *
     * @param msg
     */
    public static void w(String msg) {
    	if (!allowW)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_WARNING) {
            return;
        }
        Log.w(tag, msg);
    }

    /**
     * Send a WARNING log message
     *
     * @param msg
     * @param args
     */
    public static void w(String msg, Object... args) {
      	if (!allowW)
    			return;
    		StackTraceElement caller = getCallerStackTraceElement();
    		String tag = generateTag(caller);
    		
        if (sLevel > LEVEL_WARNING) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.w(tag, msg);
    }

    /**
     * Send a WARNING log message
     *
     * @param msg
     * @param throwable
     */
    public static void w(String msg, Throwable throwable) {
      	if (!allowW)
    			return;
    		StackTraceElement caller = getCallerStackTraceElement();
    		String tag = generateTag(caller);
    		
        if (sLevel > LEVEL_WARNING) {
            return;
        }
        Log.w(tag, msg, throwable);
    }

    /*public static void w(String tag, String msg) {
     	if (!allowW)
			return;
     	
        if (sLevel > LEVEL_WARNING) {
            return;
        }
        Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Object... args) {
     	if (!allowW)
			return;
     	
        if (sLevel > LEVEL_WARNING) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable throwable) {
     	if (!allowW)
			return;
     	
        if (sLevel > LEVEL_WARNING) {
            return;
        }
        Log.w(tag, msg, throwable);
    }*/
    
    /*=========================================================================
     * Error
     *========================================================================= 
     */
    
    /**
     * Send an ERROR log message
     *
     * @param msg
     */
    public static void e(String msg) {
     	if (!allowE)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_ERROR) {
            return;
        }
        Log.e(tag, msg);
    }

    /**
     * Send an ERROR log message
     *
     * @param msg
     * @param args
     */
    public static void e(String msg, Object... args) {
     	if (!allowE)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_ERROR) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.e(tag, msg);
    }

    /**
     * Send an ERROR log message
     *
     * @param msg
     * @param throwable
     */
    public static void e(String msg, Throwable throwable) {
     	if (!allowE)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_ERROR) {
            return;
        }
        Log.e(tag, msg, throwable);
    }

    /*public static void e(String tag, String msg) {
     	if (!allowE)
			return;
     	
        if (sLevel > LEVEL_ERROR) {
            return;
        }
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Object... args) {
     	if (!allowE)
			return;
     	
        if (sLevel > LEVEL_ERROR) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
     	if (!allowE)
			return;
     	
        if (sLevel > LEVEL_ERROR) {
            return;
        }
        Log.e(tag, msg, throwable);
    }*/
    
    /*=========================================================================
     * What  a Terrible Failure
     *========================================================================= 
     */
    
    /**
     * Send a FATAL ERROR log message
     *
     * @param msg
     */
    public static void f(String msg) {
    	if (!allowWtf)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
    	
        if (sLevel > LEVEL_FATAL) {
            return;
        }
        Log.wtf(tag, msg);
    }

    /**
     * Send a FATAL ERROR log message
     *
     * @param msg
     * @param args
     */
    public static void f(String msg, Object... args) {
     	if (!allowWtf)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_FATAL) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.wtf(tag, msg);
    }

    /**
     * Send a FATAL ERROR log message
     *
     * @param msg
     * @param throwable
     */
    public static void f(String msg, Throwable throwable) {
     	if (!allowWtf)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		
        if (sLevel > LEVEL_FATAL) {
            return;
        }
        Log.wtf(tag, msg, throwable);
    }

    /*public static void f(String tag, String msg) {
     	if (!allowWtf)
			return;
     	
        if (sLevel > LEVEL_FATAL) {
            return;
        }
        Log.wtf(tag, msg);
    }

    public static void f(String tag, String msg, Object... args) {
     	if (!allowWtf)
			return;
     	
        if (sLevel > LEVEL_FATAL) {
            return;
        }
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Log.wtf(tag, msg);
    }

    public static void f(String tag, String msg, Throwable throwable) {
     	if (!allowWtf)
			return;
     	
        if (sLevel > LEVEL_FATAL) {
            return;
        }
        Log.wtf(tag, msg, throwable);
    }*/
}