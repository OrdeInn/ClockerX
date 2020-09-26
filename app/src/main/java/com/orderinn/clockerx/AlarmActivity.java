package com.orderinn.clockerx;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Calendar> alarms;
    ArrayList<String> alarmList;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        listView = findViewById(R.id.listView);
        alarms = new ArrayList<>();
        alarmList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, alarmList);


        //alarms.add(newAlarm);
        alarmList.add("7:40");
        listView.setAdapter(arrayAdapter);
    }

    public void addAnAlarm(View view){
        Intent intent = new Intent(getApplicationContext(), AddAnAlarmActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Get alarm information
    }

}