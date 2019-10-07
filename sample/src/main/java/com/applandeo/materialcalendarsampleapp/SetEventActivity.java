package com.applandeo.materialcalendarsampleapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
    String date;
    TimePicker timePicker;
    EditText event_name;
    EditText event_description;

    String year_str;
    String month_str;
    String day_str;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_event);

        Intent intent1 = getIntent();
        day = intent1.getStringExtra("chosen_date");
        date = intent1.getStringExtra("date");

        year_str = intent1.getStringExtra("year");
        month_str = intent1.getStringExtra("month");
        day_str = intent1.getStringExtra("day");


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
            if (add_event())
                onBackPressed();
            else
                Toast.makeText(getApplicationContext(),
                        "Please, set future time",
                        Toast.LENGTH_SHORT).show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean add_event() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String name = event_name.getText().toString();
        String description = event_description.getText().toString();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar c = Calendar.getInstance();

        SimpleDateFormat hour_format = new SimpleDateFormat("HH");
        SimpleDateFormat minute_format = new SimpleDateFormat("mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String cur_date = dateFormat.format(c.getTime());
        String hour_str = hour_format.format(c.getTime());
        String minute_str = minute_format.format(c.getTime());

        if (cur_date.equals(date)) {
            if (hour < Integer.parseInt(hour_str)
                    || (hour == Integer.parseInt(hour_str)
                    && minute <= Integer.parseInt(minute_str)))
                return false;
        }

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

        set_alarm(id, Integer.valueOf(year_str), Integer.valueOf(month_str),
                Integer.valueOf(day_str), hour, minute);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void set_alarm(int id, int year, int month, int day, int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        Log.d("log", String.valueOf(System.currentTimeMillis()));
        cal.clear();
        cal.set(year, month - 1, day, hour, min, 0);
        Log.d("log", String.valueOf(cal.getTimeInMillis()));
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), pendingIntent);
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
