package com.example.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import com.example.popularmovies.applilcation.ApplicationContext;

public class NetworkUtil {
    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) ApplicationContext.
                instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = cm.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities nc = cm.getNetworkCapabilities(nw);
            if (nc == null) return false;
            if(nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    ||nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    ||nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            ) return true;

            return false;

        } else {
            //Deprecated in Android PI//
            NetworkInfo ninfo = cm.getActiveNetworkInfo();
            return ninfo.isConnected();
        }
    }
}
