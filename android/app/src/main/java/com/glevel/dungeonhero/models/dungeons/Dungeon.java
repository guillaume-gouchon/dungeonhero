package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.models.characters.Unit;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Dungeon implements Serializable {

    private static final long serialVersionUID = 4765596237193067497L;

    private final Directions startDirection;

    private final Room[][] rooms = new Room[10][10];
    private final int start;
    private final int introText;
    private final int outroText;

    private int currentPosition;
    private boolean isIntroTextAlreadyRead = false;

    public Dungeon(int introText, int outroText, Directions startDirection) {
        // create random dungeon
        rooms[0][0] = new Room();
        start = 0;
        this.startDirection = startDirection;
        currentPosition = start;

        this.introText = introText;
        this.outroText = outroText;
    }

    public Room getCurrentRoom() {
        return rooms[currentPosition / 10][currentPosition % 10];
    }

    public void moveToOtherRoom(Tile doorTile) {
        Room previousRoom = getCurrentRoom();
        Directions doorDirection = previousRoom.getDirectionFromDoorTile(doorTile);
        switch (doorDirection) {
            case NORTH:
                currentPosition -= 10;
                break;
            case SOUTH:
                currentPosition += 10;
                break;
            case EAST:
                currentPosition++;
                break;
            case WEST:
                currentPosition--;
                break;
        }

        List<Unit> lstUnitsToMoveIn = previousRoom.exit();
        moveIn(lstUnitsToMoveIn, doorDirection.getOpposite());
    }

    public void moveIn(List<Unit> lstUnitsToMoveIn, Directions from) {
        Room currentRoom = getCurrentRoom();
        currentRoom.moveIn(lstUnitsToMoveIn, from);
    }

    public Directions getStartDirection() {
        return startDirection;
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

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
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

}
