package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.game.graphics.LightSprite;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume on 10/10/14.
 */
public class Light extends Decoration {

    private static final long serialVersionUID = 5948624130900767936L;
    private boolean mIsOn = true;

    public Light(String identifier) {
        super(identifier, 28, 20, 4, 1);
    }

    public boolean isOn() {
        return mIsOn;
    }

    @Override
    public void createSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        sprite = new LightSprite(this, vertexBufferObjectManager);
    }

    public void switchLight() {
        mIsOn = !mIsOn;
        ((LightSprite) sprite).updateSprite(mIsOn);
    }

}
