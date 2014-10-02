package com.glevel.dungeonhero.game;

import java.util.ArrayList;

import com.glevel.dungeonhero.game.data.ArmiesData;
import com.glevel.dungeonhero.game.data.BattlesData;
import com.glevel.dungeonhero.game.data.UnitsData;
import com.glevel.dungeonhero.game.models.Battle;
import com.glevel.dungeonhero.game.models.Player;
import com.glevel.dungeonhero.game.models.VictoryCondition;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.game.models.units.categories.Unit.Experience;

public class GameUtils {

	/**
	 * Common settings
	 */
	public static final String GAME_PREFS_FILENAME = "com.glevel.wwii";
	public static final String GAME_PREFS_KEY_DIFFICULTY = "game_difficulty";
	public static final String GAME_PREFS_KEY_MUSIC_VOLUME = "game_music_volume";
	public static final String TUTORIAL_DONE = "tutorial_done";

	public static enum DifficultyLevel {
		easy, medium, hard
	}

	public static enum MusicState {
		off, on
	}

	/**
	 * Armies settings
	 */
	public static final int MAX_UNIT_PER_ARMY = 8;
	public static final float SELL_PRICE_FACTOR = 1.0f;

	/**
	 * Game settings
	 */
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;

	public static final int GAME_LOOP_FREQUENCY = 10;// per second
	public static final int UPDATE_VISION_FREQUENCY = 10;
	public static final int AI_FREQUENCY = 30;
	public static final int CHECK_VICTORY_FREQUENCY = 10;

	public static final int NUMBER_OBJECTIVES_PER_PLAYER = 2;
	public static final int OBJECTIVE_RADIUS_IN_TILE = 2;

	public static final int PIXEL_BY_METER = 15;
	public static final int PIXEL_BY_TILE = 32;

	/**
	 * Game sounds
	 */
	public static final String[] ATMO_SOUNDS = { "cow", "bird" };
	public static final int ATMO_SOUND_FREQUENCY = 50;

	public static Battle createTestData() {
		Battle battle = new Battle(BattlesData.OOSTERBEEK);

		// me
		Player p = new Player("Me", ArmiesData.USA, 0, false,
				new VictoryCondition(100));
		ArrayList<Unit> lstUnits = new ArrayList<Unit>();
		Unit e = UnitsData.buildATCannon(ArmiesData.USA, Experience.ELITE)
				.copy();
		lstUnits.add(e);
		Unit e2 = UnitsData.buildRifleMan(ArmiesData.USA, Experience.VETERAN)
				.copy();
		lstUnits.add(e2);
		p.setUnits(lstUnits);
		battle.getPlayers().add(p);

		// enemy
		p = new Player("Enemy", ArmiesData.GERMANY, 1, true,
				new VictoryCondition(100));
		lstUnits = new ArrayList<Unit>();
		e = UnitsData.buildScout(ArmiesData.GERMANY, Experience.RECRUIT).copy();
		lstUnits.add(e);
		e2 = UnitsData.buildRifleMan(ArmiesData.GERMANY, Experience.VETERAN)
				.copy();
		lstUnits.add(e2);
		e2 = UnitsData.buildRifleMan(ArmiesData.GERMANY, Experience.ELITE)
				.copy();
		lstUnits.add(e2);
		p.setUnits(lstUnits);
		battle.getPlayers().add(p);
		return battle;
	}

}
