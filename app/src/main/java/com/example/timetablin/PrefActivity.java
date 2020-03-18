package com.example.timetablin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class PrefActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEdit;
    private Boolean dCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        pref = getSharedPreferences("TimetablinUPref", MODE_PRIVATE);
        prefEdit = pref.edit();

        Switch dark = findViewById(R.id.darkSwitch);
        Button guestCS = findViewById(R.id.btnGuestColor);
        Button lectureCS = findViewById(R.id.btnLectureColor);
        Button societyCS = findViewById(R.id.btnSocietyColor);
        Drawable guestDraw = guestCS.getBackground();
        Drawable lectureDraw = lectureCS.getBackground();
        Drawable societyDraw = societyCS.getBackground();

        guestDraw = DrawableCompat.wrap(guestDraw);
        lectureDraw = DrawableCompat.wrap(lectureDraw);
        societyDraw = DrawableCompat.wrap(societyDraw);

        String stringToRGB;
        if (pref.contains("guestColor")) {
            stringToRGB = pref.getString("guestColor","000000");
            DrawableCompat.setTint(guestDraw, toRGB(stringToRGB));
        }
        if (pref.contains("lectureColor")) {
            stringToRGB = pref.getString("lectureColor", "000000");
            DrawableCompat.setTint(lectureDraw, toRGB(stringToRGB));
        }
        if (pref.contains("societyColor")) {
            stringToRGB = pref.getString("societyColor", "000000");
            DrawableCompat.setTint(societyDraw, toRGB(stringToRGB));
        }

        //DrawableCompat.setTint(guestDraw, toRGB("000000")); //beginnings of setting color using drawable
        //DrawableCompat.setTint(guestDraw, toRGB("FFD1DC")); // -- https://stackoverflow.com/questions/29801031/how-to-add-button-tint-programmatically/37324427 "dimsuz"

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
        }); //TODO: save Dark Theme preference, maybe using Shared Preferences?

        //Potentially allow users to pick colors
    }
    @Override
    protected void onStop() {
        super.onStop();
        prefEdit.putBoolean("dark", dCheck);
        prefEdit.commit();
    }

    private void setDarkCheck(Switch s) {
        int mode = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(pref.contains("dark")) {
            dCheck = pref.getBoolean("dark", false);
            System.out.println("FOUND DARK SETTING: " + dCheck);
        } else {
            System.out.println("HAS NOT FOUND DARK SETTING");
            switch (mode) {
                case Configuration.UI_MODE_NIGHT_YES :
                    dCheck = true;
                    break;
                default :
                    dCheck = false;
                    break;
            }
        }
        s.setChecked(dCheck);
    }

    public int toRGB(String sCol) {
        int colorConvert = Integer.parseInt(sCol, 16);
        int r = (colorConvert >> 16) & 0xFF;
        int g = (colorConvert >> 8) & 0xFF;
        int b = (colorConvert >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
}
