package com.glevel.dungeonhero.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.glevel.dungeonhero.utils.database.DatabaseResource;

/**
 * Created by guillaume on 10/2/14.
 */
public class Game extends DatabaseResource {

    public static final String TABLE_NAME = "game";

    @Override
    public ContentValues getContentValues() {
        return null;
    }

    public static Game fromCursor(Cursor cursor) {
        return null;
    }
}
