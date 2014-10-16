package com.glevel.dungeonhero.models;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.dungeons.Dungeon;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/3/14.
 */
public class Chapter implements Serializable {

    private static final long serialVersionUID = 1484748281734643544L;

    private final int name;
    private final int introText, outroText;
    private final transient Dungeon dungeon;
    private final Monster boss;
    private boolean done;

    public Chapter(int name, int introText, int outroText, Monster boss) {
        this.name = name;
        this.introText = introText;
        this.outroText = outroText;
        this.boss = boss;
        this.dungeon = new Dungeon(R.string.about, 0);
        this.done = false;
    }

    public int getName() {
        return name;
    }

    public int getIntroText() {
        return introText;
    }

    public int getOutroText() {
        return outroText;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Monster getBoss() {
        return boss;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
