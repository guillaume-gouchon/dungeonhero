package com.glevel.dungeonhero.models;

import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Event;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guillaume ON 10/3/14.
 */
public class Chapter implements Serializable {

    private static final long serialVersionUID = 1484748281734643544L;

    private final int introText, outroText;
    private transient Dungeon dungeon;
    private final List<Event> events;
    private boolean done;

    public Chapter(int introText, int outroText, List<Event> events) {
        this.introText = introText;
        this.outroText = outroText;
        this.done = false;
        this.events = events;
    }

    public int getIntroText() {
        return introText;
    }

    public int getOutroText() {
        return outroText;
    }

    public void createDungeon() {
        dungeon = new Dungeon(3, 3, events);
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
