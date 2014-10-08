package com.glevel.dungeonhero.utils.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

public interface IRepository<T> {

    public List<T> get(String selection, String[] selectionArgs, String orderBy, String limit);

    public List<T> getAll();

    public T getById(long id);

    public long save(T entity);

    public void delete(String selection, String[] selectionArgs);

    public List<T> convertCursorToObjectList(Cursor c);

    public ContentValues getContentValues(T entity);

    public T convertCursorRowToObject(Cursor cursor);

}
