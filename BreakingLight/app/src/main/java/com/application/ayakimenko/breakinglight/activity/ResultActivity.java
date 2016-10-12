package com.application.ayakimenko.breakinglight.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.application.ayakimenko.breakinglight.R;

import static com.application.ayakimenko.breakinglight.constants.Constants.SCORE;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView score = (TextView) findViewById(R.id.scoreView);
        score.setText(String.valueOf(getIntent().getExtras().getInt(SCORE)));
    }

    public void onClickBackToMenu(View view) {
        Intent intent = new Intent(ResultActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickRetry(View view) {
        Intent intent = new Intent(ResultActivity.this, GameActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
