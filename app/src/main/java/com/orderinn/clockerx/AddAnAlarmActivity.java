package com.orderinn.clockerx;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddAnAlarmActivity extends AppCompatActivity {

    TimePicker timePicker;
    TextView timeText;
    EditText alarmTitle;
    Calendar newAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_an_alarm);

        newAlarm = Calendar.getInstance();

        alarmTitle = findViewById(R.id.alarmTitle);
        timeText = findViewById(R.id.timeText);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                timeText.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
                newAlarm.set(Calendar.HOUR_OF_DAY, hour);
                newAlarm.set(Calendar.MINUTE, minute);
                newAlarm.set(Calendar.SECOND, 0);
            }
        });

    }

    public void addAlarm(View view){
        //Put a new alarm to the intent

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Time", newAlarm.getTimeInMillis());
        setResult(RESULT_OK, returnIntent);
        finish();
    }


    public void setAlarm(Calendar cal){
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.BroadCastReceive.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Title", alarmTitle.getText());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0 , intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,  cal.getTimeInMillis(), pendingIntent);
        Log.i("ALARMINFO", "NEW ALARM SET");

    }



}