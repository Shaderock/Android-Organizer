package com.applandeo.materialcalendarsampleapp;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_event);

        Intent intent1 = getIntent();
        day = intent1.getStringExtra("chosen_date");

        Button add_event = findViewById(R.id.add_event);
        add_event.setOnClickListener(view -> {
            Intent intent = new Intent(this, SetEventActivity.class);
            intent.putExtra("chosen_date", day);
            startActivity(intent);
        });

        write_events();

        del_list.setOnItemClickListener((adapterView, view, i, l) -> {
            int id = (int) event_ids.get(i);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(DBHelper.TABLE_EVENTS, DBHelper.KEY_ID + "=" +
                    id, null);
            refresh_events();
        });
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
        event_list = findViewById(R.id.event_list);
        del_list = findViewById(R.id.del_list);
        ArrayList<Object> names = new ArrayList<>();
        ArrayList<Object> arrayList = new ArrayList<>();
        event_ids.clear();
        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(DBHelper.KEY_ID);
            int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int desc_index = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            int hour_index = cursor.getColumnIndex(DBHelper.KEY_HOUR);
            int minute_index = cursor.getColumnIndex(DBHelper.KEY_MINUTE);
            do {
                names.add( cursor.getInt(hour_index) + ":"
                        + cursor.getInt(minute_index) + " | "
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
}
