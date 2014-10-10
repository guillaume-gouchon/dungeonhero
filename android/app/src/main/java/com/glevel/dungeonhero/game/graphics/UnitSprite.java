package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.dungeons.Directions;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume ON 10/8/14.
 */
public class UnitSprite extends GameElementSprite {

    private static final int ANIMATION_DURATION = 320;
    private static final int NB_ANIMATIONS = 3;
    private static final long[] DURATIONS = new long[]{ANIMATION_DURATION, ANIMATION_DURATION, ANIMATION_DURATION, ANIMATION_DURATION};

    public UnitSprite(GameElement gameElement, VertexBufferObjectManager vertexBufferObjectManager) {
        super(gameElement, vertexBufferObjectManager);
        setScale(0.25f);
        setZIndex(10);
        stand();
    }

    public void walk(Directions direction) {
        stopAnimation();
        animate(DURATIONS, new int[]{direction.ordinal() * NB_ANIMATIONS, direction.ordinal() * NB_ANIMATIONS + 1, direction.ordinal() * NB_ANIMATIONS, direction.ordinal() * NB_ANIMATIONS + 2}, true);
    }

    public void stand() {
        walk(Directions.SOUTH);
    }

}
