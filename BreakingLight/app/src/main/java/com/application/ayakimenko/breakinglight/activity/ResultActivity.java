package com.application.ayakimenko.breakinglight.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.ayakimenko.breakinglight.R;
import com.application.ayakimenko.breakinglight.beans.BestPlayer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.application.ayakimenko.breakinglight.constants.Constants.APP_SCORE;
import static com.application.ayakimenko.breakinglight.constants.Constants.APP_SCORE_PLAYER1;
import static com.application.ayakimenko.breakinglight.constants.Constants.APP_SCORE_PLAYER2;
import static com.application.ayakimenko.breakinglight.constants.Constants.APP_SCORE_PLAYER3;
import static com.application.ayakimenko.breakinglight.constants.Constants.SCORE;

public class ResultActivity extends AppCompatActivity {
    public static final String BASE_PLAYER_NAME = ".";
    private SharedPreferences mScore;
    private int userScore = 0;
    private boolean playerInLiderBord = true;

    private EditText player1View;
    private EditText player2View;
    private EditText player3View;

    public static final Gson GSON = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        updateUserScore();
    }

    private void updateUserScore() {
        TextView score = (TextView) findViewById(R.id.scoreView);
        userScore = getIntent().getExtras().getInt(SCORE);
        score.setText(String.valueOf(userScore));

        player1View = (EditText) findViewById(R.id.player1);
        player2View = (EditText) findViewById(R.id.player2);
        player3View = (EditText) findViewById(R.id.player3);

        mScore = getSharedPreferences(APP_SCORE, Context.MODE_PRIVATE);

        List<BestPlayer> bestPlayers = new ArrayList<>();

        bestPlayers.add(getBestPlayer(APP_SCORE_PLAYER1));
        bestPlayers.add(getBestPlayer(APP_SCORE_PLAYER2));
        bestPlayers.add(getBestPlayer(APP_SCORE_PLAYER3));
        bestPlayers.add(new BestPlayer(BASE_PLAYER_NAME, userScore));

        Collections.sort(bestPlayers,
                new Comparator<BestPlayer>() {
                    public int compare(BestPlayer o1, BestPlayer o2) {
                        return o2.getScore() - o1.getScore();
                    }
                }
        );

        bestPlayers.remove(3);

        displayLiederBoard(bestPlayers.get(0), player1View);
        displayLiederBoard(bestPlayers.get(1), player2View);
        displayLiederBoard(bestPlayers.get(2), player3View);
    }

    private void displayLiederBoard(BestPlayer player, EditText view) {
        if (player.getScore() == userScore && BASE_PLAYER_NAME.equals(player.getName()) && playerInLiderBord) {
            playerInLiderBord = false;
            view.setEnabled(true);
            view.setOnKeyListener(onKeyListener(view));
        } else {
            view.setText(player.getDisplayName());
        }
    }

    private BestPlayer getBestPlayer(String key) {
        return GSON.fromJson(mScore.getString(key, GSON.toJson(new BestPlayer())), BestPlayer.class);
    }

    @NonNull
    private View.OnKeyListener onKeyListener(final EditText view) {
        return new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    EditText editText = (EditText) v;
                    String name = editText.getText().toString().replaceAll(" ", "");
                    if (name.length() >= 3) {
                        BestPlayer player = new BestPlayer(name, userScore);
                        updateScoreData(view.getId(), player);
                        editText.setText(player.getDisplayName());
                        editText.setEnabled(false);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.errorNameMessage),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                } else {
                    return false;
                }
            }
        };
    }

    private void updateScoreData(int id, BestPlayer player) {
        SharedPreferences.Editor editor = mScore.edit();
        String userLocation = getUserLocation(id);
        if (userLocation == null) {
            failedToUpdatePlayerScore(player);
        } else {
            editor.putString(userLocation, new Gson().toJson(player));

            if (editor.commit()) {
                Toast.makeText(getApplicationContext(),
                        player.getDisplayName() + getString(R.string.savedSuccess),
                        Toast.LENGTH_SHORT).show();
            } else {
                failedToUpdatePlayerScore(player);
            }
        }

    }

    private void failedToUpdatePlayerScore(BestPlayer player) {
        Toast.makeText(getApplicationContext(),
                getString(R.string.failedToSave) + ": " + player.getDisplayName(),
                Toast.LENGTH_SHORT).show();
    }

    private String getUserLocation(int id) {
        if (id == player1View.getId()) {
            return APP_SCORE_PLAYER1;
        } else if (id == player2View.getId()) {
            return APP_SCORE_PLAYER2;
        } else if (id == player3View.getId()) {
            return APP_SCORE_PLAYER3;
        } else {
            return null;
        }
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
