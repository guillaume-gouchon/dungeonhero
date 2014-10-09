package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ActionTile extends Rectangle {

    private static final float ALPHA_ANIMATION_SPEED = 0.003f;
    private static final float ALPHA_ANIMATION_LIMIT = 0.05f;
    private static final float INITIAL_ALPHA = 0.1f;

    private final Tile mTile;

    private boolean mIsGrowing = true;

    public ActionTile(Tile tile, VertexBufferObjectManager vertexBufferObjectManager) {
        super(tile.getTileX() - GameConstants.PIXEL_BY_TILE / 2, tile.getTileY() - GameConstants.PIXEL_BY_TILE / 2, GameConstants.PIXEL_BY_TILE, GameConstants.PIXEL_BY_TILE, vertexBufferObjectManager);
        setColor(0.0f, 1.0f, 0.0f, INITIAL_ALPHA);
        mTile = tile;
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        // alpha animation
        setAlpha(getAlpha() + ALPHA_ANIMATION_SPEED * (mIsGrowing ? 1 : -1));
        if (Math.abs(getAlpha() - INITIAL_ALPHA) > ALPHA_ANIMATION_LIMIT) {
            mIsGrowing = !mIsGrowing;
        }
        super.onManagedUpdate(pSecondsElapsed);
    }

    public Tile getTile() {
        return mTile;
    }

}
