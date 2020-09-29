package com.orderinn.clockerx;

import android.content.Context;
import androidx.appcompat.widget.SwitchCompat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmObject implements Serializable {

    private Calendar time;
    private String title;
    public AlarmObject(Context context, long millis, String consTitle){
        time = Calendar.getInstance();
        time.setTimeInMillis(millis);
        title = consTitle;


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




    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {
        aInputStream.defaultReadObject();
        time = (Calendar) aInputStream.readObject();
        title = aInputStream.readUTF();
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        aOutputStream.defaultWriteObject();
        aOutputStream.writeObject(time);
        aOutputStream.writeUTF(title);
    }

}
