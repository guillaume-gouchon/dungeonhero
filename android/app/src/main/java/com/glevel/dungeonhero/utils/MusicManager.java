package com.glevel.dungeonhero.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.preference.PreferenceManager;
import android.util.Log;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.game.GameConstants.MusicState;

public class MusicManager {
	private static final String TAG = "MusicManager";

	public static enum Music {
		MUSIC_PREVIOUS(0), MUSIC_MENU(R.raw.menu_music), MUSIC_CAMPAIGN(
				R.raw.campaign_music), MUSIC_GAME(R.raw.game_music);

		private final int resource;

		private Music(int resource) {
			this.resource = resource;
		}

		public int getResource() {
			return resource;
		}
	}

	@SuppressLint("UseSparseArrays")
	private static Map<Music, MediaPlayer> players = new HashMap<Music, MediaPlayer>();
	private static Music currentMusic = null;
	private static Music previousMusic = null;

	public static void start(Context context, Music music) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (sharedPrefs.getInt(GameConstants.GAME_PREFS_KEY_MUSIC_VOLUME,
				MusicState.on.ordinal()) == MusicState.on.ordinal()) {
			start(context, music, false);
		}
	}

	public static void start(Context context, Music music, boolean force) {
		if (!force && currentMusic != null) {
			// already playing some music and not forced to change
			return;
		}
		if (music == Music.MUSIC_PREVIOUS) {
			music = previousMusic;
		}
		if (currentMusic == music) {
			// already playing this music
			return;
		}
		if (currentMusic != null) {
			previousMusic = currentMusic;
			// playing some other music, pause it and change
			pause();
		}
		currentMusic = music;
		MediaPlayer mp = players.get(music);
		if (mp != null) {
			if (!mp.isPlaying()) {
				mp.start();
			}
		} else {
			mp = MediaPlayer.create(context, music.getResource());
			players.put(music, mp);
			if (mp == null) {
				Log.e(TAG, "player was not created successfully");
			} else {
				try {
					mp.setLooping(true);
					mp.start();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
	}

	public static void pause() {
		Collection<MediaPlayer> mps = players.values();
		for (MediaPlayer p : mps) {
			if (p.isPlaying()) {
				p.pause();
			}
		}
		// previousMusic should always be something valid
		if (currentMusic != null) {
			previousMusic = currentMusic;
		}
		currentMusic = null;
	}

	public static void release() {
		Collection<MediaPlayer> mps = players.values();
		for (MediaPlayer mp : mps) {
			try {
				if (mp != null) {
					if (mp.isPlaying()) {
						mp.stop();
					}
					mp.release();
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		mps.clear();
		if (currentMusic != null) {
			previousMusic = currentMusic;
		}
		currentMusic = null;
	}

	public static void playSound(Context context, int sound) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (sharedPrefs.getInt(GameConstants.GAME_PREFS_KEY_MUSIC_VOLUME,
				MusicState.off.ordinal()) == MusicState.on.ordinal()) {
			MediaPlayer mp = MediaPlayer.create(context, sound);
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
					mp = null;
				}
			});
		}
	}

}
