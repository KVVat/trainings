package com.example.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 0;

    private Button button_notify;
    private Button button_update;
    private Button button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_notify = findViewById(R.id.notify);
        button_update = findViewById(R.id.update);
        button_cancel = findViewById(R.id.cancel);

        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        createNotificationChannel();
        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));
        //


        setNotificationButtonState(true, false, false);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setDeleteIntent(createOnDismissedIntent(this, 101));
        return notifyBuilder;
    }
    public void sendNotification(){
        //
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        updateIntent.putExtra("com.exmple.notifyme.id", 100);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent);
        /* it works on oreo or later
        Intent intent = new Intent(this, DismissReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 100, intent, 0);
        notifyBuilder.setDeleteIntent(pendingIntent);
        */
        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());
        setNotificationButtonState(false, true, true);
    }
    public void updateNotification(View view) {
        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(),R.drawable.mascot_1);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!"));
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        setNotificationButtonState(false, false, true);
    }
    public void cancelNotification(View view) {
        mNotifyManager.cancel(NOTIFICATION_ID);
        setNotificationButtonState(true, false, false);
    }

    void setNotificationButtonState(Boolean isNotifyEnabled, Boolean isUpdateEnabled, Boolean isCancelEnabled) {
        button_notify.setEnabled(isNotifyEnabled);
        button_update.setEnabled(isUpdateEnabled);
        button_cancel.setEnabled(isCancelEnabled);
    }

    /* for Android 8.0 or later*/
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    public void createNotificationChannel()
    {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }

    }
    /* For In-Notification Interaction */
    private NotificationReceiver mReceiver = new NotificationReceiver();
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";
    public class NotificationReceiver extends BroadcastReceiver{
        public NotificationReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int notificationId = intent.getExtras().getInt("com.exmple.notifyme.id");
            if(notificationId==100){
                updateNotification(button_update);
            } else {
                setNotificationButtonState(true,false,false);
            }
        }
    }

    private PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("com.exmple.notifyme.id", notificationId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }
    /* oreo or later
    public class DismissReceiver extends BroadcastReceiver{
        public DismissReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            setNotificationButtonState(true, false, false);
        }
    }*/
}
