package com.glevel.dungeonhero.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.preference.PreferenceManager;
import android.util.Log;

import com.glevel.dungeonhero.game.GameConstants;

import java.util.Arrays;

public class MusicManager {

    private static final String TAG = "MusicManager";

    private static int[] sCurrentMusic;
    private static MediaPlayer sMediaPlayer;

    public static void playMusic(Context context, int[] musicResources) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (musicResources != null && musicResources.length > 0 && sharedPrefs.getInt(GameConstants.GAME_PREFS_KEY_MUSIC_VOLUME, GameConstants.MusicStates.ON.ordinal()) == GameConstants.MusicStates.ON.ordinal()) {
            play(context, musicResources);
        }
    }

    public static void pauseMusic() {
        if (sMediaPlayer != null) {
            sMediaPlayer.pause();
        }
    }

    private static void play(final Context context, final int[] musicResources) {
        if (Arrays.equals(sCurrentMusic, musicResources)) {
            // already playing this music, resume it
            sMediaPlayer.start();
            return;
        } else if (sCurrentMusic != null) {
            // playing some other music, release it
            release();
        }

        sCurrentMusic = musicResources;
        int musicIndex = (int) (Math.random() * musicResources.length);
        Log.d(TAG, "playing music index = " + musicIndex);
        sMediaPlayer = MediaPlayer.create(context, musicResources[musicIndex]);
        sMediaPlayer.setVolume(0.8f, 0.8f);
        if (sMediaPlayer == null) {
            Log.e(TAG, "player was not created successfully");
        } else {
            try {
                sMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        release();
                        play(context, musicResources);
                    }
                });
                sMediaPlayer.start();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public static void release() {
        Log.d(TAG, "Release music player");
        try {
            if (sMediaPlayer != null) {
                if (sMediaPlayer.isPlaying()) {
                    sMediaPlayer.stop();
                }
                sMediaPlayer.release();
                sMediaPlayer = null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        sCurrentMusic = null;
    }

    public static void playSound(Context context, int sound) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPrefs.getInt(GameConstants.GAME_PREFS_KEY_MUSIC_VOLUME, GameConstants.MusicStates.ON.ordinal()) == GameConstants.MusicStates.ON.ordinal()) {
            Log.d(TAG, "play sound");
            MediaPlayer mp = MediaPlayer.create(context, sound);
            mp.start();
            mp.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
    }

}
