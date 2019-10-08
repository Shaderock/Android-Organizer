package com.applandeo.materialcalendarsampleapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CheckEvent extends AppCompatActivity {

    DBHelper dbHelper;
    ListView event_list;
    ListView del_list;
    ArrayList<Object> event_ids = new ArrayList<>();
    String day;
    String date;
    String year_str;
    String month_str;
    String day_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_event);

        Intent intent1 = getIntent();
        day = intent1.getStringExtra("chosen_date");
        date = intent1.getStringExtra("date");

        year_str = intent1.getStringExtra("year");
        month_str = intent1.getStringExtra("month");
        day_str = intent1.getStringExtra("day");


        Button add_event = findViewById(R.id.add_event);
        add_event.setOnClickListener(view -> {
            Intent intent = new Intent(this, SetEventActivity.class);
            intent.putExtra("chosen_date", day);
            intent.putExtra("date", date);
            intent.putExtra("year", year_str);
            intent.putExtra("month", month_str);
            intent.putExtra("day", day_str);
            startActivity(intent);
        });

        event_list = findViewById(R.id.event_list);
        del_list = findViewById(R.id.del_list);

        write_events();

        del_list.setOnItemClickListener((adapterView, view, i, l) -> {
            int id = (int) event_ids.get(i);
            cancel_alarm(id);
            delete_event(id);
            refresh_events();
        });

        event_list.setOnItemClickListener(((adapterView, view, i, l) -> {
            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int id = (int) event_ids.get(i);
            Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                    null, "_id = ?", new String[]{String.valueOf(id)},
                    null, null, null);

            // to prevent user from editing already alarmed event

            if (cursor.moveToFirst()) {
                Intent intent = new Intent(this, SetEventActivity.class);
                intent.putExtra("chosen_event", id);
                intent.putExtra("chosen_date", day);
                intent.putExtra("date", date);
                intent.putExtra("year", year_str);
                intent.putExtra("month", month_str);
                intent.putExtra("day", day_str);
                startActivity(intent);
            }
            else
                refresh_events();
            cursor.close();
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        write_events();
    }

    public void write_events() {
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                null, "day = ?", new String[]{day},
                null, null, null);
        ArrayList<Object> names = new ArrayList<>();
        ArrayList<Object> arrayList = new ArrayList<>();
        event_ids.clear();

        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(DBHelper.KEY_ID);
            int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int hour_index = cursor.getColumnIndex(DBHelper.KEY_HOUR);
            int minute_index = cursor.getColumnIndex(DBHelper.KEY_MINUTE);
            do {
                String time = "";
                if (cursor.getInt(hour_index) < 10)
                    time += "0";
                time += cursor.getInt(hour_index) + ":";
                if (cursor.getInt(minute_index) < 10)
                    time += "0";
                time += cursor.getInt(minute_index);

                names.add(time + " | "
                        + cursor.getString(name_index));
                arrayList.add("Delete");
                event_ids.add(cursor.getInt(id_index));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
        }

        ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, names);
        ArrayAdapter<Object> arrayAdapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayList);
        event_list.setAdapter(arrayAdapter);
        del_list.setAdapter(arrayAdapter1);
    }

    public void refresh_events() {
        event_list.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<>()));
        del_list.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<>()));
        write_events();
    }

    public void delete_event(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_EVENTS, DBHelper.KEY_ID + "=" +
                id, null);
    }

    public void cancel_alarm(int id) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
    }
}
