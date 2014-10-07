package com.glevel.dungeonhero.game;

public class GameConstants {

    /**
     * Common settings
     */
    public static final String GAME_PREFS_KEY_MUSIC_VOLUME = "game_music_volume";
    public static final String TUTORIAL_DONE = "tutorial_done";

    public static enum MusicState {
        off, on
    }

    /**
     * Game settings
     */
    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 480;

    public static final int PIXEL_BY_TILE = 16;
}
