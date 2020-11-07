package com.orderinn.clockerx.AlarmClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.orderinn.clockerx.AlarmClock.AlarmService;

public class myBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALARM INFO", "ALARM  IS RECEIVED");

        Intent intentToService = new Intent(context, AlarmService.class);
        intentToService.putExtra("Object", intent.getStringExtra("Object"));
        context.startForegroundService(intentToService);
    }

}
