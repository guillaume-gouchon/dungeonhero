package com.glevel.dungeonhero.game;

import com.glevel.dungeonhero.R;

public class GameConstants {

    /**
     * Common settings
     */
    public static final String GAME_PREFS_KEY_MUSIC_VOLUME = "game_music_volume";
    public static final String GAME_PREFS_LANDSCAPE = "game_landscape";
    public static final String GAME_PREFS_METAL_MUSIC = "metal_music";
    public static final String TUTORIAL_DONE = "tutorial_done";

    public static enum MusicStates {
        OFF, ON
    }

    /**
     * Game settings
     */
    public static final int CAMERA_WIDTH = 800, CAMERA_HEIGHT = 480;
    public static final float CAMERA_ZOOM_MIN = 1.5f, CAMERA_ZOOM_MAX = 1.5f;
    public static final int PIXEL_BY_TILE = 40;

    public static final String FINISH_GAME_WITH_CHARACTER_PREFS = "victory";
    public static final String FINISH_GAME_PREFS = "final_victory";
    public static final int MAXIMAL_STARS_RATING = 3;
    public static final int NB_QUESTS = 7;
    public static final int HERO_LEVEL_MAX = 6;
    public static final int SKILL_MAX_LEVEL = 6;
    public static final int NB_ITEMS_MAX_IN_BAG = 15;

    public static final float ANIMATED_SPRITE_ALPHA = 0.6f;

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
