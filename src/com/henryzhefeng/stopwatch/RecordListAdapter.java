package com.henryzhefeng.stopwatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 哲 on 12/23/2014.
 */
public class RecordListAdapter extends BaseAdapter {

    private List<Record> records = new ArrayList<Record>();
    private int recordNum;
    private final int PLACE_HOLDER_NUM = 8;

    RecordListAdapter() {
        reset();
    }

    public void reset() {
        recordNum = 0;
        // clear records
        records.clear();
        // set place holder items
        for (int i = 0; i < PLACE_HOLDER_NUM; i++) {
            records.add(new Record());
        }
        notifyDataSetInvalidated();
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
            Button btn = (Button) view.findViewById(R.id.delta);
            btn.setFocusable(false);
            btn.setClickable(false);
        } else {
            view = convertView;
        }
        Record record = records.get(position);
        int order = record.order;
        View layout = view.findViewById(R.id.record_layout);
        if (order == -1) {
            // this is just a "place holder" record.
            layout.setVisibility(View.INVISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            // set text
            TextView orderTextView = (TextView) view.findViewById(R.id.lap_order);
            orderTextView.setText(String.valueOf(order));
            TextView timeText = (TextView) view.findViewById(R.id.time_text);
            Period period = record.period;
            timeText.setText(String.format("%02d:%02d.%01d", period.minutes, period.seconds, period.tenOfSec));
            Button deltaBtn = (Button) view.findViewById(R.id.delta);
            // calculate the delta
            // there is no previous record or not
            if (order == 1) {
                deltaBtn.setText(String.format("+%02d:%02d.%01d", period.minutes, period.seconds, period.tenOfSec));
            } else {
                // get the previous period
                Period prePeriod = records.get(position + 1).period;
                // calculate the delta in ten of a second
                int deltaUnit = (period.minutes * 600 + period.seconds * 10 + period.tenOfSec)
                        - (prePeriod.minutes * 600 + prePeriod.seconds * 10 + prePeriod.tenOfSec);
                int deltaMin = deltaUnit / 600;
                int deltaSec = deltaUnit / 10 - deltaMin * 60;
                int deltaTS = deltaUnit % 10;
                deltaBtn.setText(String.format("+%02d:%02d.%01d", deltaMin, deltaSec, deltaTS));
            }
        }
        return view;
    }

    // add new record to records.
    public void addRecord(Period period) {
        records.add(0, new Record(++recordNum, period));
        notifyDataSetInvalidated();
    }
}
