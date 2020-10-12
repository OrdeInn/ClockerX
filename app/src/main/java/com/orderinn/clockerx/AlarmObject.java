package com.orderinn.clockerx;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

    public AlarmObject(Context context, long millis, String consTitle, int consId){
        time = Calendar.getInstance();
        time.setTimeInMillis(millis);
        title = consTitle;
        id = consId;
        active = true;

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

    public long getTime(){ return time.getTimeInMillis(); }
    public String getTitle(){ return title; }
    public int getId() { return id; }
    public boolean isActive() { return active; }


    public void setAlarm(Context context){
        Intent intent = new Intent(context, myBroadCastReceiver.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try{
            String serializedObject = ObjectSerializer.serializeObject(this);
            intent.putExtra("Object", serializedObject);
        }catch (Exception e){
            e.printStackTrace();
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 , intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,  time.getTimeInMillis(), pendingIntent);
        Log.i("ALARM INFO", "NEW ALARM SET");
    }

    public void unSetAlarm(Context context){
        context.stopService(new Intent(context, AlarmService.class));
        active = false;
        Log.i("ALARM INFO", "Alarm unactivated");
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
