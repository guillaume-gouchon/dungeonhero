package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.base.interfaces.OnUserActionDetected;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.models.dungeons.Directions;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume ON 10/8/14.
 */
public class UnitSprite extends GameElementSprite {

    private static final int ANIMATION_DURATION = 300;
    private static final int NB_ANIMATIONS = 3;

    public UnitSprite(GameElement gameElement, VertexBufferObjectManager vertexBufferObjectManager, OnUserActionDetected userActionListener) {
        super(gameElement, vertexBufferObjectManager, userActionListener);
        setScale(0.25f);
        stand();
    }

    public void walk(Directions direction) {
        stopAnimation();
        animate(new long[]{ANIMATION_DURATION, ANIMATION_DURATION, ANIMATION_DURATION, ANIMATION_DURATION},
                new int[]{direction.ordinal() * NB_ANIMATIONS, direction.ordinal() * NB_ANIMATIONS + 1, direction.ordinal() * NB_ANIMATIONS, direction.ordinal() * NB_ANIMATIONS + 2},
                true);
    }

    public void stand() {
        walk(Directions.SOUTH);
    }

}
