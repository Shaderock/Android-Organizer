package com.applandeo.materialcalendarsampleapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list;
    private ArrayList<String> time_list;
    private ArrayList<String> date_list;
    private ArrayList<Object> event_ids;
    DBHelper dbHelper;
    private Context context;

    public EventAdapter(ArrayList<String> list, Context context,
                        ArrayList<String> time_list,
                        ArrayList<String> date_list,
                        ArrayList<Object> event_ids) {
        this.list = list;
        this.context = context;
        this.time_list = time_list;
        this.date_list = date_list;
        this.event_ids = event_ids;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position,
                        View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.found_event, null);
        }
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(date_list.get(position));

        TextView time = (TextView) view.findViewById(R.id.time);
        time.setText(time_list.get(position));

        ImageView del_button = (ImageView) view.findViewById(R.id.delete_btn);

        del_button.setOnClickListener(view1 -> {
            list.remove(position);
            int id = (int) event_ids.get(position);
            cancel_alarm(id);
            delete_event(id);
            notifyDataSetChanged();
        });

        return view;
    }

    public void cancel_alarm(int id) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
    }
    public void delete_event(int id) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_EVENTS, DBHelper.KEY_ID + "=" +
                id, null);
    }
}
