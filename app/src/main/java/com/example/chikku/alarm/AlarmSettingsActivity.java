package com.example.chikku.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chikku.utils.AlarmManager;
import com.chikku.utils.AlarmXmlManager;
import com.chikku.utils.AlarmsHandler;
import com.pojo.Alarm;
import com.pojo.ApplicationDetails;

import org.w3c.dom.Text;

import java.io.File;


public class AlarmSettingsActivity extends Activity {

    private int currentPosition;
    private Alarm alarm;
    private String defaultAppPackageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);
        Intent intent = getIntent();
        this.currentPosition = intent.getIntExtra(AlarmSetActivity.ALARM_FOR_SETTINGS, -1);

        if (currentPosition >= 0) {
            this.alarm = AlarmManager.getInstance().getAlarm(this.currentPosition);
            setValues();
        } else {
            Switch statusSwitch = (Switch) findViewById(R.id.alarm_status_switch);
            statusSwitch.setChecked(true);

            TextView defaultApplicationName = (TextView) findViewById(R.id.default_application_name);
            defaultApplicationName.setText("No Default App");

            createAlarm();
        }
    }

    private void createAlarm() {

        Log.d("Alarm Settings", "creating new alarm");
        AlarmManager manager = AlarmManager.getInstance();

        this.alarm = new Alarm();
        this.alarm.setName("Alarm " + (int) (manager.getNumberOfAlarms() + 1));
        this.alarm.setEnabled(true);
        this.defaultAppPackageName = "";

    }

    private void setValues() {
        TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_settings_time_picker);
        Switch statusSwitch = (Switch) findViewById(R.id.alarm_status_switch);

        if (this.alarm.isEnabled()) {
            statusSwitch.setChecked(true);
        } else {
            statusSwitch.setChecked(false);
        }

        timePicker.setCurrentHour(this.alarm.getHour());
        timePicker.setCurrentMinute(this.alarm.getMinutes());

        TextView defaultApplicationName = (TextView) findViewById(R.id.default_application_name);
        defaultApplicationName.setText(this.alarm.getLaunchAppDetails().getApplicationName());

        this.defaultAppPackageName = this.alarm.getLaunchAppDetails().getPackageName();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_settings, menu);
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

    public void onCancel(View view) {
        finish();
    }

    public void saveAlarm(View view) {
        TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_settings_time_picker);
        Switch statusSwitch = (Switch) findViewById(R.id.alarm_status_switch);
        TextView defaultAppName = (TextView) findViewById(R.id.default_application_name);

        AlarmManager manager = AlarmManager.getInstance();

        this.alarm.setEnabled(statusSwitch.isChecked());


        this.alarm.setHour(timePicker.getCurrentHour());
        this.alarm.setMinutes(timePicker.getCurrentMinute());

        this.alarm.setAlarmTimeHour(this.alarm.getHour());
        this.alarm.setAlarmMinute(this.alarm.getMinutes());

        this.alarm.getLaunchAppDetails().setApplicationName(defaultAppName.getText().toString());
        this.alarm.getLaunchAppDetails().setPackageName(this.defaultAppPackageName);

        if (this.currentPosition < 0) {
            manager.addAlarm(this.alarm);
        }

        Log.d("Test", manager.toString());

        AlarmXmlManager.flushAlarmManager();

        AlarmsHandler.setAlarm(getApplicationContext());
        finish();
    }


    public void deleteAlarm(View view) {
        if (currentPosition >= 0) {
            String deleteAlarmName = this.alarm.getName();
            AlarmManager.getInstance().removeAlarm(currentPosition);
            AlarmsHandler.cleanUpAfterDelete(deleteAlarmName, getApplicationContext());
        }
        AlarmXmlManager.flushAlarmManager();

        finish();
    }

    public void launchAppSelectActivity(View view) {
        Intent launchIntent = new Intent(this, ApplicationSelectActivity.class);
        startActivityForResult(launchIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.d("Alarm Settings", "Adding default app");
                this.defaultAppPackageName = data.getStringExtra("PackageName");
                TextView defaultAppName = (TextView) findViewById(R.id.default_application_name);
                defaultAppName.setText(data.getStringExtra("AppName"));
                defaultAppName.postInvalidate();
            }
        }
    }

}
