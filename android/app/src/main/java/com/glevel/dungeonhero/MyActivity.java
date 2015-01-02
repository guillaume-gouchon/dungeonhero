package com.glevel.dungeonhero;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.glevel.dungeonhero.utils.MusicManager;

public abstract class MyActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // allow user to change the music volume with the phone's hardware buttons
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.playMusic(getApplicationContext(), getMusicResource());
    }

    protected abstract int[] getMusicResource();

}
