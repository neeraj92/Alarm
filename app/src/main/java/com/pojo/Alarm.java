package com.pojo;

import java.io.Serializable;

/**
 * Created by chikku on 14/11/14.
 */
public class Alarm implements Serializable, Comparable<Alarm> {
    private String name;
    private Time actualTime;
    private Time alarmTime;
    private boolean isEnabled;
    private ApplicationDetails launchAppDetails;
    private boolean vibrate;

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isVibrate() {

        return vibrate;
    }

    public Alarm() {
        this.isEnabled = false;
        this.actualTime = new Time();
        this.alarmTime = new Time();
        this.launchAppDetails = new ApplicationDetails();
    }

    public ApplicationDetails getLaunchAppDetails() {
        return launchAppDetails;
    }

    public void setLaunchAppDetails(ApplicationDetails launchAppDetails) {
        this.launchAppDetails = launchAppDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHour() {
        return this.actualTime.getHour();
    }

    public void setHour(int hour) {
        this.actualTime.setHour(hour);
    }

    public int getMinutes() {
        return this.actualTime.getMinute();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getAlarmTimeHour() {
        return this.alarmTime.getHour();
    }

    public int getAlarmTimeMinute() {
        return this.alarmTime.getMinute();
    }

    public void setAlarmTimeHour(int hour) {
        this.alarmTime.setHour(hour);
    }

    public void setAlarmMinute(int minute) {
        this.alarmTime.setMinute(minute);
    }

    public void setMinutes(int minutes) {
        this.actualTime.setMinute(minutes);
    }

    @Override
    public int compareTo(Alarm alarm) {
        if (this.getAlarmTimeHour() > alarm.getAlarmTimeHour()) {
            return 1;
        } else if (this.getAlarmTimeHour() == alarm.getAlarmTimeHour()) {
            if (this.getAlarmTimeMinute() > alarm.getAlarmTimeMinute()) {
                return 1;
            } else if (this.getAlarmTimeMinute() == alarm.getAlarmTimeMinute()) {
                return this.getName().compareTo(alarm.getName());
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }


    public String getTime() {
        String time = "";
        String timeOfTheDay = "Am";
        int hour = 0;
        if (this.getAlarmTimeHour() > 12) {
            timeOfTheDay = "Pm";
            hour = this.getAlarmTimeHour() % 12;
        } else if (this.getAlarmTimeHour() == 0) {
            hour = 12;
        } else {
            hour = this.getAlarmTimeHour();
        }

        if (hour < 10) {
            time += "0" + hour;
        } else {
            time += hour;
        }
        time += ":";

        if (this.getAlarmTimeMinute() < 10) {
            time += "0" + this.getAlarmTimeMinute();
        } else {
            time += this.getAlarmTimeMinute();
        }

        time += " " + timeOfTheDay;

        return time;
    }

    public void resetAlarmTime() {
        this.alarmTime.setHour(this.getHour());
        this.alarmTime.setMinute(this.getMinutes());
    }

    public void addToAlarmTime(int minutes) {
        this.alarmTime.addMinutes(minutes);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "name='" + name + '\'' +
                ", hour=" + this.getHour() +
                ", minutes=" + this.getMinutes() +
                '}';
    }
}
