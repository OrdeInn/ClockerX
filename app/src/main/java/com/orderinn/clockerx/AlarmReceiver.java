package com.orderinn.clockerx;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AlarmReceiver extends Activity {


    PowerManager.WakeLock wakeLock;
    AlarmObject alarmObject;
    TextView alarmTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_receiver);

        Intent intent = getIntent();
        String serializedObject = intent.getStringExtra("Object");

        try{
            alarmObject = (AlarmObject) ObjectSerializer.deserializeStringToAlarmObject(serializedObject);
        }catch(Exception e ){
            e.printStackTrace();
        }

        alarmTitle = (TextView) findViewById(R.id.textView);
        alarmTitle.setText(alarmObject.getTitle());
        //alarmTitle.setText(alarmObject.getTitle());
        //alarmTitle.setText("asrgt");



        Log.i("ALARMINFO", "AlarmReceiver activity is activated");

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();

    }

    public void cancelAlarm(View view) {

        stopService(new Intent(AlarmReceiver.this, AlarmService.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }
}