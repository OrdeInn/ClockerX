package com.orderinn.clockerx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class myBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALARM INFO", "ALARM  IS RECEIVED");

        Intent intentToService = new Intent(context, AlarmService.class);
        intentToService.putExtra("Object", intent.getStringExtra("Object"));
        context.startForegroundService(intentToService);
    }

}
