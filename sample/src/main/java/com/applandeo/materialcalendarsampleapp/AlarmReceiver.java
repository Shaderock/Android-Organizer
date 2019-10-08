package com.applandeo.materialcalendarsampleapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {

    NotificationManager nm;
    int id;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        id = intent.getIntExtra("id", -1);
        make_alarm(id, context);
        del_event(id, context);
    }

    public void del_event(int id, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_EVENTS, DBHelper.KEY_ID + "=" +
                id, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void make_alarm(int id, Context context) {
        String name = "";
        String description = "";

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_EVENTS,
                null, "_id = ?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor.moveToFirst()) {
            int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int desc_index = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);

            name = cursor.getString(name_index);
            description = cursor.getString(desc_index);
        }
        cursor.close();
        Toast.makeText(context, "Alarm triggered", Toast.LENGTH_LONG).
                show();

        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(context.getApplicationContext())
                .setContentText(description)
                .setContentTitle(name)
                .setSmallIcon(R.drawable.sample_icon_2)
                .build();
        nm.notify(0, notify);

        Intent intent = new Intent(context, CheckEvent.class);

    }
}
