package com.example.background.workers;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.background.Constants;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BlurWorker extends Worker {
    public BlurWorker(@NonNull Context appContext,@NonNull WorkerParameters workerParameters){
        super(appContext,workerParameters);
    }

    private static final String TAG = BlurWorker.class.getSimpleName();

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();

        // ADD THIS LINE
        String resourceUri = getInputData().getString(Constants.KEY_IMAGE_URI);
        try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri");
                throw new IllegalArgumentException("Invalid input uri");
            }

            ContentResolver resolver = applicationContext.getContentResolver();
            // Create a bitmap
            Bitmap picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri)));
            /*
            Bitmap picture = BitmapFactory.decodeResource(
                    applicationContext.getResources(),
                    R.drawable.test
            );*/
            Bitmap output = WorkerUtils.blurBitmap(picture,applicationContext);
            Uri outputUri = WorkerUtils.writeBitmapToFile(applicationContext,output);
            WorkerUtils.makeStatusNotification(
                    "Output is"+outputUri.toString(),applicationContext
            );

            Data outputData = new Data.Builder()
                    .putString(Constants.KEY_IMAGE_URI, outputUri.toString())
                    .build();

            return Result.success(outputData);
        } catch(Throwable anyThrowable){
            Log.e(TAG,"Error applying blur",anyThrowable);
            return Result.failure();
        }
    }
}
