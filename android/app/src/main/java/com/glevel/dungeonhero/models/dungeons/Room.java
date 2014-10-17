package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.data.DecorationFactory;
import com.glevel.dungeonhero.data.MonsterFactory;
import com.glevel.dungeonhero.data.PNJFactory;
import com.glevel.dungeonhero.data.dungeon.Rooms;
import com.glevel.dungeonhero.data.dungeon.TerrainTypes;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.characters.Unit;

import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Room implements Serializable {

    private static final long serialVersionUID = 4746018928851456729L;

    private final String tmxName;
    private Tile[][] tiles;

    private transient Map<Directions, Tile> doors;
    private transient List<GameElement> objects = new ArrayList<GameElement>();
    private transient List<Unit> queue = new ArrayList<Unit>();
    private transient boolean isSafe;

    public Room() {
        // create random room
        Rooms[] availableRooms = Rooms.values();
        tmxName = availableRooms[(int) (Math.random() * availableRooms.length)].getTmxName();
    }

    public void initRoom(TMXTiledMap tiledMap, Dungeon dungeon) {
        if (tiles != null) {
            // build loaded room
            addContentFromExistingTiles();
        } else {
            // create new room
            doors = new HashMap<Directions, Tile>(4);
            tiles = new Tile[tiledMap.getTileRows()][tiledMap.getTileColumns()];

            // add ground tiles
            TMXLayer groundLayer = tiledMap.getTMXLayers().get(0);
            Tile tile;
            for (TMXTile[] tmxTiles : groundLayer.getTMXTiles()) {
                for (TMXTile tmxTile : tmxTiles) {
                    tile = new Tile(tmxTile, tiledMap);
                    tiles[tmxTile.getTileRow()][tmxTile.getTileColumn()] = tile;
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
                        tile = tiles[tmxTile.getTileRow()][tmxTile.getTileColumn()];
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

            createRoomContent(dungeon);
        }
        checkSafe();
    }

    private void addContentFromExistingTiles() {
        for (Tile[] hTile : tiles) {
            for (Tile tile : hTile) {
                if (tile.getContent() != null) {
                    addGameElement(tile.getContent(), tile);
                }
            }
        }
    }

    private void createRoomContent(Dungeon dungeon) {
        // TODO
        addGameElement(MonsterFactory.buildGoblin(), 5, 5);
        addGameElement(MonsterFactory.buildGoblin(), 6, 6);
        addGameElement(MonsterFactory.buildGoblin(), 8, 8);

        addGameElement(PNJFactory.buildPNJ(), 5, 8);

        addGameElement(DecorationFactory.buildLight(), 5, 10);
        addGameElement(DecorationFactory.buildLight(), 5, 12);

        addGameElement(DecorationFactory.buildTreasureChest(), 7, 5);
    }

    private void addGameElement(GameElement gameElement, int x, int y) {
        addGameElement(gameElement, tiles[x][y]);
    }

    private void addGameElement(GameElement gameElement, Tile tile) {
        gameElement.setTilePosition(tile);
        if (gameElement instanceof Unit && (!(gameElement instanceof Pnj) || ((Pnj) gameElement).isActive())) {
            queue.add((Unit) gameElement);
        }
        objects.add(gameElement);
        checkSafe();
    }

    public void removeElement(GameElement gameElement) {
        objects.remove(gameElement);
        if (gameElement instanceof Unit) {
            queue.remove(gameElement);
        }
        checkSafe();
    }

    private void checkSafe() {
        isSafe = true;
        for (Unit unit : queue) {
            if (unit.getRank() == Ranks.ENEMY) {
                isSafe = false;
                return;
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

    public List<Unit> getQueue() {
        return queue;
    }

    public List<GameElement> getObjects() {
        return objects;
    }

    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean isSafe) {
        this.isSafe = isSafe;
    }

    public Directions getDirectionFromDoorTile(Tile doorTile) {
        // get direction from doors list
        Set<Map.Entry<Directions, Tile>> doorsEntrySet = doors.entrySet();
        Iterator<Map.Entry<Directions, Tile>> doorIterator = doorsEntrySet.iterator();
        Map.Entry<Directions, Tile> doorMap;
        do {
            doorMap = doorIterator.next();
        } while (doorIterator.hasNext() && doorMap.getValue() != doorTile);

        return doorMap.getKey();
    }

    public List<Unit> exit() {
        List<Unit> unitsToMoveAway = new ArrayList<Unit>();
        for (Unit unit : queue) {
            if (unit.getRank() == Ranks.ME || unit.getRank() == Ranks.ALLY) {
                removeElement(unit);
                unit.setTilePosition(null);
                unitsToMoveAway.add(unit);
            }
        }
        return unitsToMoveAway;
    }

    public void moveIn(List<Unit> units, Directions from) {
        // TODO : multiple heroes / allies
        Tile position = doors.get(from);
        for (Unit unit : units) {
            addGameElement(unit, position);
        }
    }

}
