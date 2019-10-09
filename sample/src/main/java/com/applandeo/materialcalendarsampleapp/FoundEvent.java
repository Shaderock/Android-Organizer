package com.applandeo.materialcalendarsampleapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FoundEvent extends AppCompatActivity {

    ListView event_list;
    DBHelper dbHelper;
    String key_name;
    String date, day_str, month_str, year_str;

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

        event_list.setOnItemClickListener(((adapterView, view, i, l) -> {
            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int id = (int) event_ids.get(i);
            Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                    null, "_id = ?", new String[]{String.valueOf(id)},
                    null, null, null);

            // to prevent user from editing already alarmed event

            if (cursor.moveToFirst()) {
                int short_date_index = cursor.getColumnIndex(DBHelper.KEY_SHORT_DATE);
                int only_year_index = cursor.getColumnIndex(DBHelper.KEY_ONLY_YEAR);
                int only_month_index = cursor.getColumnIndex(DBHelper.KEY_ONLY_MONTH);
                int only_day_index = cursor.getColumnIndex(DBHelper.KEY_ONLY_DAY);

                Intent intent1 = new Intent(this, SetEventActivity.class);
                intent1.putExtra("chosen_event", id);
                intent1.putExtra("date", cursor.getString(short_date_index));
                intent1.putExtra("year", cursor.getString(only_year_index));
                intent1.putExtra("month", cursor.getString(only_month_index));
                intent1.putExtra("day", cursor.getString(only_day_index));
                startActivity(intent1);
            }
            else
                refresh_events();
            cursor.close();
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_events();
    }

    public void write_events() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> times = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                null, DBHelper.KEY_NAME + " LIKE ?",
                new String[]{"%" + key_name + "%"},
                null, null, DBHelper.KEY_SHORT_DATE);

        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(DBHelper.KEY_ID);
            int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int hour_index = cursor.getColumnIndex(DBHelper.KEY_HOUR);
            int minute_index = cursor.getColumnIndex(DBHelper.KEY_MINUTE);
            int short_day_index = cursor.getColumnIndex(DBHelper.KEY_SHORT_DATE);
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
                this, times, dates, event_ids);
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

    public void delete_event(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_EVENTS, DBHelper.KEY_ID + "=" +
                id, null);
    }

    public void refresh_events() {
        event_list.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<>()));
        write_events();
    }

    public void cancel_alarm(int id) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
    }
}
