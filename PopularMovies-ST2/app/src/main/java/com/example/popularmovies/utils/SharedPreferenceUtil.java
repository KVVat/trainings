package com.example.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.popularmovies.R;

/**
 * Shared preference wrapper class for this application.
 */
public class SharedPreferenceUtil {
    private SharedPreferences mSharedPref;
    //Key
    private static String SORT_MODE="SortMode";
    private static String FORCE_CHROME="ForceChrome";

    private static SharedPreferenceUtil instance;
    public static synchronized SharedPreferenceUtil getInstance(Context applicationContext){
        if(instance == null)
            instance = new SharedPreferenceUtil(applicationContext);
        return instance;
    }
    private SharedPreferenceUtil(Context applicationContext) {
        mSharedPref = applicationContext.getSharedPreferences(
                applicationContext.getString(R.string.key_pref), Context.MODE_PRIVATE );
    }

    public void setSortMode(int sortMode){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(SORT_MODE, sortMode);
        editor.apply();
    }

    public int getSortMode(){
        return mSharedPref.getInt(SORT_MODE,0);
    }

    public void setForceChorme(Boolean mode){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(FORCE_CHROME, mode);
        editor.apply();
    }

    public Boolean getForceChrome(){
        return mSharedPref.getBoolean(FORCE_CHROME,false);
    }

}

