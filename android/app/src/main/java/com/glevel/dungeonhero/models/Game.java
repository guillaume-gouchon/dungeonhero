package com.glevel.dungeonhero.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.glevel.dungeonhero.data.BookFactory;
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
    private int[] booksDone;
    private Chapter chapter;
    private Hero hero;
    private Dungeon dungeon = null;

    public Game() {
    }

    public static Game fromCursor(Cursor cursor) {
        Game game = new Game();
        game.setId(cursor.getLong(0));
        int bookId = cursor.getInt(1);
        if (bookId > 0) {
            game.startNewBook(BookFactory.getAll().get(bookId - 1));
        }
        game.setChapter((Chapter) ByteSerializer.getObjectFromByte(cursor.getBlob(2)));
        game.setHero((Hero) ByteSerializer.getObjectFromByte(cursor.getBlob(3)));
        if (cursor.getBlob(4) != null) {
            game.setDungeon((Dungeon) ByteSerializer.getObjectFromByte(cursor.getBlob(4)));
        }
        game.setBooksDone((int[]) ByteSerializer.getObjectFromByte(cursor.getBlob(5)));
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
        content.put(Columns.BOOK_ID.toString(), book != null ? book.getBookId() : 0L);
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

    public void startNewBook(Book book) {
        this.book = book;
        this.chapter = book.getChapters().get(0);
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
        BOOK_ID("book_id"), CHAPTER("chapter"), HERO("hero"), DUNGEON("dungeon"), BOOKS_DONE("books_done"), IN_ADVENTURE("in_adventure");

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
        List<GraphicHolder> toLoad = new ArrayList<GraphicHolder>();

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
        List<String> toLoad = new ArrayList<String>();

        return toLoad;
    }

}
