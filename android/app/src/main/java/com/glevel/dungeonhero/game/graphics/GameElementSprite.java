package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.andengine.custom.CenteredSprite;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.GraphicsManager;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class GameElementSprite extends CenteredSprite {

    public static final int Y_OFFSET = 12;

    public GameElementSprite(GameElement gameElement, VertexBufferObjectManager vertexBufferObjectManager) {
        super(gameElement.getTilePosition().getTileX(), gameElement.getTilePosition().getTileY(), GraphicsManager.sGfxMap.get(gameElement.getSpriteName()), vertexBufferObjectManager);
        setZIndex(10 + gameElement.getTilePosition().getY());
    }

    @Override
    public float getY() {
        return super.getY() + Y_OFFSET;
    }

    @Override
    public void setPosition(float pX, float pY) {
        super.setPosition(pX, pY - Y_OFFSET);
    }

}
