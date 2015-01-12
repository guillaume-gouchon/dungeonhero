package com.glevel.dungeonhero.models;

import android.content.res.Resources;
import android.util.Log;

import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Event;
import com.glevel.dungeonhero.utils.ApplicationUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guillaume ON 10/3/14.
 */
public class Chapter implements Serializable {

    private static final long serialVersionUID = 1484748281734643544L;

    private static final String TAG = "Chapter";

    private static final int DUNGEON_SIZE = 3;

    private final List<Event> initialEvents;

    private List<Event> events;
    private int index;
    private String introText, outroText;
    private transient Dungeon dungeon;
    private boolean done;

    public Chapter(String introText, String outroText, List<Event> events) {
        this.introText = introText;
        this.outroText = outroText;
        this.done = false;
        this.initialEvents = events;
        resetChapter();
    }

    public int getIntroText(Resources resources) {
        return StorableResource.getResource(resources, introText, false);
    }

    public void read() {
        introText = "";
    }

    public int getOutroText(Resources resources) {
        return StorableResource.getResource(resources, outroText, false);
    }

    public void createDungeon() {
        dungeon = new Dungeon(DUNGEON_SIZE, DUNGEON_SIZE, events);
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

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isFirst() {
        return index == 0;
    }

    public void resetChapter() {
        this.events = (List<Event>) ApplicationUtils.deepCopy(initialEvents);
        Log.d(TAG, "reset chapter = " + events.size() + " events");
    }

    public List<Event> getEvents() {
        return events;
    }

}
