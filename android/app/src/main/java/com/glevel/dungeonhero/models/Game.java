package com.glevel.dungeonhero.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.glevel.dungeonhero.data.DecorationFactory;
import com.glevel.dungeonhero.data.MonsterFactory;
import com.glevel.dungeonhero.data.PNJFactory;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.MyBaseGameActivity;
import com.glevel.dungeonhero.game.graphics.GraphicHolder;
import com.glevel.dungeonhero.game.graphics.SpriteHolder;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.utils.database.ByteSerializer;
import com.glevel.dungeonhero.utils.database.DatabaseResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Game extends DatabaseResource implements Serializable {

    public static final String TABLE_NAME = "game";
    private static final long serialVersionUID = -840485102400894219L;

    private Book book;
    private List<Integer> booksDone;
    private Hero hero;
    private Dungeon dungeon = null;

    public Game() {
    }

    public static Game fromCursor(Cursor cursor) {
        Game game = new Game();
        game.setId(cursor.getLong(0));
        game.setHero((Hero) ByteSerializer.getObjectFromByte(cursor.getBlob(1)));
        game.setBooksDone((List<Integer>) ByteSerializer.getObjectFromByte(cursor.getBlob(2)));
        if (cursor.getBlob(3) != null) {
            game.setBook((Book) ByteSerializer.getObjectFromByte(cursor.getBlob(3)));
        }
        if (cursor.getBlob(4) != null) {
            game.setDungeon((Dungeon) ByteSerializer.getObjectFromByte(cursor.getBlob(4)));
        }
        return game;
    }

    public void setOnNewSprite(MyBaseGameActivity gameActivity) {
    }

    public void setOnNewSoundToPlay(MyBaseGameActivity gameActivity) {
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues content = new ContentValues(Columns.values().length + 1);
        if (id > 0) {
            content.put(Game.COLUMN_ID, id);
        }
        content.put(Columns.HERO.toString(), ByteSerializer.toByteArray(hero));
        content.put(Columns.BOOKS_DONE.toString(), ByteSerializer.toByteArray((Serializable) booksDone));
        content.put(Columns.BOOK.toString(), ByteSerializer.toByteArray(book));
        content.put(Columns.DUNGEON.toString(), ByteSerializer.toByteArray(dungeon));
        return content;
    }

    public static String[] getDatabaseCreationStatements() {
        return new String[]{
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";",
                "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, " + Columns.HERO + " BLOB, " + Columns.BOOKS_DONE +
                        " BLOB, " + Columns.BOOK + " BLOB, " + Columns.DUNGEON + " BLOB);"
        };
    }

    public List<Integer> getBooksDone() {
        return booksDone;
    }

    public void setBooksDone(List<Integer> booksDone) {
        this.booksDone = booksDone;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void finishBook(Book activeBook) {
        activeBook.setDone(true);
        booksDone.add(activeBook.getBookId());
    }

    private enum Columns {
        HERO("hero"), BOOKS_DONE("books_done"), BOOK("book"), DUNGEON("dungeon");

        private final String columnName;

        private Columns(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public String toString() {
            return columnName;
        }
    }

    public List<GraphicHolder> getGraphicsToLoad() {
        List<GraphicHolder> toLoad = new ArrayList<>();

        // load hero
        toLoad.add(hero);

        // load monsters
        for (GameElement element : MonsterFactory.getAll()) {
            toLoad.add(element);
        }

        // load decorations
        for (GameElement element : DecorationFactory.getAll()) {
            toLoad.add(element);
        }

        // load PNJs
        for (GameElement element : PNJFactory.getAll()) {
            toLoad.add(element);
        }

        toLoad.add(new SpriteHolder("stairs.png", 32, 20, 2, 1));
        toLoad.add(new SpriteHolder("selection.png", 64, 64, 1, 1));
        toLoad.add(new SpriteHolder("blood.png", 300, 50, 6, 1));
        toLoad.add(new SpriteHolder("item_on_ground.png", 12, 12, 1, 1));

        return toLoad;
    }

    public List<String> getSoundEffectsToLoad() {
        List<String> toLoad = new ArrayList<>();

        return toLoad;
    }

}
