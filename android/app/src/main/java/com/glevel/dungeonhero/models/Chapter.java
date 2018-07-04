package com.glevel.dungeonhero.models;

import android.content.res.Resources;
import android.util.Log;

import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Event;
import com.glevel.dungeonhero.utils.ApplicationUtils;

import java.io.Serializable;
import java.util.List;

public class Chapter implements Serializable {

    private static final long serialVersionUID = 1484748281734643544L;

    private static final String TAG = "Chapter";

    private final List<Event> initialEvents;
    private final int mDungeonWidth, mDungeonHeight;
    private List<Event> events;
    private int index;
    private String introText;
    private final String outroText;
    private transient Dungeon dungeon;

    public Chapter(String introText, String outroText, List<Event> events, int dungeonWidth, int dungeonHeight) {
        this.introText = introText;
        this.outroText = outroText;
        this.initialEvents = events;
        mDungeonWidth = dungeonWidth;
        mDungeonHeight = dungeonHeight;
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
        dungeon = new Dungeon(mDungeonWidth, mDungeonHeight, events);
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void resetChapter() {
        //noinspection unchecked
        this.events = (List<Event>) ApplicationUtils.deepCopy(initialEvents);
        if (events != null) {
            Log.d(TAG, "reset chapter = " + events.size() + " events");
        }
    }

    public boolean isFirst() {
        return index == 0;
    }

    public List<Event> getEvents() {
        return events;
    }

}
