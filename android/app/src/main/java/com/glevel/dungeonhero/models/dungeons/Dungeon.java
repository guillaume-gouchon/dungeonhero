package com.glevel.dungeonhero.models.dungeons;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Dungeon implements Serializable {

    private static final long serialVersionUID = 4765596237193067497L;

    private final Room[][] rooms = new Room[10][10];
    private final int start;
    private final int introText;
    private final int outroText;

    private int currentPosition;
    private boolean isIntroTextAlreadyRead = false;

    public Dungeon(int introText, int outroText) {
        // create random dungeon
        rooms[0][0] = new Room();
        start = 0;
        currentPosition = start;

        this.introText = introText;
        this.outroText = outroText;
    }

    public Room getCurrentRoom() {
        return rooms[currentPosition / 10][currentPosition % 10];
    }

    public int getIntroText() {
        return introText;
    }

    public int getOutroText() {
        return outroText;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getStart() {
        return start;
    }

    public boolean hasToDisplayIntroStory() {
        return currentPosition == start && !isIntroTextAlreadyRead;
    }

    public void setIntroTextAlreadyRead(boolean isIntroTextAlreadyRead) {
        this.isIntroTextAlreadyRead = isIntroTextAlreadyRead;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

}
