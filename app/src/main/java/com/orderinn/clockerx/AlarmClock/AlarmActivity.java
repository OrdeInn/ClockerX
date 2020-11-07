package com.orderinn.clockerx.AlarmClock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orderinn.clockerx.ArrayAdapters.AlarmArrayAdapter;
import com.orderinn.clockerx.R;

import java.util.ArrayList;

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




        //When list item clicked, directs to AddAnAlarmActivity to update an selected one.
        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), AddAnAlarmActivity.class);
                AlarmObject alarmToUpdate = alarms.get(i);
                intent.putExtra("Title", alarmToUpdate.getTitle());
                intent.putExtra("Time", alarmToUpdate.getTime());
                intent.putExtra("RingtoneTitle", alarmToUpdate.getRingtoneTitle());
                intent.putExtra("AlarmId", alarmToUpdate.getId());
                intent.putExtra("SnoozeInterval", alarmToUpdate.getSnoozeInterval());

                intent.putExtra("RequestCode", 2);
                startActivityForResult(intent, 2);
            }
        });

        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                new AlertDialog.Builder(AlarmActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Remove An Alarm")
                        .setMessage("Are you sure to delete this alarm?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              deleteAnAlarm(position);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayAdapter.updateItems(alarms);

    }

    //onClick Method
    public void addAnAlarm(View view){

        Intent intent = new Intent(getApplicationContext(), AddAnAlarmActivity.class);
        intent.putExtra("RequestCode", 1);
        startActivityForResult(intent, 1);
    }



    //Gets alarm information that set previous activity and save it in shared preferences
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            long millis = data.getLongExtra("Time", 0);
            String title = data.getStringExtra("Title");
            String ringtone = data.getStringExtra("Ringtone");
            String ringtoneTitle = data.getStringExtra("RingtoneTitle");
            int snoozeInterval = data.getIntExtra("SnoozeInterval",-1);

            if(requestCode == 1){
                idCount++;
                AlarmObject newAlarm = new AlarmObject(this, millis, title, idCount, ringtone, ringtoneTitle, snoozeInterval);

                alarms.add(newAlarm);
                AlarmObject.sortObjectArrayList(alarms);
                newAlarm.setAlarm(this);
                Log.i("ALARM INFO", "Alarm Created " + String.valueOf(idCount));

            }else if(requestCode == 2){
                //Getting id of updated alarm
                int updatedAlarmId = data.getIntExtra("AlarmId", -1);

                for(AlarmObject alarm : alarms){
                    if(alarm.getId() == updatedAlarmId){
                        alarm.setTime(millis);
                        alarm.setTitle(title);
                        alarm.setRingtone(ringtone);
                        alarm.setRingtoneTitle(ringtoneTitle);
                        alarm.setSnoozeInterval(snoozeInterval);
                        alarm.setAlarm(this);
                        Log.i("ALARM INFO", "Alarm Updated " + String.valueOf(idCount));
                    }
                }
            }
        }
        arrayAdapter.updateItems(alarms);
    }

    private void deleteAnAlarm(int position) {

        AlarmObject alarmToDelete = alarms.get(position);
        if(alarmToDelete.isActive()){
            alarmToDelete.unSetAlarm(this);
        }
        alarms.remove(alarmToDelete);
        arrayAdapter.updateItems(alarms);

    }




    //Gets alarms from shared preferences to call in onCreate method.
    private void getAlarmsFromPreferences(){
        String tmpString = sharedPreferences.getString("Alarms", "");
        try{
            alarms = (ArrayList<AlarmObject>) ObjectSerializer.deserializeStringToArrayList(tmpString);
            if(alarms.size() > 0){
                AlarmObject.sortObjectArrayList(alarms);
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

        alarms = arrayAdapter.getAlarmList();
        sharedPreferences.edit().putString("Alarms",ObjectSerializer.serializeObject(alarms)).apply();
    }
}
