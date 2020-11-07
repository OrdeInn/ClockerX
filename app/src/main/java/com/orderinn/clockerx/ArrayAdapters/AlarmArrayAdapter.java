package com.orderinn.clockerx.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orderinn.clockerx.AlarmClock.AlarmObject;
import com.orderinn.clockerx.R;

import java.util.ArrayList;

public class AlarmArrayAdapter extends ArrayAdapter<AlarmObject> {

    private Context context;
    private static ArrayList<AlarmObject> alarmList;

    public AlarmArrayAdapter(@NonNull Context consContext, ArrayList<AlarmObject> list){
        super(consContext, android.R.layout.simple_list_item_1, list);
        context = consContext;
        alarmList = list;

    }

    public void updateItems (ArrayList<AlarmObject> list) {
        alarmList.clear();
        AlarmObject.sortObjectArrayList(list);
        alarmList.addAll(list);
        notifyDataSetChanged();
    }

    public static ArrayList<AlarmObject> getAlarmList() { return alarmList; }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        final MyViewHolder myViewHolder;
        final AlarmObject currentAlarm = alarmList.get(position);


        listItem = LayoutInflater.from(context).inflate(R.layout.complex_list_item, parent, false);
        if(listItem.getTag() == null){
            myViewHolder = new MyViewHolder();
            myViewHolder.textView = (TextView)  listItem.findViewById(R.id.alarmTitle);
            myViewHolder.switchView = (Switch) listItem.findViewById(R.id.switchButton);
        }else {
            myViewHolder = (MyViewHolder) listItem.getTag();
        }


        myViewHolder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                if(state) {
                    //Activate an alarm
                    if(!currentAlarm.isActive()) {currentAlarm.setAlarm(context); }
                }else{
                   //Cancel an alarm
                   currentAlarm.unSetAlarm(context);
                }
             }
        });

        if(currentAlarm.isActive()){
            myViewHolder.switchView.setChecked(true);
        }else{
            myViewHolder.switchView.setChecked(false);
        }

        long millis = currentAlarm.getTime();

        int minutes = (int) ((millis / (1000*60)) % 60);
        int hours   = (int) ((millis / (1000*60*60)) % 24);

        myViewHolder.textView.setText(currentAlarm.getTitle() + "\n" + String.format("%02d : %02d", hours, minutes));

        listItem.setTag(myViewHolder);

        return listItem;
    }


    public static class MyViewHolder {
        static TextView textView;
        static Switch switchView;
    }


}
