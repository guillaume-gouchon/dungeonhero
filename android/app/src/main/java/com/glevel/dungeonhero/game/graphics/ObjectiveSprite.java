package com.glevel.dungeonhero.game.graphics;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.ColorUtils;

import com.glevel.dungeonhero.game.GraphicsFactory;
import com.glevel.dungeonhero.game.data.ArmiesData;

public class ObjectiveSprite extends CenteredSprite {

    private static final float ROTATION_SPEED = 0.07f;
    private static final float SCALE_ANIMATION_SPEED = 0.001f;
    private static final float SCALE_ANIMATION_LIMIT = 0.1f;
    private static final float INITIAL_SCALE = 0.8f;
    private static final float INITIAL_ALPHA = 0.25f;

    private boolean mIsGrowing = true;

    public ObjectiveSprite(float x, float y, ArmiesData owner, VertexBufferObjectManager vertexBufferObjectManager) {
        super(0, 0, GraphicsFactory.mGfxMap.get("objective.png"), vertexBufferObjectManager);
        setPosition(x, y);
        setScale(INITIAL_SCALE);
        setZIndex(2);
        updateColor(owner.getColor());
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        this.setRotation(getRotation() + ROTATION_SPEED);
        this.setScale(getScaleX() + SCALE_ANIMATION_SPEED * (mIsGrowing ? 1 : -1));
        if (Math.abs(getScaleX() - INITIAL_SCALE) > SCALE_ANIMATION_LIMIT) {
            mIsGrowing = !mIsGrowing;
        }
        super.onManagedUpdate(pSecondsElapsed);
    }

    public void updateColor(int argbColor) {
        setColor(ColorUtils.convertARGBPackedIntToColor(argbColor));
        setAlpha(INITIAL_ALPHA);
    }

}
