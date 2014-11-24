package com.example.chikku.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowAnimationFrameStats;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chikku.utils.AlarmsHandler;
import com.pojo.Alarm;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AlarmActivity extends Activity {

    private PowerManager.WakeLock wakeLock = null;
    private KeyguardManager.KeyguardLock keyguardLock = null;
    private Vibrator vibrator = null;
    private Timer killActivityTimer = null;
    private Ringtone ringtone;
    private String alarmName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmName = getIntent().getStringExtra(AlarmsReceiver.ALARM_NAME);
        Log.d("Alarm", "Creating alarmRingtone for:" + alarmName);

        EditText textEdit = (EditText) findViewById(R.id.snooze_time);
        textEdit.setText("10");

        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (wakeLock == null) {
            wakeLock = manager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Alarm");
        }

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        boolean isKeyGuardLocked = keyguardManager.inKeyguardRestrictedInputMode();
        if (keyguardLock == null) {
            keyguardLock = keyguardManager.newKeyguardLock("AlarmActivity");
        }

        if (isKeyGuardLocked) {
            keyguardLock.disableKeyguard();
            isKeyGuardLocked = false;
        }

        wakeLock.acquire();

        this.killActivityTimer = new Timer();
        this.killActivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, 60000);

        Uri alarmRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        this.ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmRingtone);
        this.ringtone.play();

        Alarm alarm = com.chikku.utils.AlarmManager.getInstance().getAlarmByName(alarmName);
        if (alarm.isVibrate()) {
            this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {500, 500};
            vibrator.vibrate(pattern, 0);

        }

        setTitle(alarmName);

    }

    @Override
    public void onAttachedToWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onAttachedToWindow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
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

    public void quitAlarm(View view) {
        Alarm alarm = com.chikku.utils.AlarmManager.getInstance().getAlarmByName(this.alarmName);
        Log.d("Alarm", "Resetting alarm:" + alarm.getName());
        alarm.resetAlarmTime();
        cleanUpAfterAlarm();
    }

    private void cleanUpAfterAlarm() {
        wakeLock.release();
        keyguardLock.reenableKeyguard();
        this.killActivityTimer.cancel();
        if (this.vibrator != null) {
            this.vibrator.cancel();
        }

        this.ringtone.stop();

        AlarmsHandler.setAlarm(getApplicationContext());
        finish();
    }

    private void cleanUpAfterAlarmWithoutLock() {
        this.killActivityTimer.cancel();
        this.ringtone.stop();
        if (this.vibrator != null) {
            this.vibrator.cancel();
        }

        AlarmsHandler.setAlarm(getApplicationContext());
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void setupSnooze(View view) {
        Alarm alarm = com.chikku.utils.AlarmManager.getInstance().getAlarmByName(this.alarmName);
        EditText textEdit = (EditText) findViewById(R.id.snooze_time);
        int snoozeTime = Integer.parseInt(textEdit.getText().toString());
        Log.d("Alarm", "Adding snooze time:" + snoozeTime);
        alarm.addToAlarmTime(snoozeTime);
        AlarmsHandler.setAlarm(getApplicationContext());
        cleanUpAfterAlarm();
    }

    public void startNewApplication(View view) {
        PackageManager manager = getPackageManager();
        Alarm alarm = com.chikku.utils.AlarmManager.getInstance().getAlarmByName(this.alarmName);
        if (!alarm.getLaunchAppDetails().getPackageName().equals("")) {
            Intent launchIntent = manager.getLaunchIntentForPackage(alarm.getLaunchAppDetails().getPackageName());
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
            cleanUpAfterAlarmWithoutLock();
        }
    }
}
