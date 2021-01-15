package com.example.alarmclock.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.alarmclock.helper.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmManagerHelper extends BroadcastReceiver {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME_HOUR = "timeHour";
    public static final String TIME_MINUTE = "timeMinute";
    public static final String TYPE = "alarmType";
    public static final String VOL = "vol";
    public static final String TONE = "alarmTone";
    public static final String ONCE = "once";
    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarms(context);
    }
    public static void setAlarms(Context context) {
        cancelAlarms(context);

        DBHelper dbHelper = new DBHelper(context);

        ArrayList<AlarmObject> alarms = dbHelper.getAlarms();

        for (AlarmObject alarm:alarms) {
            if (alarm.isEnabled) {
                PendingIntent pIntent = createPendingIntent(context, alarm);

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
                calendar.set(Calendar.MINUTE, alarm.timeMinute);
                calendar.set(Calendar.SECOND, 00);

                final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
                boolean alarmSet = false;


                if ((alarm.timeHour > nowHour) | ((alarm.timeHour == nowHour) && (alarm.timeMinute > nowMinute))){
                    calendar.set(Calendar.DAY_OF_WEEK, nowDay);
                    setAlarm(context, calendar, pIntent);
                    alarmSet = true;
                } else {
                    for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++ dayOfWeek) {
                        if ( alarm.getRepeatingDay(dayOfWeek -1 ) && dayOfWeek >= nowDay && !(dayOfWeek == nowDay && alarm.timeHour < nowHour)
                                && !(dayOfWeek == nowDay && alarm.timeHour == nowHour && alarm.timeMinute <= nowMinute)) {
                            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                            setAlarm(context, calendar, pIntent);
                            alarmSet = true;
                            break;
                        }
                    }
                }

                if (!alarmSet) {
                    for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++ dayOfWeek) {
                        if (alarm.getRepeatingDay(dayOfWeek -1 ) && dayOfWeek <= nowDay && alarm.repeatWeekly) {
                            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                            calendar.add(Calendar.WEEK_OF_YEAR, 1);

                            setAlarm(context, calendar, pIntent);
                            break;
                        }
                    }
                }
            }
        }
    }
    public static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
    }

    public static void cancelAlarms(Context context) {
        DBHelper dbHelper = new DBHelper(context);

        ArrayList<AlarmObject> alarms = dbHelper.getAlarms();

        if (alarms != null)
        {
            for (AlarmObject alarm:alarms) {
                if (alarm.isEnabled) {
                    PendingIntent pIntent = createPendingIntent(context, alarm);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                    alarmManager.cancel(pIntent);
                }
            }
        }
    }
    private static PendingIntent createPendingIntent(Context context, AlarmObject model) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(ID, model.id);
        intent.putExtra(NAME, model.name);
        intent.putExtra(TIME_HOUR, model.timeHour);
        intent.putExtra(TIME_MINUTE, model.timeMinute);
        intent.putExtra(TONE, model.alarmTone.toString());
        intent.putExtra(TYPE, model.type);
        intent.putExtra(VOL,model.volume);

        if(       !model.getRepeatingDay(model.SUNDAY)  &&!model.getRepeatingDay(model.MONDAY)
                &&!model.getRepeatingDay(model.TUESDAY) &&!model.getRepeatingDay(model.WEDNESDAY)
                &&!model.getRepeatingDay(model.THURSDAY)&&!model.getRepeatingDay(model.FRIDAY)
                &&!model.getRepeatingDay(model.SATURDAY)){
            intent.putExtra(ONCE,true);
        }
        else {
            intent.putExtra(ONCE,false);
        }

        return PendingIntent.getService(context, (int) model.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
