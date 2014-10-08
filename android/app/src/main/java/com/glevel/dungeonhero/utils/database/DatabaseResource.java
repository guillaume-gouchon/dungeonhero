package com.glevel.dungeonhero.utils.database;

import android.content.ContentValues;

/**
 * Created by guillaume ON 10/2/14.
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

    public abstract ContentValues getContentValues();

    public boolean isNew() {
        return id == 0L;
    }

}
