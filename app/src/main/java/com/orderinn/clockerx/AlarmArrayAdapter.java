package com.orderinn.clockerx;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class AlarmArrayAdapter extends ArrayAdapter<AlarmObject> {

    private Context context;
    private static ArrayList<AlarmObject> alarmList;

    public AlarmArrayAdapter(@NonNull Context consContext, ArrayList<AlarmObject> list){
        super(consContext, 0, list);
        context = consContext;
        alarmList = list;

    }

    public void updateItems (ArrayList<AlarmObject> list) {
        alarmList.clear();
        this.alarmList.addAll(list);
        this.notifyDataSetChanged();
    }

    public static ArrayList<AlarmObject> getAlarmList() { return alarmList; }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final MyViewHolder myViewHolder;
        View listItem = convertView;
        final AlarmObject currentAlarm = alarmList.get(position);

        if(listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            myViewHolder = new MyViewHolder();
            myViewHolder.textView = (TextView)  listItem.findViewById(R.id.alarmTitle);
            myViewHolder.switchView = (Switch) listItem.findViewById(R.id.switchButton);

            myViewHolder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                    if(state) {
                        //Activate an alarm
                        currentAlarm.setAlarm(context);
                        Log.i("ALARM INFO", "Alarm is active");
                    }else{
                        //Cancel an alarm
                        currentAlarm.unSetAlarm(context);
                        Log.i("ALARM INFO", "Alarm is inactive");
                    }
                }
            });

            listItem.setTag(myViewHolder);

        }else {
            myViewHolder = (MyViewHolder) listItem.getTag();
        }

        if(currentAlarm.isActive()){
            myViewHolder.switchView.setChecked(true);
        }else{
            myViewHolder.switchView.setChecked(false);
        }




        long millis = currentAlarm.getTime();

        int minutes = (int) ((millis / (1000*60)) % 60);
        int hours   = (int) ((millis / (1000*60*60)) % 24);

        myViewHolder.textView.setText(currentAlarm.getTitle() + "\n" + String.format("%02d : %02d", hours, minutes));

        return listItem;
    }

    public static class MyViewHolder {
        static TextView textView;
        static Switch switchView;
    }
}
