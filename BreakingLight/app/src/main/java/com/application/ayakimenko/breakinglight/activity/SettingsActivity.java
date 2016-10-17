package com.application.ayakimenko.breakinglight.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.application.ayakimenko.breakinglight.R;

import java.util.Locale;

import static com.application.ayakimenko.breakinglight.activity.Helper.getLanguageShortName;
import static com.application.ayakimenko.breakinglight.constants.Constants.APP_PREFERENCES;
import static com.application.ayakimenko.breakinglight.constants.Constants.APP_PREFERENCES_LANGUAGE;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SharedPreferences mSettings;
    private int languageId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Spinner spinner = (Spinner) findViewById(R.id.languageSpinner);
        spinner.setOnItemSelectedListener(this);

        if (mSettings.contains(APP_PREFERENCES_LANGUAGE)) {
            languageId = mSettings.getInt(APP_PREFERENCES_LANGUAGE, 0);
            spinner.setSelection(languageId);
        }
    }

    public void updateLocation(int position) {
        String languageShortName = getLanguageShortName(position);
        Locale myLocale = new Locale(languageShortName);
        Resources res = getResources();

        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if (!conf.locale.getLanguage().equals(languageShortName)) {
            conf.setLocale(myLocale);
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, SettingsActivity.class);
            startActivity(refresh);
            finish();
        }
    }

    public void backToMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_LANGUAGE, position);
        updateLocation(position);

        if (editor.commit()) {
            Toast.makeText(parent.getContext(),
                    getString(R.string.languageTextView) + " : " + parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(parent.getContext(),
                    R.string.failedToChangeLanguage,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
