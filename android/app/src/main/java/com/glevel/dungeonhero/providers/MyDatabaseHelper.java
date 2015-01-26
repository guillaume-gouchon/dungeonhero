package com.glevel.dungeonhero.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.utils.providers.AbstractDatabaseHelper;


public class MyDatabaseHelper extends AbstractDatabaseHelper {

    private static final String DATABASE_NAME = "dungeon_hero";
    private static final int DATABASE_VERSION = 4;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Game.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Game.TABLE_NAME);
        db.execSQL(Game.CREATE_TABLE);
    }

}
