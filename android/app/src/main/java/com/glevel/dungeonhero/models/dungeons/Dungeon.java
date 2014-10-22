package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.models.characters.Unit;

import org.andengine.extension.tmx.TMXTiledMap;

import java.io.Serializable;
import java.util.List;

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
    private Directions currentDirection;

    public Dungeon(int introText, int outroText, Directions startDirection) {
        // TODO : create random dungeon
        rooms[0][0] = new Room();
        rooms[0][1] = new Room();
        start = 1;
        currentDirection = startDirection;
        currentPosition = start;

        this.introText = introText;
        this.outroText = outroText;
    }

    public Room getCurrentRoom() {
        return rooms[currentPosition / 10][currentPosition % 10];
    }

    public void switchRoom(Tile doorTile) {
        Room previousRoom = getCurrentRoom();
        previousRoom.exit();
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
        currentDirection = doorDirection.getOpposite();
    }

    public void moveIn(TMXTiledMap tmxTiledMap, List<Unit> lstUnitsToMoveIn) {
        Room currentRoom = getCurrentRoom();
        currentRoom.initRoom(tmxTiledMap, this);
        currentRoom.moveIn(lstUnitsToMoveIn, currentDirection);
    }

    public Directions getCurrentDirection() {
        return currentDirection;
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

}
