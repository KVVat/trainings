package com.example.jissample

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat.getExtras
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.io.UnsupportedEncodingException
import java.security.NoSuchAlgorithmException
import java.util.*


class WorkerIntentService: JobIntentService() {
    companion object {
        val JOB_ID:Int = 1000
        fun enqueueWork(context:Context,work:Intent){
            enqueueWork(context,
                WorkerIntentService::class.java,JOB_ID,work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        try {
            //updateTask(intent.extras.getInt("data"))
            val bundle = Bundle()
            if (intent.hasExtra("receiver")) {
                val bundle = Bundle()
                bundle.putString(
                    "data",
                    String.format(
                        Locale.getDefault(),
                        "Result JobIntent Service %d",
                        intent.extras.getInt("data")
                    )
                )
                val mResultReceiver:WorkerResultReceiver = intent.getParcelableExtra("receiver")
                mResultReceiver.onReceiveResult(RESULT_OK, bundle)
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

    }
    /*static final int JOB_ID = 1000;
    static final String WORK_DOWNLOAD_ARTWORK = ".DOWNLOAD_ARTWORK";
    ArtworkDownloader mDownloader;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, Update.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDownloader = ArtworkDownloader.getSequencialDownloader();
    }

    @Override
    protected void onHandleWork(Intent intent) {
        // enqueueWork()에 의해 적재된 인텐트는 여기로 전달됩니다.
        if (WORK_DOWNLOAD_ARTWORK.equals(intent.getAction())) {
            mDownloader.download(intent.getStringExtra("URL"))
        }
    }

    @Override
    public boolean onStopCurrentWork() {
        // 현재 처리 중인 동작들을 중지해야 할 경우
        return !mDownloader.isFinished();
    }*/
}