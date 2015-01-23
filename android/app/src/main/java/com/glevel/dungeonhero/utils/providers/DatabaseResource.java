package com.glevel.dungeonhero.utils.providers;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/2/14.
 */
public abstract class DatabaseResource implements Serializable {

    private static final long serialVersionUID = 9214046938287134784L;
    public static final String COLUMN_ID = "_id";

    protected long id = 0L;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract ContentValues toContentValues();

}
