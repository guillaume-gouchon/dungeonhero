package com.glevel.dungeonhero.data.dungeon;

/**
 * Created by guillaume on 12/2/14.
 */
public class RoomFactory {

    public static String getRoomDependingOnDoorPositions(boolean[][] rooms, int xPosition, int yPosition) {
        boolean leftDoor = xPosition > 0 && rooms[yPosition][xPosition - 1];
        boolean topDoor = yPosition > 0 && rooms[yPosition - 1][xPosition];
        boolean rightDoor = xPosition < rooms[0].length && rooms[yPosition][xPosition + 1];
        boolean bottomDoor = yPosition < rooms.length && rooms[yPosition + 1][xPosition];

        StringBuilder roomName = new StringBuilder("room-");
        if (topDoor) roomName.append("t");
        if (bottomDoor) roomName.append("b");
        if (leftDoor) roomName.append("l");
        if (rightDoor) roomName.append("r");

        return roomName.toString();
    }

}
