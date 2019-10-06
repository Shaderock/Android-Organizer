package com.applandeo.materialcalendarsampleapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SetEventActivity extends AppCompatActivity {

    DBHelper dbHelper;
    int id = -1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_event);

        Intent intent1 = getIntent();
        String day;
        day = intent1.getStringExtra("chosen_date");

        Intent intent = getIntent();
        id = intent.getIntExtra("chosen_event", -1);

        TimePicker timePicker = findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);

        dbHelper = new DBHelper(this);
        Button add_event = findViewById(R.id.add_event);
        EditText event_name = findViewById(R.id.event_name);
        EditText event_description = findViewById(R.id.event_description);

        if (id != -1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                    null, "_id = ?", new String[]{String.valueOf(id)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                int id_index = cursor.getColumnIndex(DBHelper.KEY_ID);
                int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
                int desc_index = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
                int hour_index = cursor.getColumnIndex(DBHelper.KEY_HOUR);
                int minute_index = cursor.getColumnIndex(DBHelper.KEY_MINUTE);
                do {
                    event_name.setText(cursor.getString(name_index));
                    event_description.setText(cursor.getString(desc_index));
                    timePicker.setHour(cursor.getInt(hour_index));
                    timePicker.setMinute(cursor.getInt(minute_index));
                } while (cursor.moveToNext());
            } else {
                cursor.close();
            }
        }

        add_event.setOnClickListener(view ->
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            String name = event_name.getText().toString();
            String description = event_description.getText().toString();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            contentValues.put(DBHelper.KEY_NAME, name);
            contentValues.put(DBHelper.KEY_DESCRIPTION, description);
            contentValues.put(DBHelper.KEY_HOUR, hour);
            contentValues.put(DBHelper.KEY_MINUTE, minute);
            contentValues.put(DBHelper.KEY_DAY, day);

            if (id == -1) {
                db.insert(DBHelper.TABLE_EVENTS, null, contentValues);
            }
            else {
                db.update(DBHelper.TABLE_EVENTS, contentValues,
                        "_id = ?", new String[]{String.valueOf(id)});
            }

            onBackPressed();
        });

    }
}
