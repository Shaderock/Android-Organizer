package com.applandeo.materialcalendarsampleapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "organizer_db";
    public static final String TABLE_EVENTS = "events";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_HOUR = "hour";
    public static final String KEY_MINUTE = "minute";
    public static final String KEY_DAY = "day";
    public static final String KEY_SHORT_DATE = "shortDate";
    public static final String KEY_ONLY_DAY = "shortDay";
    public static final String KEY_ONLY_MONTH = "shortMonth";
    public static final String KEY_ONLY_YEAR = "shortYear";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_EVENTS + "("
                + KEY_ID + " integer primary key,"
                + KEY_NAME + " text,"
                + KEY_DESCRIPTION + " text,"
                + KEY_HOUR + " integer,"
                + KEY_MINUTE + " integer,"
                + KEY_SHORT_DATE + " text,"
                + KEY_ONLY_DAY + " text,"
                + KEY_ONLY_MONTH + " text,"
                + KEY_ONLY_YEAR + " text,"
                + KEY_DAY + " text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_EVENTS);

        onCreate(sqLiteDatabase);
    }
}
