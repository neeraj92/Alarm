package com.example.chikku.alarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.chikku.utils.AlarmsHandler;

public class AlarmsReceiver extends BroadcastReceiver {
    public static String ALARM_NAME = "com.example.chikku.alarm.AlarmsReceiver";
    public AlarmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String alarmName = intent.getStringExtra(AlarmsHandler.ALARM_NAME);
        Intent alarmIntent = new Intent (context , AlarmActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra(AlarmsReceiver.ALARM_NAME , alarmName);
        Log.d("Alarm_Receiver" , "Received broadcast:" + alarmName);
        context.startActivity(alarmIntent);

    }
}
