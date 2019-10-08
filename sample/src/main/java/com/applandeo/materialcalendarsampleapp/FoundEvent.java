package com.applandeo.materialcalendarsampleapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class FoundEvent extends AppCompatActivity {

    ListView event_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_event);
        event_list = findViewById(R.id.event_list);
    }
}
