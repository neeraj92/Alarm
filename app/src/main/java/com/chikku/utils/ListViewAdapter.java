package com.chikku.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chikku.alarm.AlarmSetActivity;
import com.example.chikku.alarm.R;
import com.pojo.Alarm;

import java.util.List;

/**
 * Created by chikku on 14/11/14.
 */
public class ListViewAdapter extends BaseAdapter {

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.alarm_represent_layout , parent , false);
        }

        TextView alarmName = (TextView) convertView.findViewById(R.id.textView);
        TextView alarmTime = (TextView) convertView.findViewById(R.id.textView2);
        TextView status = (TextView) convertView.findViewById(R.id.textView3);

        Alarm alarm = AlarmManager.getInstance().getAlarm(position);

        alarmName.setText(alarm.getName());
        alarmTime.setText(alarm.getTime());

        if (alarm.isEnabled()){
            status.setText("On");
        }
        else{
            status.setText("Off");
        }

        return convertView;
    }

    @Override
    public Alarm getItem(int position) {
        return AlarmManager.getInstance().getAlarm(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return AlarmManager.getInstance().getNumberOfAlarms();
    }

    @Override
    public boolean isEmpty() {
        if (AlarmManager.getInstance().getNumberOfAlarms() == 0) {
            return true;
        }
        return false;
    }
}
