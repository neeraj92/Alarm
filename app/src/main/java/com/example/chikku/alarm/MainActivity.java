package com.example.chikku.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import com.chikku.utils.AlarmManager;
import com.pojo.Alarm;

import java.util.logging.Logger;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void setNewAlarm (View view){
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        AlarmManager manager = AlarmManager.getInstance ();
        Alarm alarm = new Alarm();
        alarm.setName("Alarm "+ (int)(manager.getNumberOfAlarms()+1));
        alarm.setHour(timePicker.getCurrentHour());
        alarm.setMinutes(timePicker.getCurrentMinute());

        manager.addAlarm(alarm);

        Log.d("main activity" , manager.toString());

    }
}
