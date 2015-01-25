package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Actions;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class ActionTile extends Rectangle {

    private static final float INACTIVE_ALPHA = 0.2f, ACTIVE_ALPHA = 0.3f;
    private static final float MAX_SCALE = 0.98f, MIN_SCALE = 0.92f;

    private final Tile mTile;
    private final Actions mAction;
    private boolean isScaleGrowing = false;

    public ActionTile(Actions action, Tile tile, VertexBufferObjectManager vertexBufferObjectManager, boolean isEnemyTurn) {
        super(tile.getTileX() - GameConstants.PIXEL_BY_TILE / 2, tile.getTileY() - GameConstants.PIXEL_BY_TILE / 2, GameConstants.PIXEL_BY_TILE, GameConstants.PIXEL_BY_TILE, vertexBufferObjectManager);
        mTile = tile;
        mAction = action;
        setColor(action.getColor());
        if (mAction == Actions.LIGHT) {
            setAlpha(0);
        } else {
            setAlpha(mAction == Actions.MOVE || mAction == Actions.NONE ? INACTIVE_ALPHA : ACTIVE_ALPHA);
        }

        setScale(MAX_SCALE);

        if (isEnemyTurn) {
            setColor(new Color(1.0f, 0.0f, 0.0f));
            setAlpha(0.15f);
        }

    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if (mAction == Actions.MOVE) {
            if (mTile.isSelected()) {
                setAlpha(ACTIVE_ALPHA);
            } else if (getAlpha() == ACTIVE_ALPHA) {
                setAlpha(INACTIVE_ALPHA);
            }
        }

        setScale((float) (getScaleX() + (isScaleGrowing ? 1 : -1) * 0.0012));
        if (getScaleX() <= MIN_SCALE || getScaleX() >= MAX_SCALE) {
            isScaleGrowing = !isScaleGrowing;
        }

        super.onManagedUpdate(pSecondsElapsed);
    }

    public Tile getTile() {
        return mTile;
    }

}
