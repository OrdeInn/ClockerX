package com.orderinn.clockerx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class myBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALARMINFO", "ALARM  IS RECEIVED");

        Intent intentToService = new Intent(context, AlarmService.class);
        intentToService.putExtra("Id", intent.getIntExtra("Id", 0));
        intentToService.putExtra("Object", intent.getStringExtra("Object"));
        context.startForegroundService(intentToService);
    }

}
