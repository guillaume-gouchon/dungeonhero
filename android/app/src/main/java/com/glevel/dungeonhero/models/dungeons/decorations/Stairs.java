package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.game.graphics.StairsSprite;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.io.Serializable;

/**
 * Created by guillaume on 10/16/14.
 */
public class Stairs extends Decoration implements Serializable {

    private static final long serialVersionUID = 3129456613587633463L;

    private final boolean isDownStairs;

    public Stairs(boolean isDownStairs) {
        super(isDownStairs ? "exit_stairs" : "entrance_stairs", 32, 20, 2, 1);
        this.isDownStairs = isDownStairs;
    }

    @Override
    public String getSpriteName() {
        return "stairs.png";
    }

    @Override
    public void setTilePosition(Tile tilePosition) {
        if (this.tilePosition != null) {
            this.tilePosition.getSubContent().remove(this);
        }
        this.tilePosition = tilePosition;
        if (tilePosition != null) {
            tilePosition.getSubContent().add(this);
        }
    }

    @Override
    public void createSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        sprite = new StairsSprite(this, vertexBufferObjectManager);
    }

    public boolean isDownStairs() {
        return isDownStairs;
    }

}
