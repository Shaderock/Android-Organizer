package com.applandeo.materialcalendarsampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -1);

        calendarView.setMinimumDate(min);

        Button add_button = findViewById(R.id.check_button);

        add_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, CheckEvent.class);
            intent.putExtra("chosen_date", get_date_str());
            intent.putExtra("date", get_date());
            startActivity(intent);
        });
    }

    public String get_date_str() {
        String chosen_date = null;
        for (Calendar calendar : calendarView.getSelectedDates()) {
            chosen_date = calendar.getTime().toString();
        }
        return chosen_date;
    }
    public String get_date() {
        Date date = null;
        for (Calendar calendar : calendarView.getSelectedDates()) {
            date = calendar.getTime();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date_str = simpleDateFormat.format(date);
        return date_str;
    }
}