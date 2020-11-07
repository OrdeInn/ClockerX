package com.orderinn.clockerx.AlarmClock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.orderinn.clockerx.R;

public class AlarmService extends Service {

    AlarmObject alarmObject;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ALARM INFO", "Service is running");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try{
            alarmObject = (AlarmObject) ObjectSerializer.deserializeStringToAlarmObject(intent.getStringExtra("Object"));
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent receiverIntent = new Intent(this, AlarmReceiver.class);
        receiverIntent.putExtra("Title", alarmObject.getTitle());
        receiverIntent.putExtra("Id", alarmObject.getId());
        receiverIntent.putExtra("Ringtone", alarmObject.getRingtone());
        receiverIntent.putExtra("SnoozeInterval", alarmObject.getSnoozeInterval());

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
                    .setContentTitle(alarmObject.getTitle())
                    .setContentText("")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setFullScreenIntent(pendingIntent, true);


            Notification notification = notificationBuilder.build();

            startForeground(alarmObject.getId(), notification);
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
