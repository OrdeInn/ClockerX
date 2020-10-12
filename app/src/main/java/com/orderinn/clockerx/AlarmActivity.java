package com.orderinn.clockerx;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Switch;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    ListView alarmListView;
    AlarmArrayAdapter arrayAdapter;
    private ArrayList<AlarmObject> alarms;
    SharedPreferences sharedPreferences;
    int idCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //Create or open shared preferences calls "Alarms"
        sharedPreferences = this.getSharedPreferences("Alarms", Context.MODE_PRIVATE);

        //Fundamental initializations
        alarms = new ArrayList<>();
        alarmListView = findViewById(R.id.alarmListView);



        arrayAdapter = new AlarmArrayAdapter(this, alarms);
        alarmListView.setAdapter(arrayAdapter);

        getAlarmsFromPreferences();
        idCount = alarms.size();


    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayAdapter.updateItems(alarms);

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
            idCount++;
            long millis = data.getLongExtra("Time", 0);
            String title = data.getStringExtra("Title");

            AlarmObject newAlarm = new AlarmObject(this, millis, title, idCount);

            alarms.add(newAlarm);
            //AlarmObject.sortObjectArrayList(alarms);
            Log.i("ALARM INFO", String.valueOf(idCount));
            arrayAdapter.updateItems(alarms);


        }
    }




    //Gets alarms from shared preferences to call in onCreate method.
    private void getAlarmsFromPreferences(){
        String tmpString = sharedPreferences.getString("Alarms", "");
        try{
            alarms = (ArrayList<AlarmObject>) ObjectSerializer.deserializeStringToArrayList(tmpString);
            if(alarms.size() > 0){
                //AlarmObject.sortObjectArrayList(alarms);
                arrayAdapter.updateItems(alarms);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


    //Save to shared preferences final form of alarms before finish the activity.
    @Override
    protected void onDestroy() {
        super.onDestroy();

        alarms = AlarmArrayAdapter.getAlarmList();
        sharedPreferences.edit().putString("Alarms",ObjectSerializer.serializeObject(alarms)).apply();
    }
}
