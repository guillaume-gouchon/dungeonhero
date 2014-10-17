package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.data.DecorationFactory;
import com.glevel.dungeonhero.data.PNJFactory;
import com.glevel.dungeonhero.data.WeaponFactory;
import com.glevel.dungeonhero.data.dungeon.GroundTypes;
import com.glevel.dungeonhero.data.dungeon.Rooms;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.dungeons.decorations.ItemOnGround;
import com.glevel.dungeonhero.utils.pathfinding.MathUtils;

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

    public void initRoom(TMXTiledMap tiledMap, Hero hero, Dungeon dungeon) {
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

                    // add properties
                    if (tile.getGround() == GroundTypes.DOOR) {
                        doors.put(getDoorDirection(tile), tile);
                    }
                }
            }

            for (Tile[] hTiles : tiles) {
                for (Tile t : hTiles) {
                    // add doors
                    if (t.getGround() == GroundTypes.DOOR) {
                        doors.put(getDoorDirection(t), t);
                    }
                }
            }

            createRoomContent(hero, dungeon);
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

    private void addContentFromExistingTiles() {
        for (Tile[] hTile : tiles) {
            for (Tile tile : hTile) {
                if (tile.getContent() != null) {
                    addGameElement(tile.getContent(), tile);
                }
            }
        }
    }

    private void createRoomContent(Hero hero, Dungeon dungeon) {
        // TODO
        addGameElement(hero, doors.get(Directions.NORTH));
//        addGameElement(MonsterFactory.buildGoblin(), 5, 5);
//        addGameElement(MonsterFactory.buildGoblin(), 6, 6);
//        addGameElement(MonsterFactory.buildGoblin(), 8, 8);

        addGameElement(PNJFactory.buildPNJ(), 5, 8);
        addGameElement(new ItemOnGround(R.string.gold_coins, new Reward(WeaponFactory.buildSword(), 0, 0)), 10, 10);

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

}
