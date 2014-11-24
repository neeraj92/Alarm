package com.example.chikku.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;


import com.chikku.utils.AlarmManager;
import com.chikku.utils.AlarmXmlManager;
import com.chikku.utils.ListViewAdapter;
import com.pojo.Alarm;


public class AlarmSetActivity extends Activity {

    public static final String ALARM_FOR_SETTINGS = "com.chikku.alarm.alarm_for_settings";

    @Override
    protected void onResume() {
        super.onResume();
        AlarmManager.getInstance().normalize();
        ListView view = (ListView) findViewById(R.id.alarm_list_view);
        ListViewAdapter adapter = (ListViewAdapter) view.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        AlarmXmlManager.init(getDir("Alarms", Context.MODE_PRIVATE).getAbsolutePath());
        final ListViewAdapter arrayAdapter = new ListViewAdapter();
        AlarmManager.getInstance().normalize();

        ListView alarmsView = (ListView) findViewById(R.id.alarm_list_view);

        alarmsView.setAdapter(arrayAdapter);

        alarmsView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d("Test", "list view click");

                Intent intent = new Intent(getApplicationContext(), AlarmSettingsActivity.class);
                intent.putExtra(ALARM_FOR_SETTINGS, position);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void createNewAlarm(View view) {
        Intent intent = new Intent(this, AlarmSettingsActivity.class);
        int alarmIndex = -1;
        intent.putExtra(ALARM_FOR_SETTINGS, alarmIndex);
        startActivity(intent);

    }


}