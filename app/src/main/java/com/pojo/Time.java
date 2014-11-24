package com.pojo;

/**
 * Created by chikku on 20/11/14.
 */
public class Time {
    private int hour;
    private int minute;

    public Time() {
        this.hour = 0;
        this.minute = 0;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void addHour(int hour) {
        this.hour += hour;
        this.hour = this.hour % 24;
    }

    public void addMinutes(int minutes) {
        this.hour = this.hour + (int) minutes / 60;
        this.minute = this.minute + minutes % 60;
    }
}
