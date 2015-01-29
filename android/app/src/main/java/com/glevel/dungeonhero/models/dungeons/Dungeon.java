package com.glevel.dungeonhero.models.dungeons;

import android.os.Bundle;
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

    private static final String TAG = "Dungeon";
    private static final long serialVersionUID = -5750050429857666567L;

    private final int width, height;
    private Room[][] rooms;
    private int start;
    private List<Event> events;
    private int nbRoomVisited;
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

    public void switchRoom(Tile doorTile, List<Unit> heroes) {
        // exit room
        for (Unit unit : heroes) {
            unit.destroy();
            Log.d(TAG, "hero exits room, tile=" + unit.getTilePosition());
        }

        Room previousRoom = getCurrentRoom();
        Directions doorDirection = previousRoom.getDoorDirection(doorTile);
        Log.d(TAG, "switch room to direction = " + doorDirection.name());
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

    public void moveIn(TMXTiledMap tmxTiledMap, List<Unit> heroes) {
        Room currentRoom = getCurrentRoom();
        if (!currentRoom.isVisited()) {
            nbRoomVisited++;
        }

        int threatLevel = ((Hero) heroes.get(0)).getLevel();

        int nbRoomsLeft = width * height - nbRoomVisited;
        Log.d(TAG, "Number of rooms not visited = " + nbRoomsLeft + ", events = " + events.size());

        Event event = null;
        if (isFirstRoom()) {
            // heroes just enter the dungeon
            currentRoom.initRoom(tmxTiledMap, null, 0);

            // add stairs
            currentRoom.prepareStartRoom(heroes);
            return;
        } else if (!currentRoom.isVisited() && events.size() > 0 && (nbRoomsLeft < events.size() || nbRoomVisited > 2 && Math.random() * 100 < nbRoomVisited * 2)) {
            // the room may contain an event
            event = events.get((int) (events.size() * Math.random()));
            events.remove(event);
        }

        currentRoom.initRoom(tmxTiledMap, event, threatLevel);
        currentRoom.moveIn(heroes, currentDirection);
    }

    public boolean isFirstRoom() {
        return currentDirection == null;
    }


    public Room[][] getRooms() {
        return rooms;
    }

}
