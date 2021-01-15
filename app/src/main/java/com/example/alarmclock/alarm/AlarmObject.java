package com.example.alarmclock.alarm;

import android.net.Uri;

public class AlarmObject {

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;


    long id = -1;
    int timeHour = 0;
    int timeMinute = 0 ;
    boolean repeatingDays[];
    boolean repeatWeekly = false;
    Uri alarmTone = null;
    String name = "";
    String type = "";
    int volume = 100;
    boolean isEnabled = true;

    public AlarmObject() {
        repeatingDays = new boolean[]{false,false,false,false,false,false,false};
    }

    public AlarmObject(long id, int timeHour, int timeMinute,
                       boolean[] repeatingDays, boolean repeatWeekly, Uri alarmTone,
                       String name,String type,int volume,  boolean isEnabled) {
        this.id = id;
        this.timeHour = timeHour;
        this.timeMinute = timeMinute;
        this.repeatingDays = repeatingDays;
        this.repeatWeekly = repeatWeekly;
        this.alarmTone = alarmTone;
        this.name = name;
        this.type = type;
        this.volume = volume;
        this.isEnabled = isEnabled;
    }


    public void setRepeatingDay(int dayOfWeek, boolean value) {
        repeatingDays[dayOfWeek] = value;
    }

    public boolean getRepeatingDay(int dayOfWeek) {
        return repeatingDays[dayOfWeek];
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
    }

    public int getTimeMinute() {
        return timeMinute;
    }

    public void setTimeMinute(int timeMinute) {
        this.timeMinute = timeMinute;
    }

    public boolean[] getRepeatingDays() {
        return repeatingDays;
    }

    public void setRepeatingDays(boolean[] repeatingDays) {
        this.repeatingDays = repeatingDays;
    }

    public boolean isRepeatWeekly() {
        return repeatWeekly;
    }

    public void setRepeatWeekly(boolean repeatWeekly) {
        this.repeatWeekly = repeatWeekly;
    }

    public Uri getAlarmTone() {
        return alarmTone;
    }

    public void setAlarmTone(Uri alarmTone) {
        this.alarmTone = alarmTone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
