package com.xdroid.request.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Provide some help about the network function
 * @author Robin
 * @since 2015/5/28 11:23.
 */
public class NetworkUtils {

    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;
    }
    
    /*private static String getHeader(HttpResponse response, String key) {
        return response.getHeaders().get(key);
    }

    public static boolean isSupportRange(HttpResponse response) {
        if (TextUtils.equals(getHeader(response, "Accept-Ranges"), "bytes")) {
            return true;
        }
        String value = getHeader(response, "Content-Range");
        return value != null && value.startsWith("bytes");
    }
    
    public static boolean isGzipContent(HttpResponse response) {
        return TextUtils
                .equals(getHeader(response, "Content-Encoding"), "gzip");
    }*/
}
