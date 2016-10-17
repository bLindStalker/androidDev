package com.application.ayakimenko.breakinglight.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.application.ayakimenko.breakinglight.R;

import java.util.Locale;

import static com.application.ayakimenko.breakinglight.helper.Helper.getLanguageShortName;
import static com.application.ayakimenko.breakinglight.constants.Constants.APP_PREFERENCES;
import static com.application.ayakimenko.breakinglight.constants.Constants.APP_PREFERENCES_LANGUAGE;

public class MenuActivity extends Activity {
    private static long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setLanguage();
    }

    private void setLanguage() {
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_LANGUAGE)) {
            int languageId = mSettings.getInt(APP_PREFERENCES_LANGUAGE, 0);
            String languageShortName = getLanguageShortName(languageId);
            Locale myLocale = new Locale(languageShortName);
            Resources res = getResources();

            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();

            if (!conf.locale.getLanguage().equals(languageShortName)) {
                conf.setLocale(myLocale);
                res.updateConfiguration(conf, dm);
                Intent refresh = new Intent(this, MenuActivity.class);
                startActivity(refresh);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis())
            openQuitDialog();
        else
            Toast.makeText(getBaseContext(), R.string.exitMessage,
                    Toast.LENGTH_SHORT).show();
        backPressedTime = System.currentTimeMillis();
    }

    public void onClickStart(View view) {
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void onClickExit(View view) {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                MenuActivity.this);
        quitDialog.setTitle(R.string.exitText);

        quitDialog.setPositiveButton(R.string.yes, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        quitDialog.setNegativeButton(R.string.no, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        quitDialog.show();
    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
