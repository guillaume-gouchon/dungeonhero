package com.glevel.dungeonhero.models.dungeons;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Dungeon implements Serializable {

    private static final long serialVersionUID = 4765596237193067497L;

    private Room[][] rooms = new Room[10][10];
    private int currentPosition;
    private final int start;

    public Dungeon() {
        // create random dungeon
        rooms[0][0] = new Room();
        start = 0;
        currentPosition = start;
    }

    public Room getCurrentRoom() {
        return rooms[currentPosition / 10][currentPosition % 10];
    }

}
