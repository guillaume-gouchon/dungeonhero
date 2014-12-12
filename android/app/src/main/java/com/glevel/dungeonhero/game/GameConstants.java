package com.glevel.dungeonhero.game;

import com.glevel.dungeonhero.R;

public class GameConstants {

    /**
     * Common settings
     */
    public static final String GAME_PREFS_KEY_MUSIC_VOLUME = "game_music_volume";
    public static final String GAME_PREFS_LANDSCAPE = "game_landscape";
    public static final String TUTORIAL_DONE = "tutorial_done";
    /**
     * Game settings
     */
    public static final int CAMERA_WIDTH = 800, CAMERA_HEIGHT = 480;
    public static final float CAMERA_ZOOM_MIN = 3.5f, CAMERA_ZOOM_MAX = 3.5f;
    public static final int PIXEL_BY_TILE = 16;
    public static final float GAME_LOOP_FREQUENCY = 60;

    public static enum MusicStates {
        OFF, ON
    }

    public static int getLevelColor(int level) {
        switch (level) {
            case 0:
                return R.color.bg_level_0;
            case 1:
                return R.color.bg_level_1;
            case 2:
                return R.color.bg_level_2;
            case 3:
                return R.color.bg_level_3;
            case 4:
                return R.color.bg_level_4;
            case 5:
                return R.color.bg_level_5;
            default:
                return R.color.bg_level_6;
        }
    }

}
