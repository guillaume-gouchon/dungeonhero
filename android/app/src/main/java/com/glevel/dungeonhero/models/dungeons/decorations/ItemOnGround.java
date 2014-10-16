package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.io.Serializable;

/**
 * Created by guillaume on 10/16/14.
 */
public class ItemOnGround extends Searchable implements Serializable {

    private static final long serialVersionUID = -5951439044612420653L;

    public ItemOnGround(int name, Reward reward) {
        super(name, "item_on_ground.png", reward, 12, 12, 1, 1);
    }

    @Override
    public void setTilePosition(Tile tilePosition) {
        if (this.tilePosition != null) {
            this.tilePosition.setSubContent(null);
        }
        this.tilePosition = tilePosition;
        if (tilePosition != null) {
            tilePosition.setSubContent(this);
        }
    }

    @Override
    public void createSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        super.createSprite(vertexBufferObjectManager);
        sprite.setZIndex(2);
    }
}
