package com.application.ayakimenko.breakinglight.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.application.ayakimenko.breakinglight.R;

import java.util.Locale;

public class MenuActivity extends Activity {
    private static long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Locale locale = getResources().getConfiguration().locale;

        if (locale.toString().equals("ru")) {
            Switch languageSwitcher = (Switch) findViewById(R.id.languageSwitcher);
            languageSwitcher.setChecked(true);
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
                finish();
            }
        });

        quitDialog.setNegativeButton(R.string.no, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        quitDialog.show();
    }

    public void changeLanguage(View view) {
        Switch switcher = (Switch) view;
        if (switcher.isChecked()) {
            setLocale("ru");
        } else {
            setLocale("en");
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        conf.setLocale(myLocale);

        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MenuActivity.class);
        startActivity(refresh);
        finish();
    }
}
