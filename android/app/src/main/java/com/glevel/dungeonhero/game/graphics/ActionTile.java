package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Actions;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ActionTile extends Rectangle {

    private static final float INACTIVE_ALPHA = 0.2f, ACTIVE_ALPHA = 0.3f;

    private final Tile mTile;

    public ActionTile(Actions action, Tile tile, VertexBufferObjectManager vertexBufferObjectManager) {
        super(tile.getTileX() - GameConstants.PIXEL_BY_TILE / 2, tile.getTileY() - GameConstants.PIXEL_BY_TILE / 2, GameConstants.PIXEL_BY_TILE, GameConstants.PIXEL_BY_TILE, vertexBufferObjectManager);
        mTile = tile;
        setColor(action.getColor());
        setAlpha(INACTIVE_ALPHA);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if (mTile.isSelected()) {
            setAlpha(ACTIVE_ALPHA);
        } else if (getAlpha() == ACTIVE_ALPHA) {
            setAlpha(INACTIVE_ALPHA);
        }

        super.onManagedUpdate(pSecondsElapsed);
    }

    public Tile getTile() {
        return mTile;
    }

}
