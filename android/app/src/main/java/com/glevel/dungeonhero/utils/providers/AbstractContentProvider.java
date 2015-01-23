package com.glevel.dungeonhero.utils.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


/**
 * Created by guillaume on 5/27/14.
 */
public abstract class AbstractContentProvider extends ContentProvider {

    private static final String TAG = "AbstractContentProvider";

    protected AbstractDatabaseHelper dbHelper;
    protected static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query " + uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(getType(uri));
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        cursor.moveToFirst();
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Long resourceId = values.getAsLong(DatabaseResource.COLUMN_ID);
        if (resourceId != null) {
            update(uri, values, DatabaseResource.COLUMN_ID + "=?", new String[]{"" + resourceId});
            return ContentUris.withAppendedId(uri, resourceId);
        } else {
            Log.d(TAG, "insert " + uri);
            SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
            long id = sqlDB.insert(getType(uri), null, values);
            Uri insertedId = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update " + uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsUpdated = sqlDB.update(getType(uri), values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete " + uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = sqlDB.delete(getType(uri), selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

}
