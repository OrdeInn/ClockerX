package com.orderinn.clockerx.AlarmClock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmObject implements Serializable {

    private Calendar time;
    private String title;
    private int id;
    private boolean active;
    private String ringtone;
    private String ringtoneTitle;
    private int snoozeInterval;

    public AlarmObject(Context context, long millis, String consTitle, int consId, String consRingtone, String consRingtoneTitle, int consSnoozeInterval){
        time = Calendar.getInstance();
        time.setTimeInMillis(millis);
        title = consTitle;
        id = consId;
        active = false;
        ringtone = consRingtone;
        ringtoneTitle = consRingtoneTitle;
        snoozeInterval = consSnoozeInterval;
    }

    public static void sortObjectArrayList(ArrayList<AlarmObject> arrayList){
        //Insertion Sort function for arrays alarms and alarmList.

        for(int j=1; j < arrayList.size(); j++){

            int i = j-1;
            AlarmObject key = arrayList.get(j);
            //Shifts the greater values to the right
            while( i>-1 &&arrayList.get(i).compareTo(key) > 0){
                arrayList.set(i+1, arrayList.get(i));
                i--;
            }
            arrayList.set(i+1, key);
        }
    }

    public int compareTo(AlarmObject other){
        long firstAlarm = getTime();
        long otherAlarm = other.getTime();

        if(firstAlarm == otherAlarm){
            return 0;
        }else if(firstAlarm > otherAlarm){
            return 1;
        }else{
            return -1;
        }
    }

    //GET Methods
    public long getTime(){ return time.getTimeInMillis(); }
    public String getTitle(){ return title; }
    public int getId() { return id; }
    public boolean isActive() { return active; }
    public String getRingtone(){return ringtone; }
    public String getRingtoneTitle() { return ringtoneTitle; }
    public int getSnoozeInterval() { return snoozeInterval; }

    //SET Methods
    public void setTime(long millis){ time.setTimeInMillis(millis); }
    public void setTitle(String title){ this.title = title; }
    public void setRingtone(String ringtone){ this.ringtone = ringtone; }
    public void setRingtoneTitle(String ringtoneTitle){ this.ringtoneTitle = ringtoneTitle; }
    public void setSnoozeInterval(int snoozeInterval) { this.snoozeInterval = snoozeInterval; }


    public void setAlarm(Context context){

        if(!active){
            active = true;
            Intent intent = new Intent(context, myBroadCastReceiver.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try{
                String serializedObject = ObjectSerializer.serializeObject(this);
                intent.putExtra("Object", serializedObject);
            }catch (Exception e){
                e.printStackTrace();
            }

            //If alarm set later than now, set the alarm to tomorrow
            Calendar currentTime = Calendar.getInstance();
            currentTime.setTime(Calendar.getInstance().getTime());

            if(currentTime.getTimeInMillis() >= time.getTimeInMillis()){
                time.set(Calendar.DAY_OF_WEEK_IN_MONTH, currentTime.get(Calendar.DAY_OF_WEEK_IN_MONTH) + 1 );
            }

            Log.i("CURRENT TIME", currentTime.getTime().toString());
            Log.i("ALARM TIME", time.getTime().toString());

            //Finding difference in hours and minutes of current time and alarm
            int millisecondDifference = (int)(time.getTimeInMillis() - currentTime.getTimeInMillis());
            int minutesDifference = (millisecondDifference / (1000*60)) % 60 ;
            int hoursDifference   = (millisecondDifference / (1000*60*60)) % 24 ;

            Toast.makeText(context, String.valueOf(hoursDifference) + " hours and " + String.valueOf(minutesDifference) + " minutes", Toast.LENGTH_SHORT).show();

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 , intent,  PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,  time.getTimeInMillis(), pendingIntent);
        }
    }





    public void unSetAlarm(Context context){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, myBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        context.stopService(new Intent(context, AlarmService.class));
        active = false;
    }



    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {
        aInputStream.defaultReadObject();
        time = (Calendar) aInputStream.readObject();
        title = aInputStream.readUTF();
        active = aInputStream.readBoolean();
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        aOutputStream.defaultWriteObject();
        aOutputStream.writeObject(time);
        aOutputStream.writeUTF(title);
        aOutputStream.writeBoolean(active);
    }

}
