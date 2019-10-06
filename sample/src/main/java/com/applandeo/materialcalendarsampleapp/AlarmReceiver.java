package com.applandeo.materialcalendarsampleapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {

    NotificationManager nm;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm triggered", Toast.LENGTH_LONG).show();
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification notify = new Notification.Builder(context.getApplicationContext())
                .setContentText("It's time to dance!")
                .setContentTitle("What time is it?")
                .setSmallIcon(R.drawable.sample_icon_2)
                .build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(0, notify);
    }
}
