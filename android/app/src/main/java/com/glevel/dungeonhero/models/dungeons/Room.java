package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.data.dungeon.Rooms;
import com.glevel.dungeonhero.data.dungeon.TerrainTypes;

import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Room implements Serializable {


    private final String tmxName;
    private Tile[][] tiles;
    private transient Map<Directions, Tile> doors;

    public Room() {
        // create random room
        Rooms[] availableRooms = Rooms.values();
        tmxName = availableRooms[(int) (Math.random() * availableRooms.length)].getTmxName();
    }

    public void initRoom(TMXTiledMap tiledMap) {
        doors = new HashMap<Directions, Tile>(4);
        tiles = new Tile[tiledMap.getTileRows()][tiledMap.getTileColumns()];

        // add ground tiles
        TMXLayer groundLayer = tiledMap.getTMXLayers().get(0);
        for (TMXTile[] tmxTiles : groundLayer.getTMXTiles()) {
            for (TMXTile tmxTile : tmxTiles) {
                tiles[tmxTile.getTileRow()][tmxTile.getTileColumn()] = new Tile(tmxTile, tiledMap);
            }
        }

        // add objects and doors
        TMXLayer objectsLayer = tiledMap.getTMXLayers().get(1);
        for (TMXTile[] tmxTiles : objectsLayer.getTMXTiles()) {
            for (TMXTile tmxTile : tmxTiles) {
                Tile objectTile = new Tile(tmxTile, tiledMap);
                if (objectTile.getTerrain() != null) {
                    if (tiles[tmxTile.getTileRow()][tmxTile.getTileColumn()] == null) {
                        tiles[tmxTile.getTileRow()][tmxTile.getTileColumn()] = objectTile;
                    }
                    Tile tile = tiles[tmxTile.getTileRow()][tmxTile.getTileColumn()];
                    tile.setTerrain(objectTile.getTerrain());

                    // add properties
                    if (objectTile.getProperty() != null) {
                        tile.setProperty(objectTile.getProperty());

                        if (tile.getTerrain() == TerrainTypes.DOOR) {
                            doors.put(Directions.values()[Integer.parseInt(tile.getProperty().getValue())], tile);
                        }
                    }
                }
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public String getTmxName() {
        return tmxName;
    }

    public int getWidth() {
        return tiles[0].length;
    }

    public int getHeight() {
        return tiles.length;
    }

    public Map<Directions, Tile> getDoors() {
        return doors;
    }


}
