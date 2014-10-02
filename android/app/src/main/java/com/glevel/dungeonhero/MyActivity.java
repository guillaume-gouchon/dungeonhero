package com.glevel.dungeonhero;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.utils.MusicManager.Music;

public abstract class MyActivity extends FragmentActivity {

    protected boolean mContinueMusic = false;
    protected Music mMusic = Music.MUSIC_MENU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // allow user to change the music volume with the phone's hardware buttons
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mContinueMusic) {
            MusicManager.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContinueMusic = false;
        if (mMusic != null) {
            MusicManager.start(this, mMusic);
        }
    }

}
