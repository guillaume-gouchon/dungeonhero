package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.Actions;
import com.glevel.dungeonhero.utils.pathfinding.Node;

import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tile extends TMXTile implements Node, Serializable {

    private static final long serialVersionUID = 3793350205301888410L;

    private GameElement content = null;
    private List<GameElement> subContent = new ArrayList<GameElement>();
    private GroundTypes ground = null;
    private transient Actions action;
    private transient boolean isSelected = false;

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
                } else if (prop.getName().equals(GroundTypes.DOOR.name())) {
                    ground = GroundTypes.DOOR;
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

    public void setAction(Actions action) {
        this.action = action;
    }

    public Actions getAction() {
        return action;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public List<GameElement> getSubContent() {
        return subContent;
    }

    public void setSubContent(List<GameElement> subContent) {
        this.subContent = subContent;
    }

}
