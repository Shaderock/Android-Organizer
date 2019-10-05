package com.applandeo.materialcalendarsampleapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class SetEventActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_event);

        TimePicker timePicker = findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);

        dbHelper = new DBHelper(this);
        Button add_event = findViewById(R.id.add_event);
        EditText event_name = findViewById(R.id.event_name);
        EditText event_description = findViewById(R.id.event_description);



        add_event.setOnClickListener(view ->
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            String name = event_name.getText().toString();
            String description = event_description.getText().toString();

            contentValues.put(DBHelper.KEY_NAME, name);
            contentValues.put(DBHelper.KEY_DESCRIPTION, description);

            db.insert(DBHelper.TABLE_EVENTS, null, contentValues);

            Intent intent = new Intent(this, CheckEvent.class);
            startActivity(intent);
        });
    }
}
