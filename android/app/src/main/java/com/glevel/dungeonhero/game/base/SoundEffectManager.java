package com.glevel.dungeonhero.game.base;

import android.content.Context;
import android.util.Log;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.utils.pathfinding.MathUtils;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;

import java.util.HashMap;
import java.util.List;

public class SoundEffectManager {

    private static final String TAG = "SoundManager";
    private static final String ASSETS_PATH = "sfx/";
    private static final String SOUNDS_EXTENSION = ".ogg";
    private static final int EARS_DISTANCE = 100;// in pixels
    private static final int SILENCE_DISTANCE = 3200;// in pixels

    public static HashMap<String, Sound> sMfxMap = new HashMap<String, Sound>();

    private Context mContext;
    private boolean mSoundEnabled;
    private Camera mCamera;
    private Engine mEngine;

    public SoundEffectManager(Context context, int soundState) {
        mContext = context;
        mSoundEnabled = soundState == GameConstants.MusicStates.ON.ordinal();
    }

    public void init(Game game, Engine engine) {
        mEngine = engine;
        sMfxMap = new HashMap<String, Sound>();

        SoundFactory.setAssetBasePath(ASSETS_PATH);

        List<String> soundEffectsToLoad = game.getSoundEffectsToLoad();
        for (String soundEffect : soundEffectsToLoad) {
            loadSfx(soundEffect);
        }
    }


    public void playGeoSound(String soundName, float x, float y) {
        playSound(soundName, false, x, y);
    }

    public void playSound(String soundName, boolean isLooped) {
        playSound(soundName, isLooped, -1, -1);
    }

    private void loadSfx(String soundName) {
        if (sMfxMap.get(soundName) == null) {
            try {
                Sound mSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), mContext, soundName + SOUNDS_EXTENSION);
                sMfxMap.put(soundName, mSound);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    private void playSound(String soundName, boolean isLooped, float x, float y) {
        if (mSoundEnabled) {
            Sound sound = sMfxMap.get(soundName);
            if (sound != null) {
                sound.setLooping(isLooped);

                if (x >= 0 && y >= 0) {
                    // modify volume based ON the distance from the sound to the camera
                    sound.setVolume(getVolumeFromSoundPosition(x, y));
                }

                sound.play();
            }
        }
    }

    private float getVolumeFromSoundPosition(float x, float y) {
        float distance = MathUtils.getDistanceBetween(x, y, mCamera.getCenterX() + EARS_DISTANCE, mCamera.getCenterY());
        return Math.max(0.0f, 1.0f - distance / SILENCE_DISTANCE);
    }

    public void onPause() {
        sMfxMap = new HashMap<String, Sound>();
    }

    public void setCamera(Camera camera) {
        this.mCamera = camera;
    }

}
