package com.glevel.dungeonhero.game;

public class GameConstants {

    /**
     * Common settings
     */
    public static final String GAME_PREFS_KEY_MUSIC_VOLUME = "game_music_volume";
    public static final String TUTORIAL_DONE = "tutorial_done";

    public static enum MusicStates {
        OFF, ON
    }

    /**
     * Game settings
     */
    public static final int CAMERA_WIDTH = 800, CAMERA_HEIGHT = 480;
    public static final float CAMERA_ZOOM_MIN = 3.5f, CAMERA_ZOOM_MAX = 3.5f;

    public static final int PIXEL_BY_TILE = 16;

    public static final float GAME_LOOP_FREQUENCY = 60;

}
