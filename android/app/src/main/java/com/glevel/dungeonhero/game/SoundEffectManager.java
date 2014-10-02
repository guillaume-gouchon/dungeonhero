package com.glevel.dungeonhero.game;

import java.util.HashMap;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;

import android.content.Context;
import android.util.Log;

import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.game.models.Battle;
import com.glevel.dungeonhero.game.models.Player;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public class SoundEffectManager {

	public static HashMap<String, Sound> mMfxMap = new HashMap<String, Sound>();

	private static final int EARS_DISTANCE = 100;// in pixels
	private static final int SILENCE_DISTANCE = 3200;// in pixels

	private Context mContext;

	private boolean soundEnabled;
	private Camera mCamera;
	private Engine mEngine;

	public SoundEffectManager(Context context, int soundState) {
		this.mContext = context;
		this.soundEnabled = soundState == GameUtils.MusicState.on.ordinal();
	}

	public void init(Battle battle, Engine engine) {
		this.mEngine = engine;
		mMfxMap = new HashMap<String, Sound>();

		SoundFactory.setAssetBasePath("mfx/");

		// load all weapons sounds
		for (Player player : battle.getPlayers()) {
			for (Unit unit : player.getUnits()) {
				for (Weapon weapon : unit.getWeapons()) {
					loadMfxFromAssets(weapon.getSound());
				}
			}
		}

		// load atmosphere sound effects
		for (String atmoSound : GameUtils.ATMO_SOUNDS) {
			loadMfxFromAssets(atmoSound);
		}

		// load all other sound effects
		loadMfxFromAssets("explosion");
		loadMfxFromAssets("death");
		loadMfxFromAssets("clonk");
		// loadMfxFromAssets("need_support");
		// loadMfxFromAssets("incoming");
	}

	private void loadMfxFromAssets(String soundName) {
		if (mMfxMap.get(soundName) == null) {
			try {
				Sound mSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), mContext, soundName + ".ogg");
				mMfxMap.put(soundName, mSound);
			} catch (Exception e) {
				Log.w("SoundManager", e);
			}
		}
	}

	public void playSound(String soundName, float x, float y) {
		if (soundEnabled) {
			Sound sound = mMfxMap.get(soundName);
			if (sound != null) {
				// modify volume based on the distance from the sound to the
				// camera
				sound.setVolume(getVolumeFromSoundPosition(x, y, true));
				sound.play();
			}
		}
	}

	public void playBackgroundSound(String soundName) {
		if (soundEnabled) {
			Sound sound = mMfxMap.get(soundName);
			if (sound != null) {
				sound.setLooping(true);
				sound.play();
			}
		}
	}

	private float getVolumeFromSoundPosition(float x, float y, boolean isLeftEar) {
		float distance = MapLogic.getDistanceBetween(x, y, mCamera.getCenterX() + (isLeftEar ? -1 : 1) * EARS_DISTANCE, mCamera.getCenterY());
		return Math.max(0.0f, 1.0f - distance / SILENCE_DISTANCE);
	}

	public void onPause() {
		mMfxMap = new HashMap<String, Sound>();
	}

	public void setCamera(Camera camera) {
		this.mCamera = camera;
	}

}
