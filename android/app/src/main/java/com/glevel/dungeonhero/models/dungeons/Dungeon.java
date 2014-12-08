package com.glevel.dungeonhero.models.dungeons;

import android.util.Log;

import com.glevel.dungeonhero.models.characters.Hero;
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

    private static final String TAG = "Dungeon";

    private Room[][] rooms;
    private int start;
    private List<Event> events;
    private int nbRoomVisited;

    private final int width, height;

    private int currentPosition;
    private Directions currentDirection;

    public Dungeon(int width, int height, List<Event> events) {
        this.width = width;
        this.height = height;
        nbRoomVisited = 0;
        this.events = events;

        createRandomDungeon();
    }

    public void createRandomDungeon() {
        rooms = new Room[height][width];
        boolean[][] doors = MazeAlgorithm.createMaze(width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rooms[i][j] = new Room(doors, i, j);
            }
        }
        start = (int) (width * Math.random()) + 10 * (int) (height * Math.random());
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
        if (currentRoom.getTiles() == null) {
            nbRoomVisited++;
        }

        int threatLevel = ((Hero) lstUnitsToMoveIn.get(0)).getLevel();
        int nbRoomsLeft = width * height - nbRoomVisited;
        Log.d(TAG, "Number of rooms not visited = " + nbRoomsLeft + ", events = " + events.size());

        Event event = null;
        if (currentDirection == null) {
            // heroes just enter the dungeon
            currentRoom.initRoom(tmxTiledMap, null, threatLevel);

            // add stairs
            currentRoom.prepareStartRoom(lstUnitsToMoveIn);
            return;
        } else if (events.size() > 0 && (nbRoomsLeft < events.size() || Math.random() * 100 < nbRoomVisited * 3)) {
            // the room may contain an event
            event = events.get((int) (events.size() * Math.random()));
            events.remove(event);
        }

        currentRoom.initRoom(tmxTiledMap, event, threatLevel);
        currentRoom.moveIn(lstUnitsToMoveIn, currentDirection);
    }

    public int getStart() {
        return start;
    }

}
