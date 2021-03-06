package com.orderinn.clockerx.AlarmClock;


import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.TextView;

import com.orderinn.clockerx.ArrayAdapters.AlarmArrayAdapter;
import com.orderinn.clockerx.R;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends Activity {


    PowerManager.WakeLock wakeLock;
    TextView alarmTitle;
    TextView timeText;
    MediaPlayer mediaPlayer;
    String ringtone;
    int alarmId;
    int snoozeInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_receiver);

        Intent intent = getIntent();

        //Prints title of alarm that user set.
        alarmTitle = findViewById(R.id.titleText);
        alarmTitle.setText(intent.getStringExtra("Title"));

        alarmId = intent.getIntExtra("Id", 0);
        ringtone = intent.getStringExtra("Ringtone");
        snoozeInterval = intent.getIntExtra("SnoozeInterval", -1);

        //Print current time in pattern HH:mm
        timeText  = findViewById(R.id.timeText);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        timeText.setText(simpleDateFormat.format(currentTime).toString());



        //Wakes screen up
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();


        mediaPlayer = MediaPlayer.create(this, Uri.parse(ringtone));
        mediaPlayer.start();

    }

    public void snoozeAlarm(View view){

        stopService(new Intent(AlarmReceiver.this, AlarmService.class));
        mediaPlayer.stop();

        AlarmObject alarm =  AlarmArrayAdapter.getAlarmList().get(alarmId-1);
        alarm.unSetAlarm(this);

        if(snoozeInterval == -1){
            alarm.setTime(alarm.getTime() + 5 * (60000));
        }else{
            alarm.setTime(alarm.getTime() + snoozeInterval * (60000));
        }

        alarm.setAlarm(this);

        finish();
    }

    public void cancelAlarm(View view) {

        stopService(new Intent(AlarmReceiver.this, AlarmService.class));
        mediaPlayer.stop();
        AlarmArrayAdapter.getAlarmList().get(alarmId-1).unSetAlarm(this);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }
}