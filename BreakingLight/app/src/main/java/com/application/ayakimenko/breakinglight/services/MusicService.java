package com.application.ayakimenko.breakinglight.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.application.ayakimenko.breakinglight.R;

import static com.application.ayakimenko.breakinglight.constants.Constants.APP_PREFERENCES;

public class MusicService extends Service {
    private MediaPlayer mMediaPlayer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MediaPlayer.create(this, R.raw.background);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setVolume((float) 0.5, (float) 0.5);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (preferences.getBoolean("music", true)) {
            mMediaPlayer.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.stop();
    }
}
