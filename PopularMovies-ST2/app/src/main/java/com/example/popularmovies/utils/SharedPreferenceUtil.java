package com.example.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.popularmovies.R;

public class SharedPreferenceUtil {
    private SharedPreferences mSharedPref;
    private Context appContext;
    private static SharedPreferenceUtil instance;
    public static synchronized SharedPreferenceUtil getInstance(Context applicationContext){
        if(instance == null)
            instance = new SharedPreferenceUtil(applicationContext);
        return instance;
    }
    private SharedPreferenceUtil(Context applicationContext) {
        appContext = applicationContext;
        mSharedPref = appContext.getSharedPreferences(
                appContext.getString(R.string.key_pref), Context.MODE_PRIVATE );
    }

    public void setSortMode(int sortMode){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt("SortMode", sortMode);
        editor.commit();
    }

    public int getSortMode(){
        return mSharedPref.getInt("SortMode",0);
    }

    public void setForceChorme(Boolean mode){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean("ForceChrome", mode);
        editor.commit();
    }

    public Boolean getForceChrome(){
        return mSharedPref.getBoolean("ForceChrome",false);
    }

    public void setFavoriteDirty(Boolean mode){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean("FavoriteDirty", mode);
        editor.commit();
    }

    public Boolean getFavoriteDirty(){
        return mSharedPref.getBoolean("FavoriteDirty",
                false);
    }
}

