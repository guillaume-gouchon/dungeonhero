package com.glevel.dungeonhero.utils.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    private Map<String, Repository<DatabaseResource>> mRepositories;

    public DatabaseHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
        mContext = context;
        mRepositories = new HashMap<String, Repository<DatabaseResource>>();
    }

    public void addRepository(String name, Repository repository) {
        mRepositories.put(name, repository);
    }

    public Repository getRepository(String name) {
        return mRepositories.get(name);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeSQLFile(db, "db.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        executeSQLFile(db, "db.sql");
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

    private void executeSQLFile(SQLiteDatabase db, String fileName) {
        try {
            InputStream is = mContext.getResources().getAssets().open(fileName);
            String[] statements = FileHelper.parseSqlFile(is);
            for (String statement : statements) {
                db.execSQL(statement);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
