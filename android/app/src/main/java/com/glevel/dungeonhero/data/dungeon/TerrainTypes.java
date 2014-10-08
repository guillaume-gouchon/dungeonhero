package com.glevel.dungeonhero.data.dungeon;

/**
 * Created by guillaume ON 10/8/14.
 */
public enum TerrainTypes {
    DOOR(false), DECO(true);

    private boolean isBlocking;

    private TerrainTypes(boolean isBlocking) {
        this.isBlocking = isBlocking;
    }

    public boolean isBlocking() {
        return isBlocking;
    }
    
}
