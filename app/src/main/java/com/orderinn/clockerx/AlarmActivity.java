package com.orderinn.clockerx;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    SharedPreferences sharedPreferences;
    ObjectSerializer objectSerializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //Create or open shared preferences calls "Alarms"
        sharedPreferences = this.getSharedPreferences("Alarms", Context.MODE_PRIVATE);
        objectSerializer = new ObjectSerializer();

        //Fundamental initializations
        listView = findViewById(R.id.listView);
        alarms = new ArrayList<>();
        alarmList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, alarmList);

        getAlarmsFromPreferences();

        listView.setAdapter(arrayAdapter);

        //Long click listener to delete alarms in the list
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                alarms.remove(i);
                alarmList.remove(i);
                arrayAdapter.notifyDataSetChanged();

                return true;
            }
        });

    }

    //onClick Method
    public void addAnAlarm(View view){
        Intent intent = new Intent(getApplicationContext(), AddAnAlarmActivity.class);
        startActivityForResult(intent, 1);
    }

    //Gets alarm information that set previous activity and save it in shared preferences
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 1){
            long millis = data.getLongExtra("Time", 0);
            Calendar newAlarm = Calendar.getInstance();
            newAlarm.setTimeInMillis(millis);
            alarms.add(newAlarm);

            sortAlarms();

        }
    }

    private void sortAlarms(){
        //Insertion Sort function for arrays alarms and alarmList.

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

    //alarmList is String version of alarms to show in listview. Main implementations implemented alarms array and setAlarmList makes sync arrays.
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

    //Gets alarms from shared preferences to call in onCreate method.
    private void getAlarmsFromPreferences(){
        String tmpString = sharedPreferences.getString("Alarms", "");
        try{
            alarms = (ArrayList<Calendar>) objectSerializer.deserializeStringToArrayList(tmpString);
            if(alarms.size() > 0){
                sortAlarms();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //Save to shared preferences final form of alarms before finish the activity.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.edit().putString("Alarms",objectSerializer.serializeArrayList(alarms)).apply();
    }
}
