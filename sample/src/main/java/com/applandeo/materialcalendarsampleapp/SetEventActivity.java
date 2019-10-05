package com.applandeo.materialcalendarsampleapp;

import android.os.Bundle;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class SetEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_event);

        TimePicker timePicker = findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);
    }
}
