package com.glevel.dungeonhero.models.dungeons;

import android.util.Log;

import com.glevel.dungeonhero.data.characters.MonsterFactory;
import com.glevel.dungeonhero.data.dungeons.DecorationFactory;
import com.glevel.dungeonhero.data.dungeons.RoomFactory;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.dungeons.decorations.Decoration;
import com.glevel.dungeonhero.models.dungeons.decorations.Light;
import com.glevel.dungeonhero.models.dungeons.decorations.Stairs;

import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Room implements Serializable {

    private static final long serialVersionUID = 4746018928851456729L;

    private static final String TAG = "Room";

    private final String tmxName;
    private Tile[][] tiles = null;
    private transient Map<Directions, Tile> doors;
    private transient List<GameElement> objects;
    private List<Unit> queue = new ArrayList<>();
    private transient boolean isSafe;
    private transient boolean reorder;

    public Room(boolean[][] doors, int yPosition, int xPosition) {
        tmxName = RoomFactory.getRoomDependingOnDoorPositions(doors, yPosition, xPosition);
    }

    public void initRoom(TMXTiledMap tiledMap, Event event, int threatLevel) {
        if (tiles != null && objects == null) {
            // build loaded room
            objects = new ArrayList<>();

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
                        tile.getContent().setTilePosition(tile);
                        objects.add(tile.getContent());
                    }
                }
            }
            tiles = newTiles;
            reorder = false;
        } else if (tiles == null) {
            // create new room
            tiles = new Tile[tiledMap.getTileRows()][tiledMap.getTileColumns()];
            objects = new ArrayList<>();

            // add ground tiles
            TMXLayer groundLayer = tiledMap.getTMXLayers().get(0);
            Tile tile;
            for (TMXTile[] tmxTiles : groundLayer.getTMXTiles()) {
                for (TMXTile tmxTile : tmxTiles) {
                    tile = new Tile(tmxTile, tiledMap);
                    tiles[tmxTile.getTileRow()][tmxTile.getTileColumn()] = tile;
                }
            }

            createRoomContent(event, threatLevel);
            reorder = true;
        }

        doors = new HashMap<>(4);
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

    public Directions getDoorDirection(Tile tile) {
        if (tile.getX() == 0) {
            return Directions.WEST;
        } else if (tile.getX() == getWidth() - 1) {
            return Directions.EAST;
        } else if (tile.getY() == 1) {
            return Directions.NORTH;
        } else {
            return Directions.SOUTH;
        }
    }

    private void createRoomContent(Event event, int threatLevel) {
        int nbFreeTiles = getFreeTilesTotal();

        if (event != null) {
            if (event.isDungeonOver()) {
                addGameElement(new Stairs(true), getRandomFreeTile());
                nbFreeTiles--;
            }

            for (GameElement pnj : event.getPnjs()) {
                addGameElement(pnj, getRandomFreeTile());
                nbFreeTiles--;
            }

            for (GameElement monster : event.getMonsters()) {
                addGameElement(monster, getRandomFreeTile());
                nbFreeTiles--;
            }

            for (Reward reward : event.getRewards()) {
                addGameElement(DecorationFactory.buildSmallChest(reward), getRandomFreeTile());
                nbFreeTiles--;
            }
        }

        if (threatLevel > 0) {
            // add some monsters
            List<Monster> monsters = MonsterFactory.getRoomContent(threatLevel);
            for (Monster monster : monsters) {
                if (nbFreeTiles > 0) {
                    addGameElement(monster, getRandomFreeTile());
                    nbFreeTiles--;
                }
            }

            // add some rewards
            List<Decoration> treasures = DecorationFactory.getRoomContent(threatLevel);
            for (Decoration treasure : treasures) {
                if (nbFreeTiles > 0) {
                    addGameElement(treasure, getRandomFreeTile());
                    nbFreeTiles--;
                }
            }
        }

        for (int n = 1; n < 1 + Math.random() * 2; n++) {
            if (nbFreeTiles > 0) {
                addGameElement(DecorationFactory.buildLight(), getRandomFreeTile());
                nbFreeTiles--;
            }
        }
    }

    private int getFreeTilesTotal() {
        return (tiles.length - 6) * (tiles[0].length - 4) - 1;
    }

    public Tile getRandomFreeTile() {
        Tile freeTile;
        do {
            freeTile = tiles[(int) (3 + Math.random() * (tiles.length - 6))][(int) (2 + Math.random() * (tiles[0].length - 4))];
        }
        while (freeTile.getGround() == null || freeTile.getContent() != null || freeTile.getSubContent().size() > 0);
        return freeTile;
    }

    public void addGameElement(GameElement gameElement, Tile tile) {
        gameElement.setTilePosition(tile);
        if (!queue.contains(gameElement) && gameElement instanceof Unit && !(gameElement instanceof Pnj)) {
            queue.add((Unit) gameElement);
            Log.d(TAG, "queue size =" + queue.size());
        }

        if (!objects.contains(gameElement)) {
            objects.add(gameElement);
        }

        checkSafe();
    }

    public synchronized void removeElement(GameElement gameElement) {
        objects.remove(gameElement);
        if (gameElement instanceof Unit) {
            queue.remove(gameElement);
        }
        checkSafe();
    }

    public synchronized void checkSafe() {
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

    public boolean isVisited() {
        return tiles != null;
    }

    public String getTmxName() {
        return tmxName;
    }

    public int getWidth() {
        return tiles[0].length;
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

    public void moveIn(List<Unit> heroes, Directions from) {
        Log.d(TAG, "moving into room from direction = " + from.name() + ", " + heroes.size() + " units");
        Tile doorPosition = doors.get(from);
        Tile tile = tiles[doorPosition.getY() + from.getDy()][doorPosition.getX() - from.getDx()];
        if (tile.getContent() != null) {
            // move previous content if any
            Log.d(TAG, "there is already something at the entrance");
            tile.getContent().setTilePosition(getRandomFreeTile());
        }

        for (Unit unit : heroes) {
            addGameElement(unit, tile);
            Log.d(TAG, "hero is tile " + tile.getX() + "," + tile.getY());
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

    public void prepareStartRoom(List<Unit> lstUnitsToMoveIn) {
        Tile stairTile = getRandomFreeTile();
        addGameElement(new Stairs(false), stairTile);
        for (Unit unit : lstUnitsToMoveIn) {
            addGameElement(unit, stairTile);
        }
    }

    public boolean hasLight() {
        for (GameElement gameElement : objects) {
            if (gameElement instanceof Light && ((Light) gameElement).isOn()) {
                return true;
            }
        }
        return false;
    }

    public Map<Directions, Tile> getDoors() {
        return doors;
    }

}
