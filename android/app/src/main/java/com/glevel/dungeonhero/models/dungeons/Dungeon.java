package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.utils.MazeAlgorithm;

import org.andengine.extension.tmx.TMXTiledMap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Dungeon implements Serializable {

    private static final long serialVersionUID = 4765596237193067497L;

    private static final int DUNGEON_WIDTH = 3, DUNGEON_HEIGHT = 3;

    private Room[][] rooms;
    private int start;
    private final int introText;
    private final int outroText;

    private int currentPosition;
    private Directions currentDirection;

    public Dungeon(int introText, int outroText) {
        createRandomDungeon(DUNGEON_WIDTH, DUNGEON_HEIGHT);

        this.introText = introText;
        this.outroText = outroText;
    }

    public void createRandomDungeon(int dungeonWidth, int dungeonHeight) {
        rooms = new Room[dungeonHeight][dungeonWidth];
        boolean[][] doors = MazeAlgorithm.createMaze(dungeonWidth, dungeonHeight);
        for (int i = 0; i < dungeonHeight; i++) {
            for (int j = 0; j < dungeonWidth; j++) {
                rooms[i][j] = new Room(doors, i, j);
            }
        }
        start = (int) (dungeonWidth * Math.random()) + 10 * (int) (dungeonHeight * Math.random());
        currentPosition = start;
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
        if (currentDirection == null) {// heroes just enter the dungeon
            currentDirection = currentRoom.getDoors().keySet().iterator().next();
        }
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
