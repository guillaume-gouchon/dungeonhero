package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.models.dungeons.decorations.TreasureChest;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class TreasureSprite extends GameElementSprite {

    public TreasureSprite(TreasureChest treasureChest, VertexBufferObjectManager vertexBufferObjectManager) {
        super(treasureChest, vertexBufferObjectManager);
        setScale(2.0f);
        if (treasureChest.isEmpty()) {
            open();
        }
    }

    public void open() {
        setCurrentTileIndex(1);
    }

}
