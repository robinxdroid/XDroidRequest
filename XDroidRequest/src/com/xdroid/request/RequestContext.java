package com.xdroid.request;

import com.xdroid.request.utils.AppUtils;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * Request context
 * 
 * @author Robin
 * @since 2015-12-31 10:59:05
 *
 */
public class RequestContext extends ContextWrapper {

	private static RequestContext sContext;

	public RequestContext(Context base) {
		super(base);
	}

	public static void init(Context context) {
		String processName = AppUtils.getCurProcessName(context);
		String mainProcessName = context.getPackageName();

		if (!mainProcessName.equals(processName)) {
			return;
		}
		if (sContext == null) {
			sContext = new RequestContext(context);
		}
	}

	public static RequestContext getInstance() {
		return sContext;
	}

}
