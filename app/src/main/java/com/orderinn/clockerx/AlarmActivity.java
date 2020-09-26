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

import java.sql.Time;
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
        listView.setAdapter(arrayAdapter);
    }

    //onClick Method
    public void addAnAlarm(View view){
        Intent intent = new Intent(getApplicationContext(), AddAnAlarmActivity.class);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Get alarm information
        if(resultCode == RESULT_OK && requestCode == 1){
            long millis = data.getLongExtra("Time", 0);
            Calendar newAlarm = Calendar.getInstance();
            newAlarm.setTimeInMillis(millis);
            alarms.add(newAlarm);
            sortAlarms();

        }
    }

    private void sortAlarms(){
        //Insertion Sort alarms and alarmList

       for(int j=1; j < alarms.size(); j++){

           int i = j-1;
           Calendar key = alarms.get(j);
           //Shifts the greater values to the right
           while( i>-1 &&alarms.get(i).compareTo(key) > 0){
               alarms.set(i+1, alarms.get(i));
               i--;
           }
           alarms.set(i+1, key);
       }
        setAlarmList();
    }


    private void setAlarmList(){
        alarmList.clear();
        for(Calendar time: alarms){
            long millis = time.getTimeInMillis();
            int minutes = (int) ((millis / (1000*60)) % 60);
            int hours   = (int) ((millis / (1000*60*60)) % 24);

            alarmList.add(String.format("%02d : %02d", hours, minutes) );
        }
        arrayAdapter.notifyDataSetChanged();
    }
}