package com.glevel.dungeonhero.utils.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by guillaume on 10/2/14.
 */
public abstract class DatabaseResource {

    public static final String COLUMN_ID = "_id";

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static DatabaseResource fromCursor(Cursor cursor) {
        return null;
    }

    public abstract ContentValues getContentValues();

    public boolean isNew() {
        return id == 0L;
    }

}
