package com.applandeo.materialcalendarsampleapp;

import android.content.Context;
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
    private Context context;

    public EventAdapter(ArrayList<String> list, Context context,
                        ArrayList<String> time_list,
                        ArrayList<String> date_list) {
        this.list = list;
        this.context = context;
        this.time_list = time_list;
        this.date_list = date_list;
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
            notifyDataSetChanged();
        });

        return view;
    }
}
