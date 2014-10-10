package com.glevel.dungeonhero.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.game.base.CustomGameActivity;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.utils.database.ByteSerializer;
import com.glevel.dungeonhero.utils.database.DatabaseResource;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Game extends DatabaseResource implements Serializable {

    public static final String TABLE_NAME = "game";
    private static final long serialVersionUID = -840485102400894219L;
    
    private Book book;
    private int[] booksDone;
    private Chapter chapter;
    private Hero hero;
    private Dungeon dungeon;

    public Game() {
    }

    public Game(Hero hero, Book book) {
        this.id = 0L;
        this.hero = hero;
        this.book = book;
        this.chapter = book.getChapters().get(0);
        this.dungeon = chapter.getDungeon();
    }

    public static Game fromCursor(Cursor cursor) {
        Game game = new Game();
        game.setId(cursor.getLong(0));
        int bookId = cursor.getInt(1);
        if (bookId > 0) {
            game.setBook(BookFactory.getBooks().get(bookId - 1));
        }
        game.setChapter((Chapter) ByteSerializer.getObjectFromByte(cursor.getBlob(2)));
        game.setHero((Hero) ByteSerializer.getObjectFromByte(cursor.getBlob(3)));
        game.setDungeon((Dungeon) ByteSerializer.getObjectFromByte(cursor.getBlob(4)));
        game.setBooksDone((int[]) ByteSerializer.getObjectFromByte(cursor.getBlob(5)));
        return game;
    }

    public void setOnNewSprite(CustomGameActivity gameActivity) {
    }

    public void setOnNewSoundToPlay(CustomGameActivity gameActivity) {
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues content = new ContentValues(Columns.values().length + 1);
        if (id > 0) {
            content.put(Game.COLUMN_ID, id);
        }
        content.put(Columns.BOOK_ID.toString(), book.getBookId());
        content.put(Columns.CHAPTER.toString(), ByteSerializer.toByteArray(chapter));
        content.put(Columns.HERO.toString(), ByteSerializer.toByteArray(hero));
        content.put(Columns.DUNGEON.toString(), ByteSerializer.toByteArray(dungeon));
        content.put(Columns.BOOKS_DONE.toString(), ByteSerializer.toByteArray(booksDone));
        return content;
    }

    public int[] getBooksDone() {
        return booksDone;
    }

    public void setBooksDone(int[] booksDone) {
        this.booksDone = booksDone;
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

    private enum Columns {
        BOOK_ID("book_id"), CHAPTER("chapter"), HERO("hero"), DUNGEON("dungeon"), BOOKS_DONE("books_done");

        private final String columnName;

        private Columns(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public String toString() {
            return columnName;
        }
    }

}
