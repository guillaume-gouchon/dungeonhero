package com.glevel.dungeonhero;

import android.content.Context;
import android.database.Cursor;

import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.utils.database.DatabaseHelper;
import com.glevel.dungeonhero.utils.database.Repository;

/**
 * Created by guillaume ON 10/2/14.
 */
public class MyDatabase extends DatabaseHelper {

    private static final int DB_VERSION = 25;
    private static final String DB_NAME = "dungeon_hero";

    // TODO : refactor
    private Repository<Game> gameRepository = new Repository<Game>(this, Game.TABLE_NAME) {
        @Override
        public Game convertCursorRowToObject(Cursor cursor) {
            return Game.fromCursor(cursor);
        }
    };

    public MyDatabase(Context context) {
        super(context, DB_NAME, DB_VERSION);

        // add repositories
        addRepository(Repositories.GAME.name(), gameRepository);
    }

    public enum Repositories {
        GAME
    }

}
