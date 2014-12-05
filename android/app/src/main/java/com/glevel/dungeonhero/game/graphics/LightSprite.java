package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.base.GameElement;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume ON 10/8/14.
 */
public class LightSprite extends GameElementSprite {

    private static final int ANIMATION_DURATION = 170;
    private static final long[] DURATIONS = new long[]{ANIMATION_DURATION, ANIMATION_DURATION, ANIMATION_DURATION};

    public LightSprite(GameElement gameElement, VertexBufferObjectManager vertexBufferObjectManager) {
        super(gameElement, vertexBufferObjectManager);
        setZIndex(11);
        animate();
    }

    private void animate() {
        animate(DURATIONS, new int[]{0, 1, 2}, true);
    }

}
