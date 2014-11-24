package com.chikku.utils;

import android.util.Log;

import com.pojo.Alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by chikku on 14/11/14.
 */
public class AlarmManager {
    private ArrayList<Alarm> alarms;
    private static AlarmManager instance;

    private AlarmManager() {
        this.alarms = new ArrayList<Alarm>();
    }

    public static AlarmManager getInstance() {
        if (instance == null) {
            instance = new AlarmManager();
            AlarmXmlManager.loadAlarmManager();
        }
        return instance;
    }

    public int getNumberOfAlarms() {
        return alarms.size();
    }

    public Alarm getAlarm(int index) {
        return alarms.get(index);
    }

    public void setAlarm(int index, Alarm alarm) {
        alarms.add(index, alarm);
    }

    public void addAlarm(Alarm alarm) {
        this.alarms.add(alarm);
    }

    public void normalize() {
        Collections.sort(this.alarms);
    }

    public Alarm getClosestAlarm() {
        int smallestIndex = -1;
        long smallestDifference = Integer.MAX_VALUE;
        long time = 0;
        long systemTime = System.currentTimeMillis();
        for (int i = 0; i < this.alarms.size(); i++) {

            if (!this.alarms.get(i).isEnabled()) {
                continue;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, this.alarms.get(i).getAlarmTimeHour());
            calendar.set(Calendar.MINUTE, this.alarms.get(i).getAlarmTimeMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            time = calendar.getTimeInMillis();
            if (time - systemTime > 0 && time - systemTime < smallestDifference) {
                smallestDifference = time - systemTime;
                smallestIndex = i;
                Log.d("TimeDebug", "AlarmName:" + this.alarms.get(i).getName() + "|CurrentTime:" + System.currentTimeMillis() + "|Alarm time:" + time);
            }
        }
        if (smallestIndex < 0) {
            return null;
        }
        return this.alarms.get(smallestIndex);
    }

    public void removeAlarm(int position) {
        this.alarms.remove(position);
    }

    public Alarm getFirstEnabledAlarm() {
        this.normalize();
        for (int i = 0; i < this.alarms.size(); i++) {
            if (this.alarms.get(i).isEnabled()) {
                return this.alarms.get(i);
            }
        }
        return null;
    }

    public Alarm getAlarmByName(String name) {
        for (int i = 0; i < getNumberOfAlarms(); i++) {
            if (this.alarms.get(i).getName().equals(name)) {
                return this.alarms.get(i);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "AlarmManager{" +
                "alarms=" + alarms +
                '}';
    }
}
