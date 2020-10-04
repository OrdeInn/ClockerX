package com.orderinn.clockerx;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
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

    public void newAlarm(View view){
        //Put a new alarm to the intent

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Time", newAlarm.getTimeInMillis());
        returnIntent.putExtra("Title", alarmTitle.getText().toString());
        setResult(RESULT_OK, returnIntent);

        finish();
    }




    


}