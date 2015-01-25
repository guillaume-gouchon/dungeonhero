package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.models.dungeons.decorations.Light;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume ON 10/8/14.
 */
public class LightSprite extends GameElementSprite {

    private static final int ANIMATION_DURATION = 170;
    private static final long[] DURATIONS = new long[]{ANIMATION_DURATION, ANIMATION_DURATION, ANIMATION_DURATION};

    public LightSprite(Light light, VertexBufferObjectManager vertexBufferObjectManager) {
        super(light, vertexBufferObjectManager);
        setScale(2.0f);
        updateSprite(light.isOn());
    }

    private void animate() {
        animate(DURATIONS, new int[]{0, 1, 2}, true);
    }

    private void turnOff() {
        setCurrentTileIndex(3);
    }

    public void updateSprite(boolean isOn) {
        if (isOn) {
            animate();
        } else {
            stopAnimation();
            turnOff();
        }
    }

}
