package com.applandeo.materialcalendarsampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    EditText key_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        key_name = (EditText) findViewById(R.id.key_name);

        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -1);

        calendarView.setMinimumDate(min);

        Button add_button = findViewById(R.id.check_button);

        add_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, CheckEvent.class);
            intent.putExtra("chosen_date", get_date_str());
            intent.putExtra("date", get_date());
            intent.putExtra("year", get_date_year());
            intent.putExtra("month", get_date_month());
            intent.putExtra("day", get_date_day());
            startActivity(intent);
        });

        Button search_button = findViewById(R.id.search_button);

        search_button.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, FoundEvent.class);
            intent.putExtra("key_name", key_name.getText().toString());
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

    public String get_date_year() {
        Date date = null;
        for (Calendar calendar : calendarView.getSelectedDates()) {
            date = calendar.getTime();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String date_str = simpleDateFormat.format(date);
        return date_str;
    }

    public String get_date_month() {
        Date date = null;
        for (Calendar calendar : calendarView.getSelectedDates()) {
            date = calendar.getTime();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        String date_str = simpleDateFormat.format(date);
        return date_str;
    }

    public String get_date_day() {
        Date date = null;
        for (Calendar calendar : calendarView.getSelectedDates()) {
            date = calendar.getTime();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        String date_str = simpleDateFormat.format(date);
        return date_str;
    }
}