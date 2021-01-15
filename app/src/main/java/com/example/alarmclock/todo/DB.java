package com.example.alarmclock.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DB extends SQLiteOpenHelper {
    private static final String DB_NAME = "TODOLIST";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME  = "Tasks";
    private static final String KEY_ID      = "ID";
    private static final String KEY_TITLE   = "Title";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_DATE    = "Date";
    private static final String KEY_STATUS  = "Status";

    private SQLiteDatabase sqLiteDatabase;

    public DB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "create table " + TABLE_NAME + " ( " +
                    KEY_ID + " integer primary key, " +
                    KEY_TITLE + " text, " +
                    KEY_DESCRIPTION + " text, " +
                    KEY_DATE + " text, " +
                    KEY_STATUS + " integer" + " )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

     /**
      * Insert task into database - Todotasks
      * @param title
      * @param desc
      * @param date
      * @param status
     */
    public void insertTask (String title, String desc, String date, int status) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_DESCRIPTION, desc);
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_STATUS, status);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public ArrayList<Task> getAllCompletedTasks () {
        ArrayList<Task> taskList = new ArrayList<>();
        sqLiteDatabase = this.getReadableDatabase();
        String selectQuery = "select * from " + TABLE_NAME + " where " + KEY_STATUS + " = 1";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                task.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                task.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                taskList.add(task);
            } while (cursor.moveToNext());

        }

        sqLiteDatabase.close();
        return taskList;
    }

    public ArrayList<Task> getAllTasks () {
        ArrayList<Task> taskList = new ArrayList<>();

        sqLiteDatabase = this.getReadableDatabase();

        String selectQuery = "select * from " + TABLE_NAME;

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                task.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                task.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                taskList.add(task);
            } while (cursor.moveToNext());
        }

        sqLiteDatabase.close();
        return taskList;
    }

    /**
     * update status of ID
     * @param targetID
     * @param status
     * @return
     */
    public int updateTaskStatus (int targetID, int status) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);

        int count = sqLiteDatabase.update(TABLE_NAME, values, KEY_ID+"=?", new String[]{String.valueOf(targetID)});
        sqLiteDatabase.close();

        return count;
    }

    /**
     * @param targetTask
     * @param title
     * @param description
     * @param targetDate
     * @return
     */
    public int updateTask(Task targetTask, String title, String description, String targetDate) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_DATE, targetDate);

        int count = sqLiteDatabase.update(TABLE_NAME, values, KEY_ID+"=?", new String[]{String.valueOf(targetTask.getId())});

        sqLiteDatabase.close();
        return count;
    }

    /**
     * @param id
     * @return
     */
    public boolean deleteTask(int id) {
        sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NAME, KEY_ID+"=?", new String[]{String.valueOf(id)});

        sqLiteDatabase.close();

        return true;
    }
}
