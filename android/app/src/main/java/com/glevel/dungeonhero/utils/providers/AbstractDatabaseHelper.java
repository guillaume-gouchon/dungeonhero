package com.glevel.dungeonhero.utils.providers;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by guillaume on 5/27/14.
 */
public abstract class AbstractDatabaseHelper extends SQLiteOpenHelper {

    public AbstractDatabaseHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
    }

}
