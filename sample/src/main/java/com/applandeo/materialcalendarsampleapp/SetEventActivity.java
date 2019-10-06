package com.applandeo.materialcalendarsampleapp;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetEventActivity extends AppCompatActivity {

    DBHelper dbHelper;
    int id = -1;
    String day;
    TimePicker timePicker;
    EditText event_name;
    EditText event_description;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_event);

        Intent intent1 = getIntent();
        day = intent1.getStringExtra("chosen_date");

        Intent intent = getIntent();
        id = intent.getIntExtra("chosen_event", -1);

        timePicker = findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);

        dbHelper = new DBHelper(this);
        Button add_event = findViewById(R.id.add_event);
        event_name = findViewById(R.id.event_name);
        event_description = findViewById(R.id.event_description);

        if (id != -1) {
            set_values();
        }

        add_event.setOnClickListener(view ->
        {
            add_event();
            onBackPressed();
        });
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("HH");
            SimpleDateFormat df1 = new SimpleDateFormat("mm");

            String formatted_date = df.format(c.getTime()) + ":"
                    + df1.format(c.getTime());

            Toast.makeText(getApplicationContext(),
                    formatted_date,
                    Toast.LENGTH_SHORT).show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void add_event() {
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
        } else {
            db.update(DBHelper.TABLE_EVENTS, contentValues,
                    "_id = ?", new String[]{String.valueOf(id)});
        }
    }

    public void set_alarm() {
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void set_values() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                null, "_id = ?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor.moveToFirst()) {
            int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int desc_index = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            int hour_index = cursor.getColumnIndex(DBHelper.KEY_HOUR);
            int minute_index = cursor.getColumnIndex(DBHelper.KEY_MINUTE);
            int day_index = cursor.getColumnIndex(DBHelper.KEY_DAY);
            do {
                event_name.setText(cursor.getString(name_index));
                event_description.setText(cursor.getString(desc_index));
                timePicker.setHour(cursor.getInt(hour_index));
                timePicker.setMinute(cursor.getInt(minute_index));
                day = cursor.getString(day_index);
            } while (cursor.moveToNext());
        } else {
            cursor.close();
        }
    }
}
