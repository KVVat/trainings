package com.example.periodicworker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit
import android.os.IBinder
import android.app.*
import android.graphics.Color

const val DELAY:Long = 15;

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //startWork()
        //startRepeatWork()
        //startPeriodicWork()
        startForegroundService()
    }

    //Run task as a foreground service
    private fun startForegroundService()
    {
        val intent = Intent(application, TestService::class.java)
        intent.putExtra("REQUEST_CODE", 1)
        startForegroundService(intent)
    }

    //One time request
    //There is a way to call this type of Worker from Firebase Message.
    private fun startWork() {
        val testWorker = OneTimeWorkRequestBuilder<TestWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(testWorker)
    }

    //It could call repeatedly...
    //but not works on the sleep mode.
    private fun startRepeatWork() {
        val testWorker = OneTimeWorkRequestBuilder<RepeatWorker>().
                build()
        WorkManager.getInstance(applicationContext).enqueue(testWorker)
    }

    private val workerTag:String="100";

    //It works fine on the sleep mode. But we can't shorten duration of the task under 15 minutes.
    private fun startPeriodicWork(){
        Log.d("MainActivity", "Run Task By Periodic Worker")
        val periodicWork = PeriodicWorkRequest.Builder(
                TestWorker::class.java,java.time.Duration.ofMinutes(1)
        ).apply {
            addTag(workerTag)
        }.build()
        //we need to use this method to avoid the dupe of the workers
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                "this_worker_is_unique!",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWork)
    }
}
//Worker for testing
class TestWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("TestWorker", "WorkManager Test Running:")
        return Result.success()
    }
}
//Worker for run repeatedly
class RepeatWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("TestWorker", "WorkManager RepeatWorker Running:")
        //return Result.SUCCESS
        val testWorker = OneTimeWorkRequestBuilder<RepeatWorker>().
                setInitialDelay(DELAY, TimeUnit.SECONDS).
                build()
        WorkManager.getInstance(applicationContext).enqueue(testWorker)

        return Result.success()
    }
}
// To Use Service
// https://developer.android.com/training/monitoring-device-state/doze-standby.html
// https://developer.android.com/training/scheduling/alarms?hl=ja
// https://akira-watson.com/android/setwindow-repeat.html
class TestService: Service() {
    private lateinit var context:Context
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarmService()
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TestService", "Service Running:")
        val requestCode = intent?.getIntExtra("REQUEST_CODE", 0)

        val channelId = "default"
        val title = context.getString(R.string.app_name)

        val pendingIntent = PendingIntent.getActivity(context, requestCode!!,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
                channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "Silent Notification"

        channel.setSound(null, null)

        channel.enableLights(false)
        channel.lightColor = Color.BLUE

        channel.enableVibration(false)

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel)
            val notification = Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    .setSmallIcon(android.R.drawable.btn_star)
                    .setContentText("Notification")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build()

            // startForeground
            startForeground(1, notification)

        }

        setNextAlarmService(context)

        return START_NOT_STICKY;
    }

    private fun stopAlarmService(){
        val indent = Intent(context, TestService::class.java)
        val pendingIntent = PendingIntent.getService(context, 0, indent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager?.cancel(pendingIntent)
    }

    private fun setNextAlarmService(context:Context){
        val repeatPeriod = (5 * 60 * 1000).toLong()

        val intent = Intent(context, TestService::class.java)

        val startMillis = System.currentTimeMillis() + repeatPeriod

        val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                startMillis, pendingIntent)
    }
}
