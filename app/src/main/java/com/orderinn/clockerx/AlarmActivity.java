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
import android.widget.GridLayout;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    GridLayout alarmListGridLayout;
    private ArrayList<AlarmObject> alarms;
    private ArrayList<SwitchCompat> alarmViews;
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
        alarmViews = new ArrayList<>();
        alarmListGridLayout = (GridLayout) findViewById(R.id.alarmListGridLayout);

        getAlarmsFromPreferences();
        idCount = alarms.size();

    }

    //onClick Method
    public void addAnAlarm(View view){
        Intent intent = new Intent(getApplicationContext(), AddAnAlarmActivity.class);
        startActivityForResult(intent, 1);
    }

    public void setAlarm(AlarmObject obj,long millis){
        Intent intent = new Intent(this, myBroadCastReceiver.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try{
            intent.putExtra("Object", ObjectSerializer.serializeArrayList(obj));//Change the method name if it works.
            intent.putExtra("Id", obj.getId());
        }catch (Exception e){
            e.printStackTrace();
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 , intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,  millis, pendingIntent);
        Log.i("ALARMINFO", "NEW ALARM SET");
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
            Log.i("ALARMINFO", String.valueOf(idCount));

            alarms.add(newAlarm);
            AlarmObject.sortObjectArrayList(alarms);
            syncAlarmLists();
            setAlarm(newAlarm, millis);
        }
    }


    private void syncAlarmLists() {
        alarmViews.clear();
        alarmListGridLayout.removeAllViews();
        for(AlarmObject alarm : alarms) {
            SwitchCompat switchCompat = createSwitchCompatObject(alarm.getTitle(), alarm.getTime());
            alarmViews.add(switchCompat);
        }
        displayAlarmListViews();

    }



    private SwitchCompat createSwitchCompatObject(String title, long millis){

        SwitchCompat switchCompat = new SwitchCompat(this);
        switchCompat.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        switchCompat.setThumbResource(R.drawable.thumb);
        switchCompat.setTrackResource(R.drawable.track);
        int minutes = (int) ((millis / (1000*60)) % 60);
        int hours   = (int) ((millis / (1000*60*60)) % 24);
        switchCompat.setText(title + "\n" + String.format("%02d : %02d", hours, minutes));
        switchCompat.setTextSize(35);
        switchCompat.setPadding(0, 0, 20, 0);

        return switchCompat ;
    }



    //Gets alarms from shared preferences to call in onCreate method.
    private void getAlarmsFromPreferences(){
        String tmpString = sharedPreferences.getString("Alarms", "");
        try{
            alarms = (ArrayList<AlarmObject>) ObjectSerializer.deserializeStringToArrayList(tmpString);
            if(alarms.size() > 0){
                AlarmObject.sortObjectArrayList(alarms);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        syncAlarmLists();
    }


    //Display all alarms in GridLayout as a SwitchCompat
    private void displayAlarmListViews(){
        for(SwitchCompat switchCompat : alarmViews) {
            alarmListGridLayout.addView(switchCompat);
        }
    }



    //Save to shared preferences final form of alarms before finish the activity.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.edit().putString("Alarms",ObjectSerializer.serializeArrayList(alarms)).apply();
    }
}
