package com.example.location2_2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG="detection_is";
    public DetectedActivitiesIntentService(){
        super(TAG);
        Log.i(TAG,"Intent Service Constructor");
    }
    public DetectedActivitiesIntentService(String tag){
        super(TAG);
    }
    @Override
    public void onCreate() {
        Log.i(TAG,"Intent Service Create");
        super.onCreate();
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG,"Intent Service OnHandle");
        if(intent == null) return;
        ActivityRecognitionResult result =
                ActivityRecognitionResult.extractResult(intent);
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
        Log.i(TAG, "activities detected");
        ArrayList<DetectedActivity> detectedActivities = (
                ArrayList) result.getProbableActivities();localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities);

        localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
