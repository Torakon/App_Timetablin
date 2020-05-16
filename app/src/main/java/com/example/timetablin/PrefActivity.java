package com.example.timetablin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.CompoundButton;
import android.widget.Switch;

public class PrefActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private Boolean dCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        pref = getSharedPreferences("TimetablinUPref", MODE_PRIVATE);

        Switch dark = findViewById(R.id.darkSwitch);

        setDarkCheck(dark);

        dark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean checker) {
                if (checker) {
                    dCheck = true;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                if (!checker) {
                    dCheck = false;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putBoolean("dark", dCheck);
        prefEdit.apply();
    }

    /*
     * Adjusts the "Dark Mode" switch to be in line with the current theme. If the application is in dark mode
     * then the switch will be set to the right.
     * @param darkSwitch
     */
    private void setDarkCheck(Switch darkSwitch) {
        int mode = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(pref.contains("dark")) {
            dCheck = pref.getBoolean("dark", false);
            System.out.println("FOUND DARK SETTING: " + dCheck);
        } else {
            System.out.println("HAS NOT FOUND DARK SETTING");
            dCheck = (mode == Configuration.UI_MODE_NIGHT_YES);
        }
        darkSwitch.setChecked(dCheck);
    }
}
