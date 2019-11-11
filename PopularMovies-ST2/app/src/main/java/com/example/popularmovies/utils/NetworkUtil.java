package com.example.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import com.example.popularmovies.applilcation.ApplicationContext;

/**
 * isOnline mechod checks, is device network available.
 */
public class NetworkUtil {
    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) ApplicationContext.
                instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Network nw = cm.getActiveNetwork();
            if (nw == null) return false;

            NetworkCapabilities nc = cm.getNetworkCapabilities(nw);
            if (nc == null) return false;

            return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    ||nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    ||nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            );
        } else {
            //Deprecated in Android PI//
            NetworkInfo ninfo = cm.getActiveNetworkInfo();
            if(ninfo != null) {
                return ninfo.isConnected();
            } else {
                return false;
            }
        }
    }
}
