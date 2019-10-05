package com.applandeo.materialcalendarsampleapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;

public class CheckEvent extends AppCompatActivity {

    DBHelper dbHelper;
    ListView event_list;
    ListView del_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_event);

        Button add_event = findViewById(R.id.add_event);
        add_event.setOnClickListener(view -> {
            Intent intent = new Intent(this, SetEventActivity.class);
            startActivity(intent);
        });

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                null, null, null,
                null, null, null);

        event_list = findViewById(R.id.event_list);
        del_list = findViewById(R.id.del_list);
        ArrayList arrayList = new ArrayList<>();
        ArrayList arrayList1 = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(DBHelper.KEY_ID);
            int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int desc_index = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            do {
                //write_events(cursor.getString(name_index));
                arrayList.add(cursor.getString(name_index));
                arrayList1.add("Delete");
            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
            cursor.close();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList1);
        event_list.setAdapter(arrayAdapter);
        del_list.setAdapter(arrayAdapter1);
    }

    public void write_events(String name) {
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout);

        TextView event_name = new TextView(this);
        event_name.setText(name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            event_name.setId(View.generateViewId());
        }
        event_name.setTextSize(30);

        constraintLayout.addView(event_name);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        TextView cur_events = findViewById(R.id.cur_events);

        constraintSet.connect(event_name.getId(), ConstraintSet.TOP,
                cur_events.getId(), ConstraintSet.BOTTOM, 8);
        constraintSet.connect(event_name.getId(), ConstraintSet.LEFT,
                constraintLayout.getId(), ConstraintSet.LEFT, 32);
//        constraintSet.connect(event_name.getId(), ConstraintSet.RIGHT,
//                constraintLayout.getId(), ConstraintSet.RIGHT, 8);

        constraintSet.applyTo(constraintLayout);
    }
}
