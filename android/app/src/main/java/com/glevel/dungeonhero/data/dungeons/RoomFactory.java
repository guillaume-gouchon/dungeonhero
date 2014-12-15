package com.glevel.dungeonhero.data.dungeons;

/**
 * Created by guillaume on 12/2/14.
 */
public class RoomFactory {

    public static String getRoomDependingOnDoorPositions(boolean[][] doors, int yPosition, int xPosition) {
        boolean leftDoor = xPosition > 0 && doors[yPosition * 2][xPosition - 1];
        boolean rightDoor = doors[yPosition * 2][xPosition];
        boolean topDoor = yPosition > 0 && doors[yPosition * 2 - 1][xPosition];
        boolean bottomDoor = yPosition < doors.length / 2 && doors[yPosition * 2 + 1][xPosition];

        StringBuilder roomName = new StringBuilder("room-");
        if (topDoor) roomName.append("t");
        if (bottomDoor) roomName.append("b");
        if (leftDoor) roomName.append("l");
        if (rightDoor) roomName.append("r");

        return roomName.toString();
    }

}
