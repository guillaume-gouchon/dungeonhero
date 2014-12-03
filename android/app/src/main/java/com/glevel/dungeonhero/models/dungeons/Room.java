package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.data.dungeon.GroundTypes;
import com.glevel.dungeonhero.data.dungeon.RoomFactory;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.utils.pathfinding.MathUtils;

import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private Tile[][] tiles = null;

    private transient Map<Directions, Tile> doors;
    private transient List<GameElement> objects = new ArrayList<GameElement>();
    private List<Unit> queue = new ArrayList<Unit>();
    private transient boolean isSafe;
    private transient boolean reorder = true;

    public Room(boolean[][] doors, int yPosition, int xPosition) {
        tmxName = RoomFactory.getRoomDependingOnDoorPositions(doors, yPosition, xPosition);
    }

    public void initRoom(TMXTiledMap tiledMap, Dungeon dungeon) {
        if (tiles != null && objects == null) {
            // build loaded room
            objects = new ArrayList<GameElement>();

            // recreate tiles
            Tile[][] newTiles = new Tile[tiledMap.getTileRows()][tiledMap.getTileColumns()];
            TMXLayer groundLayer = tiledMap.getTMXLayers().get(0);
            Tile tile;
            for (TMXTile[] tmxTiles : groundLayer.getTMXTiles()) {
                for (TMXTile tmxTile : tmxTiles) {
                    tile = new Tile(tmxTile, tiledMap);
                    Tile oldTile = tiles[tmxTile.getTileRow()][tmxTile.getTileColumn()];
                    tile.setContent(oldTile.getContent());
                    tile.setGround(oldTile.getGround());
                    tile.setSubContent(oldTile.getSubContent());
                    newTiles[tmxTile.getTileRow()][tmxTile.getTileColumn()] = tile;

                    if (tile.getContent() != null) {
                        addGameElement(tile.getContent(), tile);
                    }
                }
            }
            tiles = newTiles;
            reorder = false;
        } else if (tiles == null) {
            // create new room
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

            createRoomContent(dungeon);
        }

        doors = new HashMap<Directions, Tile>(4);
        for (Tile[] hTiles : tiles) {
            for (Tile t : hTiles) {
                // add doors
                if (t.getGround() == GroundTypes.DOOR) {
                    doors.put(getDoorDirection(t), t);
                }
            }
        }

        checkSafe();
    }

    private Directions getDoorDirection(Tile tile) {
        int x = tile.getTileColumn();
        int y = tile.getTileRow();
        Set<Tile> adjacentTiles = MathUtils.getAdjacentNodes(tiles, tile, 1, false, null);
        Iterator<Tile> iterator = adjacentTiles.iterator();
        Tile adjacentTile;
        do {
            adjacentTile = iterator.next();
        } while (adjacentTile.getGround() == null && iterator.hasNext());
        return Directions.from(tile.getX() - adjacentTile.getX(), adjacentTile.getY() - tile.getY());
    }

    private void createRoomContent(Dungeon dungeon) {
        // TODO
//        addGameElement(MonsterFactory.buildGoblin(), 5, 5);
//        addGameElement(MonsterFactory.buildGoblin(), 6, 6);
//        addGameElement(MonsterFactory.buildGoblin(), 8, 8);

//        addGameElement(PNJFactory.buildPNJ(), 5, 5);
//        addGameElement(new ItemOnGround(R.string.gold_coins, new Reward(WeaponFactory.buildSword(), 0, 0)), 10, 10);

//        addGameElement(DecorationFactory.buildLight(), 5, 10);
//        addGameElement(DecorationFactory.buildLight(), 5, 12);

//        addGameElement(DecorationFactory.buildTreasureChest(), 4, 4);
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

    public synchronized void removeElement(GameElement gameElement) {
        objects.remove(gameElement);
        if (gameElement instanceof Unit) {
            queue.remove(gameElement);
        }
        checkSafe();
    }

    private synchronized void checkSafe() {
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

    public void exit() {
        for (Unit unit : queue) {
            if (unit.getRank() == Ranks.ME || unit.getRank() == Ranks.ALLY) {
                unit.setTilePosition(null);
            }
        }
    }

    public void moveIn(List<Unit> units, Directions from) {
        // TODO : multiple heroes / allies
        Tile doorPosition = doors.get(from);
        int factor = from == Directions.SOUTH ? 2 : 1;
        Tile tile = tiles[doorPosition.getY() + factor * from.getDy()][doorPosition.getX() - factor * from.getDx()];
        for (Unit unit : units) {
            addGameElement(unit, tile);
        }

        // sort queue by initiative
        if (reorder) {
            Collections.sort(queue, new Comparator<Unit>() {
                @Override
                public int compare(Unit u1, Unit u2) {
                    return -u1.calculateInitiative() + u2.calculateInitiative();
                }
            });
        }
    }

}
