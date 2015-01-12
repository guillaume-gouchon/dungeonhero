package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume on 10/16/14.
 */
public class ItemOnGround extends Searchable {

    private static final long serialVersionUID = 16298680039695627L;

    public ItemOnGround(String identifier, Reward reward) {
        super(identifier, reward, 12, 12, 1, 1);
    }

    @Override
    public String getSpriteName() {
        return "item_on_ground.png";
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
        super.createSprite(vertexBufferObjectManager);
        sprite.setZIndex(3);
    }

}
