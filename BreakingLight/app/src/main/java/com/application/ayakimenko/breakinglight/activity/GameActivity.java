package com.application.ayakimenko.breakinglight.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.application.ayakimenko.breakinglight.R;
import com.application.ayakimenko.breakinglight.constants.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.INVISIBLE;
import static com.application.ayakimenko.breakinglight.constants.Constants.MAX_LIGHTS_ON;
import static java.util.Arrays.asList;

public class GameActivity extends AppCompatActivity {
    public static final int DEFAULT_TIME_VALUE = 30;

    private int score = 0;
    private int startTime = DEFAULT_TIME_VALUE;
    private boolean gameOver = false;
    private List<View> lives = new ArrayList<>();
    private List<View> onLights = new ArrayList<>();
    private SoundPool mSoundPool;
    private AssetManager mAssetManager;

    private List<Integer> sounds;
    private int notBroken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            createOldSoundPool();
        } else {
            createNewSoundPool();
        }

        mAssetManager = getAssets();

        sounds = asList(
                loadSound("glass1.mp3"),
                loadSound("glass2.mp3"),
                loadSound("glass3.mp3"),
                loadSound("glass4.mp3"),
                loadSound("glass5.mp3")
        );

        notBroken = loadSound("notBroken.mp3");

        lives.add(findViewById(R.id.life1));
        lives.add(findViewById(R.id.life2));
        lives.add(findViewById(R.id.life3));

        startTimer();
        startGame();
    }

    @Override
    public void onBackPressed() {
    }

    private void startGame() {
        List<View> buttons = asList(
                findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3),
                findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6),
                findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9)
        );

        Collections.shuffle(buttons);

        for (View button : buttons) {
            lightsTimer(true, button);
        }
    }

    @SuppressWarnings("deprecation")
    private void createOldSoundPool() {
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
    }

    private void startTimer() {
        final Timer timer = new Timer();
        final Handler uiHandler = new Handler();
        final TextView timerView = (TextView) findViewById(R.id.timer);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final int time = updateTime();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (gameOver || time < 0) {
                            finishGame();
                            timer.cancel();
                        } else {
                            timerView.setText(String.valueOf(time));
                        }
                    }
                });
            }
        }, 10L * 100, 10L * 100);
    }


    private void lightsTimer(final boolean status, final View button) {
        final Timer timer = new Timer();
        final Handler uiHandler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!gameOver) {
                            changeButtonStatus(status, button);
                            lightsTimer(!status, button);
                        }
                    }
                });
            }
        }, getPeriod());
    }

    private void changeButtonStatus(boolean status, View button) {
        if (status && onLights.size() < MAX_LIGHTS_ON) {
            button.setBackground(getResources().getDrawable(R.drawable.light_on));
            onLights.add(button);
        } else {
            if (button.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.light_on).getConstantState())) {
                button.setBackground(getResources().getDrawable(R.drawable.light_off));
            }
            onLights.remove(button);
        }
    }

    private long getPeriod() {
        Random rand = new Random();
        int n = rand.nextInt(startTime + 5);
        return n > 5 ? n * 100 : 5 * 100;
    }

    private int updateTime() {
        startTime--;
        return startTime;
    }

    public void onClickLight(View view) {
        if (view.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.light_on).getConstantState())) {
            view.setBackground(getResources().getDrawable(R.drawable.light_broken));
            score++;
            playSound(sounds.get(new Random().nextInt(4)));

        } else {
            playSound(notBroken);
            if (lives.size() > 0) {
                View life = lives.get(0);
                lives.remove(0);
                life.setVisibility(INVISIBLE);
            } else {
                gameOver = true;
            }
        }
        TextView scoreView = (TextView) findViewById(R.id.score);
        scoreView.setText(String.valueOf(score));
        checkLife();
    }

    private void playSound(int sound) {
        if (sound > 0) {
            mSoundPool.play(sound, 3, 3, 1, 0, 1);
        }
    }

    private void checkLife() {
        boolean noLife = findViewById(R.id.life1).getVisibility() == View.INVISIBLE
                && findViewById(R.id.life2).getVisibility() == View.INVISIBLE
                && findViewById(R.id.life3).getVisibility() == View.INVISIBLE;

        if (noLife) {
            gameOver = true;
        }
    }

    private void finishGame() {
        gameOver = true;
        Intent intent = new Intent(GameActivity.this, ResultActivity.class);
        intent.putExtra(Constants.SCORE, score);
        startActivity(intent);
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(R.string.failedToLoadSong) + fileName,
                    Toast.LENGTH_SHORT).show();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }
}
