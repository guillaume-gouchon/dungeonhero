package com.glevel.dungeonhero.game.andengine.custom;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class CenteredSprite extends AnimatedSprite {

    public CenteredSprite(float pX, float pY, ITiledTextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        setPosition(pX, pY);
    }

    @Override
    public float getX() {
        return super.getX() + getWidth() / 2.0f;
    }

    @Override
    public float getY() {
        return super.getY() + getHeight() / 2.0f;
    }

    @Override
    public void setPosition(float pX, float pY) {
        super.setPosition(pX - getWidth() / 2.0f, pY - getHeight() / 2.0f);
    }

}
