package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.GraphicsManager;
import com.glevel.dungeonhero.game.andengine.custom.CenteredSprite;
import com.glevel.dungeonhero.game.base.interfaces.OnUserActionDetected;
import com.glevel.dungeonhero.game.models.GameElement;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class GameElementSprite extends CenteredSprite {

    private transient final GameElement mGameElement;
    private transient final OnUserActionDetected mUserActionListener;

    public GameElementSprite(GameElement gameElement, VertexBufferObjectManager vertexBufferObjectManager, OnUserActionDetected userActionListener) {
        super(gameElement.getTilePosition().getTileX(), gameElement.getTilePosition().getTileY(), GraphicsManager.sGfxMap.get(gameElement.getSpriteName()), vertexBufferObjectManager);
        mGameElement = gameElement;
        mGameElement.setSprite(this);
        mUserActionListener = userActionListener;
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                mUserActionListener.onElementSelected(mGameElement);
                break;
            case TouchEvent.ACTION_MOVE:
                break;
            case TouchEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public GameElement getGameElement() {
        return mGameElement;
    }

}
