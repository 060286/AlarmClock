package com.example.alarmclock.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.alarmclock.alarm.AlarmContract.Alarm;
import com.example.alarmclock.alarm.AlarmObject;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "alarmclock.db";

    private static final String SQL_CREATE_ALARM = "CREATE TABLE " + Alarm.TABLE_NAME + " (" +
            Alarm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Alarm.COLUMN_NAME_ALARM_NAME + " TEXT," +
            Alarm.COLUMN_NAME_ALARM_TIME_HOUR + " INTEGER," +
            Alarm.COLUMN_NAME_ALARM_TIME_MINUTE + " INTEGER," +
            Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS + " TEXT," +
            Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY + " BOOLEAN," +
            Alarm.COLUMN_NAME_ALARM_TONE + " TEXT," +
            Alarm.COLUMN_NAME_ALARM_TYPE + " TEXT," +
            Alarm.COLUMN_NAME_ALARM_VOL + " INTEGER," +
            Alarm.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN" + " );";


    private static final String SQL_DELETE_ALARM = "DROP TABLE IF EXISTS " + Alarm.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(SQL_CREATE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL(SQL_DELETE_ALARM);
        onCreate(db);
    }

    private AlarmObject populateModel(Cursor c) {
        AlarmObject model = new AlarmObject();
        model.setId(c.getLong(c.getColumnIndex(Alarm._ID)));
        model.setName(c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_NAME)));
        model.setTimeHour(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_HOUR)));
        model.setTimeMinute(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE)));
        model.setRepeatWeekly(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY)) == 0 ? false : true);
        model.setAlarmTone(Uri.parse(c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TONE))));
        model.setType(c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TYPE)));
        model.setVolume(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_VOL)));
        model.setEnabled(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_ENABLED)) == 0 ? false : true);

        String[] repeatingDays = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS)).split(",");
        for (int i = 0; i < repeatingDays.length; i++) {
            model.setRepeatingDay(i, repeatingDays[i].equals("false") ? false : true);
        }

        return model;
    }

    private ContentValues populateContent(AlarmObject model) {
        ContentValues values = new ContentValues();
        values.put(Alarm.COLUMN_NAME_ALARM_NAME, model.getName());
        values.put(Alarm.COLUMN_NAME_ALARM_TIME_HOUR, model.getTimeHour());
        values.put(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE, model.getTimeMinute());
        values.put(Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY, model.isRepeatWeekly());
        values.put(Alarm.COLUMN_NAME_ALARM_TONE, model.getAlarmTone() != null ? model.getAlarmTone().toString() : "");
        values.put(Alarm.COLUMN_NAME_ALARM_TYPE, model.getType());
        values.put(Alarm.COLUMN_NAME_ALARM_VOL, model.getVolume());
        values.put(Alarm.COLUMN_NAME_ALARM_ENABLED, model.isEnabled());

        String repeatingDays = "";
        for (int i = 0; i<7; i++) {
            repeatingDays += model.getRepeatingDay(i) + ",";
        }
        values.put(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS, repeatingDays);

        return values;
    }

    public long createAlarm(AlarmObject model) {
        ContentValues values = populateContent(model);
        return getWritableDatabase().insert(Alarm.TABLE_NAME, null, values);
    }

    public AlarmObject getAlarm(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Alarm.TABLE_NAME + " WHERE " + Alarm._ID + " = " + id;

        Cursor c = db.rawQuery(select, null);
        if (c.moveToNext()) {
            return populateModel(c);
        }
        return null;
    }

    public long updateAlarm(AlarmObject model) {
        ContentValues values = populateContent(model);
        return getWritableDatabase().update(Alarm.TABLE_NAME, values, Alarm._ID + " = ?", new String[] {String.valueOf(model.getId())});
    }

    public int deleteAlarm(long id) {
        return getWritableDatabase().delete(Alarm.TABLE_NAME, Alarm._ID + " = ?", new String[] {String.valueOf(id)});
    }

    public ArrayList<AlarmObject> getAlarms() {
        SQLiteDatabase db = getReadableDatabase();

        String select = "SELECT * FROM " + Alarm.TABLE_NAME;

        Cursor c = db.rawQuery(select, null);


        ArrayList<AlarmObject> alarmList = new ArrayList<>();
        while (c.moveToNext()) {
            alarmList.add(populateModel(c));
        }

        if (!alarmList.isEmpty()) {

            return alarmList;
        }

        return null;
    }

}
