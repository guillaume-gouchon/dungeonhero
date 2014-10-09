package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.data.dungeon.GroundTypes;
import com.glevel.dungeonhero.data.dungeon.TerrainTypes;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.utils.pathfinding.Node;

import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;

import java.io.Serializable;

public class Tile extends TMXTile implements Node, Serializable {

    private GameElement content = null;
    private GroundTypes ground = null;
    private TerrainTypes terrain = null;
    private transient TMXTileProperty property;

    public int getAction() {
        return action;
    }

    private int action = 0;


    /**
     * Constructor from a .tmx tile map
     *
     * @param tmxTile
     * @param tiledMap
     */
    public Tile(TMXTile tmxTile, TMXTiledMap tiledMap) {
        super(tmxTile.getGlobalTileID(), tmxTile.getTileColumn(), tmxTile.getTileRow(), tmxTile.getTileWidth(), tmxTile.getTileHeight(), tmxTile.getTextureRegion());

        // add tile properties retrieved from the .tmx
        TMXProperties<TMXTileProperty> lstProperties = tmxTile.getTMXTileProperties(tiledMap);
        if (lstProperties != null) {
            for (TMXTileProperty prop : lstProperties) {
                // setup ground type
                if (prop.getName().equals(GroundTypes.DUNGEON_FLOOR.name())) {
                    ground = GroundTypes.DUNGEON_FLOOR;
                }

                if (prop.getName().equals(TerrainTypes.DOOR.name())) {
                    property = prop;
                    terrain = TerrainTypes.DOOR;
                }
            }
        }
    }

    public GameElement getContent() {
        return content;
    }

    public void setContent(GameElement content) {
        this.content = content;
    }

    public GroundTypes getGround() {
        return ground;
    }

    public void setGround(GroundTypes ground) {
        this.ground = ground;
    }

    public TerrainTypes getTerrain() {
        return terrain;
    }

    public void setTerrain(TerrainTypes terrain) {
        this.terrain = terrain;
    }

    @Override
    public String getId() {
        return getTileRow() + "-" + getTileColumn();
    }

    @Override
    public int getX() {
        return getTileColumn();
    }

    @Override
    public int getY() {
        return getTileRow();
    }

    @Override
    public int getTileX() {
        return (int) ((getTileColumn() + 0.5) * getTileWidth());
    }

    @Override
    public int getTileY() {
        return (int) ((getTileRow() + 0.5) * getTileHeight());
    }

    public void setProperty(TMXTileProperty property) {
        this.property = property;
    }

    public TMXTileProperty getProperty() {
        return property;
    }

    public void setAction(int action) {
        this.action = action;

    }

}
