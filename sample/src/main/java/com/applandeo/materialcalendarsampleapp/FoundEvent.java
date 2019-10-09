package com.applandeo.materialcalendarsampleapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FoundEvent extends AppCompatActivity {

    ListView event_list;
    DBHelper dbHelper;
    String key_name;

    ArrayList<Object> event_ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_event);
        event_list = findViewById(R.id.event_list);
        dbHelper = new DBHelper(this);
        Intent intent = getIntent();
        key_name = intent.getStringExtra("key_name");
        write_events();
    }

    public void write_events() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> times = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                null, DBHelper.KEY_NAME + " LIKE ?",
                new String[]{"%" + key_name + "%"},
                null, null, null);

        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(DBHelper.KEY_ID);
            int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int hour_index = cursor.getColumnIndex(DBHelper.KEY_HOUR);
            int minute_index = cursor.getColumnIndex(DBHelper.KEY_MINUTE);
            int short_day_index = cursor.getColumnIndex(DBHelper.KEY_SHORT_DAY);
            do {
                String time = get_formatted_time(cursor.getInt(hour_index),
                        cursor.getInt(minute_index));
                String date = cursor.getString(short_day_index);

                names.add(cursor.getString(name_index));
                dates.add(date);
                times.add(time);
                event_ids.add(cursor.getInt(id_index));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
        }

        EventAdapter eventAdapter = new EventAdapter(names,
                this, times, dates);
        event_list.setAdapter(eventAdapter);
    }

    public String get_formatted_time(int hour, int minute) {
        String time = "";
        if (hour < 10)
            time += "0";
        time += hour + ":";
        if (minute < 10)
            time += "0";
        time += minute;
        return time;
    }
}
