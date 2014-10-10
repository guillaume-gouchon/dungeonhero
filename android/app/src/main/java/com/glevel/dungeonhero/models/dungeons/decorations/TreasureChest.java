package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.game.graphics.TreasureSprite;
import com.glevel.dungeonhero.models.items.Item;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume on 10/10/14.
 */
public class TreasureChest extends Searchable {

    private static final long serialVersionUID = -9160370172054769184L;

    public TreasureChest(int name, String spriteName) {
        super(name, spriteName);
    }

    @Override
    public void createSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        sprite = new TreasureSprite(this, vertexBufferObjectManager);
    }

    @Override
    public Item search() {
        ((TreasureSprite) sprite).open();
        return super.search();
    }

}
