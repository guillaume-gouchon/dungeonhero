package com.glevel.dungeonhero.utils.providers;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class AbstractDatabaseHelper extends SQLiteOpenHelper {

    protected AbstractDatabaseHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
    }

}
