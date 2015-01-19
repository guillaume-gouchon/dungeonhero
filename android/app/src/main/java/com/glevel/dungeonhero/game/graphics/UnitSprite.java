package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.dungeons.Directions;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume ON 10/8/14.
 */
public class UnitSprite extends GameElementSprite {

    public static final int SPRITE_ANIM_X = 3, SPRITE_ANIM_Y = 4;
    private static final int STAND_ANIMATION_DURATION = 300;
    private static final long[] STAND_DURATIONS = new long[]{STAND_ANIMATION_DURATION, STAND_ANIMATION_DURATION, STAND_ANIMATION_DURATION, STAND_ANIMATION_DURATION};
    private static final int WALK_ANIMATION_DURATION = 100;
    private static final long[] WALK_DURATIONS = new long[]{WALK_ANIMATION_DURATION, WALK_ANIMATION_DURATION};

    public UnitSprite(GameElement gameElement, VertexBufferObjectManager vertexBufferObjectManager) {
        super(gameElement, vertexBufferObjectManager);
        setScale(0.5f);
        stand();
    }

    public void walk(Directions direction) {
        stopAnimation();
        animate(WALK_DURATIONS, new int[]{direction.ordinal() * SPRITE_ANIM_X, direction.ordinal() * SPRITE_ANIM_X + 2}, true);
    }

    public void stand() {
        changeOrientation(Directions.SOUTH);
    }

    public void changeOrientation(Directions direction) {
        stopAnimation();
        animate(STAND_DURATIONS, new int[]{direction.ordinal() * SPRITE_ANIM_X, direction.ordinal() * SPRITE_ANIM_X + 1, direction.ordinal() * SPRITE_ANIM_X, direction.ordinal() * SPRITE_ANIM_X + 2}, true);
    }

}
