package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.models.dungeons.decorations.Stairs;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class StairsSprite extends GameElementSprite {

    public StairsSprite(Stairs stairs, VertexBufferObjectManager vertexBufferObjectManager) {
        super(stairs, vertexBufferObjectManager);
        setScale(2f);
        setZIndex(9);
        if (!stairs.isDownStairs()) {
            setCurrentTileIndex(1);
        }
    }

}
