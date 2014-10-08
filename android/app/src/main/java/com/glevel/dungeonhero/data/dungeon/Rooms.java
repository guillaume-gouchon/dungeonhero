package com.glevel.dungeonhero.data.dungeon;

/**
 * Created by guillaume ON 10/8/14.
 */
public enum Rooms {
    ROOM1("room1");

    private final String tmxName;

    private Rooms(String tmxName) {
        this.tmxName = tmxName;
    }

    public String getTmxName() {
        return tmxName;
    }

}
