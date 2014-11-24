package com.chikku.utils;

import android.app.*;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.chikku.alarm.AlarmsReceiver;
import com.pojo.Alarm;

import java.util.Calendar;

/**
 * Created by chikku on 18/11/14.
 */
public class AlarmsHandler {

    public static final String ALARM_NAME = "com.chikku.utils.AlarmsHandler";
    private static String currentAlarmName = "";

    public static void cleanUpAfterDelete(String alarmName, Context context) {
        if (alarmName == currentAlarmName) {
            Log.d("Alarmshandler", "Cleaning up after alarm delete");
            cancelAlarm(alarmName, context);
            setAlarm(context);

        }
    }

    private static long getTimeInMillisecondForAlarm(Alarm alarm) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getAlarmTimeHour());
        calendar.set(Calendar.MINUTE, alarm.getAlarmTimeMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();

    }

    public static void setAlarm(Context context) {
        com.chikku.utils.AlarmManager alarmManager = com.chikku.utils.AlarmManager.getInstance();
        Alarm currentAlarm = alarmManager.getClosestAlarm();

        if (currentAlarm == null) {
            Log.d("Alarmshandler", "No more alarms for the day");
            Alarm firstAlarm = alarmManager.getFirstEnabledAlarm();

            if (!currentAlarmName.equals("")) {
                cancelAlarm(currentAlarmName, context);
            }
            if (!(firstAlarm == null)) {
                Log.d("Alarmshandler", "Setting alarm for tomorrow");
                long alarmTime = getTimeInMillisecondForAlarm(firstAlarm) + DateUtils.DAY_IN_MILLIS;
                setupAlarm(firstAlarm.getName(), alarmTime, context);
            }

        }
// else if (isFirst)
//        {
//            Log.d("Alarmshandler", "First Alarm");
//            long currentAlarmTime = getTimeInMillisecondForAlarm(currentAlarm);
//
//            if (currentAlarmTime > System.currentTimeMillis()) {
//                setupAlarm(currentAlarm.getName(), currentAlarmTime, context);
//
//            }
//
//        }
        else

        {
            cancelAlarm(currentAlarmName, context);
            long currentAlarmTime = getTimeInMillisecondForAlarm(currentAlarm);
            if (currentAlarmTime > System.currentTimeMillis()) {
                setupAlarm(currentAlarm.getName(), currentAlarmTime, context);
            }
        }

    }

    private static void setupAlarm(String name, long time, Context context) {
        Log.d("Alarmshandler", "New alarm set:" + name);

        Intent toSetupIntent = new Intent(context, AlarmsReceiver.class);
        toSetupIntent.removeExtra(AlarmsHandler.ALARM_NAME);
        toSetupIntent.putExtra(AlarmsHandler.ALARM_NAME, name);

        PendingIntent setupIntent = PendingIntent.getBroadcast(context, 0, toSetupIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, time, setupIntent);

        currentAlarmName = name;

    }

    private static void cancelAlarm(String name, Context context) {
        if (name.equals("")) {
            Log.d("Alarmshandler", "No name for the alarm to be cancelled");
        }
        Log.d("Alarmshandler", "Cancelling alarm:" + name);
        Intent toCancelIntent = new Intent(context, AlarmsReceiver.class);
        toCancelIntent.putExtra(AlarmsHandler.ALARM_NAME, name);
        PendingIntent cancelIndent = PendingIntent.getBroadcast(context, 0, toCancelIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(cancelIndent);
        currentAlarmName = "";

    }
}
