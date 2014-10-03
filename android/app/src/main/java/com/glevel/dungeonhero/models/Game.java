package com.glevel.dungeonhero.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.glevel.dungeonhero.utils.database.ByteSerializer;
import com.glevel.dungeonhero.utils.database.DatabaseResource;

/**
 * Created by guillaume on 10/2/14.
 */
public class Game extends DatabaseResource {

    public static final String TABLE_NAME = "game";

    private enum Columns {
        BOOK("book"), CHAPTER("chapter"), PLAYER("player"), DUNGEON("dungeon");

        private final String columnName;

        private Columns(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public String toString() {
            return columnName;
        }
    }

    private Book book;
    private Chapter chapter;
    private Player player;
    private Dungeon dungeon;

    @Override
    public ContentValues getContentValues() {
        ContentValues content = new ContentValues(Columns.values().length + 1);
        content.put(Game.COLUMN_ID, id);
        content.put(Columns.BOOK.toString(), ByteSerializer.toByteArray(book));
        content.put(Columns.CHAPTER.toString(), ByteSerializer.toByteArray(chapter));
        content.put(Columns.PLAYER.toString(), ByteSerializer.toByteArray(player));
        content.put(Columns.DUNGEON.toString(), ByteSerializer.toByteArray(dungeon));
        return content;
    }

    public static Game fromCursor(Cursor cursor) {
        Game game = new Game();
        game.setId(cursor.getLong(0));
        game.setBook((Book) ByteSerializer.getObjectFromByte(cursor.getBlob(1)));
        game.setChapter((Chapter) ByteSerializer.getObjectFromByte(cursor.getBlob(2)));
        game.setPlayer((Player) ByteSerializer.getObjectFromByte(cursor.getBlob(3)));
        game.setDungeon((Dungeon) ByteSerializer.getObjectFromByte(cursor.getBlob(4)));
        return game;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }
    
}
