package com.henryzhefeng.stopwatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 哲 on 12/23/2014.
 */
public class RecordListAdapter extends BaseAdapter {

    private List<Record> records = new ArrayList<Record>();

    // set place holder items
    {
        for (int i = 0; i < 8; i++) {
            records.add(new Record());
        }
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.record_list_view, null);
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.lab_order);
        textView.setText(String.valueOf(position));
        return view;
    }

    // add new record to records.
    public void addRecord(Record newRecord) {
        records.add(0, newRecord);
    }
}
