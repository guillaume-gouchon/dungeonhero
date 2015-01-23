package com.glevel.dungeonhero.providers;

import android.net.Uri;

import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.utils.providers.AbstractContentProvider;


public class MyContentProvider extends AbstractContentProvider {

    private static final String AUTHORITY = "com.glevel.dungeonhero";
    private static final String CONTENT_URI = "content://" + AUTHORITY + "/";

    private static final int TYPE_GAMES = 10;
    public static final Uri URI_GAMES = Uri.parse(CONTENT_URI + "games");

    static {
        sURIMatcher.addURI(AUTHORITY, "games", TYPE_GAMES);
        sURIMatcher.addURI(AUTHORITY, "games/#", TYPE_GAMES);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TYPE_GAMES:
                return Game.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
