package com.applandeo.materialcalendarsampleapp;

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
    public void write_events()
    {
        ArrayList<String> names = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                null, DBHelper.KEY_NAME + " LIKE ? OR (" +
                        DBHelper.KEY_DESCRIPTION + " LIKE ?) OR (" +
                        DBHelper.KEY_DAY + " LIKE ?)",
                new String[]{"%" + key_name + "%"},
                null, null, null);

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
                event_ids.add(cursor.getInt(id_index));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, names);
        event_list.setAdapter(arrayAdapter);

    }
}
