package com.orderinn.clockerx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orderinn.clockerx.AlarmClock.AlarmActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startAlarmActivity(View view){

        Intent intent = new Intent(this.getApplicationContext() , AlarmActivity.class);
        startActivity(intent);

    }

    public void startTimerActivity(View view){

        Intent intent = new Intent(this.getApplicationContext() ,TimerActivty.class);
        startActivity(intent);

    }

    public void startWorldClockActivity(View view){

        Intent intent = new Intent(this.getApplicationContext() ,WorldClockActivty.class);
        startActivity(intent);

    }

    public void startEggTimerActivity(View view){

        Intent intent = new Intent(this.getApplicationContext() ,EggTimerActivity.class);
        startActivity(intent);

    }
}