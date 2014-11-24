package com.example.chikku.alarm;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chikku.utils.AlarmManager;
import com.chikku.utils.ListViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApplicationSelectActivity extends Activity {

    private Map<String, String> applicationPackageNameMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_select);

        applicationPackageNameMap = new HashMap<String, String>();

        final List<String> installedApplications = getInstalledApplications();

        Collections.sort(installedApplications);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, installedApplications);

        ListView applicationView = (ListView) findViewById(R.id.application_list_view);
        applicationView.setAdapter(arrayAdapter);

        applicationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent();
                intent.putExtra("AppName", installedApplications.get(position));
                intent.putExtra("PackageName", applicationPackageNameMap.get(installedApplications.get(position)));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private List<String> getInstalledApplications() {
        List<String> installedApplications = new ArrayList<String>();
        PackageManager manager = getPackageManager();
        List<ApplicationInfo> applicationInfos = manager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo info : applicationInfos) {
            if (manager.getLaunchIntentForPackage(info.packageName) != null) {
                installedApplications.add(info.loadLabel(manager).toString());
                this.applicationPackageNameMap.put(info.loadLabel(manager).toString(), info.packageName);
            }
        }
        return installedApplications;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_application_select, menu);
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
}
