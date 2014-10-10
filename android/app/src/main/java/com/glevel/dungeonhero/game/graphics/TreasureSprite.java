package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.base.GameElement;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume ON 10/8/14.
 */
public class TreasureSprite extends GameElementSprite {

    public TreasureSprite(GameElement gameElement, VertexBufferObjectManager vertexBufferObjectManager) {
        super(gameElement, vertexBufferObjectManager);
        setZIndex(10);
    }

    public void open() {
        setCurrentTileIndex(1);
    }

}
