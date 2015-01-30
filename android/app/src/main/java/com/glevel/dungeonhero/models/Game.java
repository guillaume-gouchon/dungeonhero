package com.glevel.dungeonhero.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.data.characters.MonsterFactory;
import com.glevel.dungeonhero.data.dungeons.DecorationFactory;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.graphics.GraphicHolder;
import com.glevel.dungeonhero.game.graphics.SpriteHolder;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.utils.providers.ByteSerializerHelper;
import com.glevel.dungeonhero.utils.providers.DatabaseResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Game extends DatabaseResource {

    public static final String TABLE_NAME = "game";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, " + Columns.HERO + " BLOB, " + Columns.BOOKS_DONE +
            " BLOB, " + Columns.BOOK + " BLOB, " + Columns.DUNGEON + " BLOB);";

    private Book book;
    private Map<Integer, Integer> booksDone = new HashMap<>();
    private Hero hero;
    private Dungeon dungeon = null;

    public static Game fromCursor(Cursor cursor) {
        Game game = new Game();
        game.setId(cursor.getLong(0));

        if (cursor.getColumnCount() > 1) {
            game.setHero((Hero) ByteSerializerHelper.getObjectFromByte(cursor.getBlob(1)));
            if (cursor.getColumnCount() > 2) {
                game.setBooksDone((Map<Integer, Integer>) ByteSerializerHelper.getObjectFromByte(cursor.getBlob(2)));
                if (cursor.getBlob(3) != null) {
                    game.setBook((Book) ByteSerializerHelper.getObjectFromByte(cursor.getBlob(3)));
                }
                if (cursor.getBlob(4) != null) {
                    game.setDungeon((Dungeon) ByteSerializerHelper.getObjectFromByte(cursor.getBlob(4)));
                }
            }
        }

        return game;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues content = new ContentValues(Columns.values().length + 1);
        if (id > 0) {
            content.put(Game.COLUMN_ID, id);
        }
        content.put(Columns.HERO.toString(), ByteSerializerHelper.toByteArray(hero));
        content.put(Columns.BOOKS_DONE.toString(), ByteSerializerHelper.toByteArray((Serializable) booksDone));
        content.put(Columns.BOOK.toString(), ByteSerializerHelper.toByteArray(book));
        content.put(Columns.DUNGEON.toString(), ByteSerializerHelper.toByteArray(dungeon));
        return content;
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

    public void finishBook() {
        // update score if better
        if (booksDone.get(book.getId()) == null || book.getCurrentScore() > booksDone.get(book.getId())) {
            booksDone.put(book.getId(), book.getCurrentScore());
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
        for (GraphicHolder element : book.getResourcesToLoad()) {
            toLoad.add(element);
        }

        toLoad.add(new SpriteHolder("stairs.png", 30, 30, 2, 1));
        toLoad.add(new SpriteHolder("selection.png", 64, 64, 1, 1));
        toLoad.add(new SpriteHolder("blood.png", 300, 50, 6, 1));
        toLoad.add(new SpriteHolder("item_on_ground.png", 18, 16, 1, 1));
        toLoad.add(new SpriteHolder("slash.png", 250, 50, 5, 1));
        toLoad.add(new SpriteHolder("poison.png", 300, 50, 6, 1));
        toLoad.add(new SpriteHolder("frost.png", 300, 50, 6, 1));
        toLoad.add(new SpriteHolder("curse.png", 300, 50, 6, 1));
        toLoad.add(new SpriteHolder("elec.png", 300, 50, 6, 1));
        toLoad.add(new SpriteHolder("ground_slam.png", 300, 50, 6, 1));
        toLoad.add(new SpriteHolder("charm.png", 300, 50, 6, 1));
        toLoad.add(new SpriteHolder("fireball.png", 200, 100, 4, 2));

        return toLoad;
    }

    public List<String> getSoundEffectsToLoad() {
        List<String> toLoad = new ArrayList<>();

        toLoad.add("block");
        toLoad.add("close_combat_attack");
        toLoad.add("coins");
        toLoad.add("damage_hero");
        toLoad.add("damage_monster");
        toLoad.add("death");
        toLoad.add("magic");
        toLoad.add("new_level");
        toLoad.add("range_attack");
        toLoad.add("search");

        return toLoad;
    }

    public Map<Integer, Integer> getBooksDone() {
        return booksDone;
    }

    public void setBooksDone(Map<Integer, Integer> booksDone) {
        this.booksDone = booksDone;
    }

    public enum Columns {
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

}
