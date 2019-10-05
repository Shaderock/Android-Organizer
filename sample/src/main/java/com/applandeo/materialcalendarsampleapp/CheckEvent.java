package com.applandeo.materialcalendarsampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CheckEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_event);

        Button add_event = findViewById(R.id.add_event);
        add_event.setOnClickListener(view -> {
            Intent intent = new Intent(this, SetEventActivity.class);
            startActivity(intent);

        });
    }
}
