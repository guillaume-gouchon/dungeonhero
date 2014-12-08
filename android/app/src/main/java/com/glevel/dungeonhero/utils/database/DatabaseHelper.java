package com.glevel.dungeonhero.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.glevel.dungeonhero.models.Game;

import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseHelper extends SQLiteOpenHelper {

    private Map<String, Repository<DatabaseResource>> mRepositories;

    public DatabaseHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
        mRepositories = new HashMap<>();
    }

    public void addRepository(String name, Repository repository) {
        mRepositories.put(name, repository);
    }

    public Repository getRepository(String name) {
        return mRepositories.get(name);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabaseFromResources(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createDatabaseFromResources(db);
    }

    /**
     * Called everytime the database is opened by getReadableDatabase or
     * getWritableDatabase. This is called after onCreate or onUpgrade is
     * called.
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    // TODO : refactor
    private void createDatabaseFromResources(SQLiteDatabase db) {
        for (String sqlStatement : Game.getDatabaseCreationStatements()) {
            db.execSQL(sqlStatement);
        }
    }

}
