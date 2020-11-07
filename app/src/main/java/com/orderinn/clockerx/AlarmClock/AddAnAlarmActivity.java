package com.orderinn.clockerx.AlarmClock;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import com.orderinn.clockerx.ArrayAdapters.AlertDialogArrayAdapter;
import com.orderinn.clockerx.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AddAnAlarmActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    TimePicker timePicker;
    EditText alarmTitle;
    Calendar newAlarm;
    TextView ringtoneText;
    TextView snoozeInterval;

    String chosenRingtone;
    String ringtoneTitle;
    MediaPlayer mediaPlayer;
    int alarmId;

    ArrayList<String> ringtoneEntries;
    ArrayList<String> ringtoneUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_an_alarm);


        alarmTitle = findViewById(R.id.alarmTitle);
        ringtoneText = findViewById(R.id.ringtoneText);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        snoozeInterval = findViewById(R.id.snoozeInterval);
        newAlarm = Calendar.getInstance();

        ringtoneEntries = new ArrayList<>();
        ringtoneUris = new ArrayList<>();


        RingtoneManager ringtoneManager = new RingtoneManager(this);
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor alarmCursor = ringtoneManager.getCursor();

        while(!alarmCursor.isAfterLast() && alarmCursor.moveToNext()) {
            ringtoneEntries.add(alarmCursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            ringtoneUris.add(alarmCursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + alarmCursor.getString(RingtoneManager.ID_COLUMN_INDEX));
        }






        Intent intent = getIntent();
        int requestCode = intent.getIntExtra("RequestCode", -1);

        if(requestCode == 1){

            //If Activity start to create a new alarm

            //Basic initializations
            chosenRingtone = "";
            ringtoneTitle = "";
            ringtoneText.setHint("Choose a Ringtone");
            snoozeInterval.setHint("Snooze Interval");
            alarmId = -1;
        }
        else if(requestCode == 2){
            //If activity start to update an already exist alarm
            long millis = intent.getLongExtra("Time", 0);
            int minutes = (int) ((millis / (1000*60)) % 60);
            int hours   = (int) ((millis / (1000*60*60)) % 24);
            alarmId = intent.getIntExtra("AlarmId", -1);

            newAlarm.setTimeInMillis(millis);
            timePicker.setHour(hours);
            timePicker.setMinute(minutes);
            alarmTitle.setText(intent.getStringExtra("Title"));
            ringtoneTitle = intent.getStringExtra("RingtoneTitle");
            snoozeInterval.setText(String.valueOf(intent.getIntExtra("SnoozeInterval", 5)));
            ringtoneText.setText(ringtoneTitle);
        }

    }

    public void newAlarmButton(View view){
        int chosenSnoozeInterval;
        //Guarantee that some variables does not contain empty values

        if(snoozeInterval.getText().equals("")){
            chosenSnoozeInterval = 5;
        }else{
            chosenSnoozeInterval = Integer.parseInt((String)snoozeInterval.getText());
        }
        if(ringtoneTitle.equals("")){
            chosenRingtone = ringtoneUris.get(0);
            ringtoneTitle = ringtoneEntries.get(0);
        }

        newAlarm.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        newAlarm.set(Calendar.MINUTE, timePicker.getMinute());


        //Puts a new alarm to the intent
        Intent returnIntent = new Intent();

        returnIntent.putExtra("Time", newAlarm.getTimeInMillis());
        returnIntent.putExtra("Title", alarmTitle.getText().toString());
        returnIntent.putExtra("SnoozeInterval", chosenSnoozeInterval);
        returnIntent.putExtra("Ringtone", chosenRingtone);
        returnIntent.putExtra("RingtoneTitle", ringtoneTitle);

        /*
        If this activity is started to create a new alarm, in this level alarmId parameter is -1 and actual id of alarm will be created in
        AlarmActivity on activity result.

        If it is started to update an alarm, alarm that will be update is sent with its own id so it must be bigger than -1.
        */

        if(alarmId > -1 ){ returnIntent.putExtra("AlarmId", alarmId ); }
        setResult(RESULT_OK, returnIntent);

        finish();
    }


    //Creates an alert dialog to select a ringtone
    public void selectRingtone(View view) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);

        View ringtoneView = getLayoutInflater().inflate(R.layout.popup_layout, null);
        ListView ringtoneListView = ringtoneView.findViewById(R.id.listView);

        AlertDialogArrayAdapter ringtoneArrayAdapter = new AlertDialogArrayAdapter(this, ringtoneEntries);
        ringtoneListView.setAdapter(ringtoneArrayAdapter);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(ringtoneView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();


        ringtoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    mediaPlayer.reset();
                    chosenRingtone = ringtoneUris.get(i);
                    ringtoneTitle = ringtoneEntries.get(i);
                    mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(chosenRingtone));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        //Set the last selected ringtone for chosenRingtone
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mediaPlayer.release();
                if(ringtoneTitle.equals("")){
                    chosenRingtone = ringtoneUris.get(0);
                    ringtoneTitle = ringtoneEntries.get(0);
                }

                ringtoneText.setText(ringtoneTitle);

            }
        });

    }

    //Method for media player
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }


    //Creates an alert dialog to select a ringtone
    public void selectSnoozeType(View view) {

        final ArrayList<String> snoozeArray = new ArrayList<>();
        for(int i=1; i<=6; i++){
            snoozeArray.add(String.valueOf(i*5));
        }

        View snoozeView = getLayoutInflater().inflate(R.layout.popup_layout, null);
        ListView snoozeListView = snoozeView.findViewById(R.id.listView);


        AlertDialogArrayAdapter snoozeArrayAdapter = new AlertDialogArrayAdapter(this, snoozeArray);
        snoozeListView.setAdapter(snoozeArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(snoozeView);
        final AlertDialog dialog = builder.create();
        dialog.show();


        snoozeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                snoozeInterval.setText(snoozeArray.get(i));
                dialog.cancel();
            }
        });


    }





}