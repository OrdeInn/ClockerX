package com.orderinn.clockerx;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {

    int id;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ALARMINFO", "Service is running");



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        id = intent.getIntExtra("Id", 0);

        Intent receiverIntent = new Intent(this, AlarmReceiver.class);
        receiverIntent.putExtra("Object", intent.getStringExtra("Object"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        startService(pendingIntent);

        return START_STICKY;
    }



    private void startService(PendingIntent pendingIntent){
        if(Build.VERSION.SDK_INT >=26) {

            String NOTIFICATION_CHANNEL_ID = "com.orderinn.clockerx";
            String channelName = "Alarm Service";
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Alarm")
                    .setContentText("")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setFullScreenIntent(pendingIntent, true);


            Notification notification = notificationBuilder.build();

            startForeground(id, notification);
        }else {
            startForeground(-1, new Notification());
        }


    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
