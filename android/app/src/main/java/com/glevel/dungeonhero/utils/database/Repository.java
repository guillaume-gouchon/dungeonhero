package com.glevel.dungeonhero.utils.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T extends DatabaseResource> implements IRepository<T> {

    protected SQLiteDatabase mDatabase;

    protected DatabaseHelper dataBaseHelper;

    private String tableName;

    public Repository(DatabaseHelper dataBaseHelper, String tableName) {
        this.tableName = tableName;
        this.dataBaseHelper = dataBaseHelper;
    }

    public void openDatabase() {
        mDatabase = dataBaseHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        mDatabase.close();
    }

    @Override
    public List<T> get(String selection, String[] selectionArgs, String orderBy, String limit) {
        this.openDatabase();
        Cursor cursor = mDatabase.query(tableName, null, selection, selectionArgs, null, null, orderBy, limit);
        return convertCursorToObjectList(cursor);
    }

    @Override
    public List<T> getAll() {
        return get(null, null, null, null);
    }

    @Override
    public T getById(long id) {
        List<T> lst = get(T.COLUMN_ID + "=" + id, null, null, null);
        if (lst != null && lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    }

    @Override
    public long save(T entity) {
        long rowId;
        this.openDatabase();
        if (entity.getId() > 0) {
            rowId = entity.getId();
            mDatabase.update(tableName, getContentValues(entity), T.COLUMN_ID + "=" + entity.getId(), null);
        } else {
            rowId = mDatabase.insert(tableName, null, getContentValues(entity));
        }
        this.closeDatabase();
        return rowId;
    }

    @Override
    public void delete(String selection, String[] selectionArgs) {
        this.openDatabase();
        mDatabase.delete(tableName, selection, selectionArgs);
        this.closeDatabase();
    }

    @Override
    public ContentValues getContentValues(T entity) {
        return entity.getContentValues();
    }

    @Override
    public List<T> convertCursorToObjectList(Cursor c) {
        List<T> list = new ArrayList<>();
        if (c.getCount() == 0) {
            return list;
        }
        while (c.moveToNext()) {
            T entity = convertCursorRowToObject(c);
            list.add(entity);
        }
        c.close();
        closeDatabase();
        return list;
    }

}
