package com.glevel.dungeonhero.game.graphics;

import android.util.Log;

import com.glevel.dungeonhero.game.GraphicsManager;
import com.glevel.dungeonhero.game.andengine.custom.CenteredSprite;
import com.glevel.dungeonhero.game.models.GameElement;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class GameElementSprite extends CenteredSprite {

    private transient final GameElement mGameElement;
    private transient boolean mIsGrabbed = false;
    private transient boolean mIsSelected = false;
    private transient boolean wasSelected = false;
    private transient boolean canBeDragged = false;

    public GameElementSprite(GameElement gameElement, VertexBufferObjectManager vertexBufferObjectManager) {
        super(gameElement.getTilePosition().getTileX(), gameElement.getTilePosition().getTileY(), GraphicsManager.sGfxMap.get(gameElement.getSpriteName()), vertexBufferObjectManager);
        mGameElement = gameElement;
        mGameElement.setSprite(this);
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                // element is selected
//                wasSelected = mInputManager.selectedElement != null;
//                mInputManager.onSelectGameElement(this);
//                this.setAlpha(0.8f);
//                mIsSelected = true;
                break;
            case TouchEvent.ACTION_MOVE:
                break;
            case TouchEvent.ACTION_UP:
//                if (mIsGrabbed) {
//                    // cancel if small distance
//                    if (Math.abs(pSceneTouchEvent.getX() - getX()) + Math.abs(pSceneTouchEvent.getY() - getY()) > VALID_ORDER_THRESHOLD) {
//                        // give order to unit
//                        mInputManager.giveOrderToUnit(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
//                    }
//                } else if (canBeDragged && wasSelected
//                        && Math.abs(pSceneTouchEvent.getX() - getX()) + Math.abs(pSceneTouchEvent.getY() - getY()) < VALID_ORDER_THRESHOLD) {
//                    mInputManager.giveHideOrder(this);
//                }
//                mInputManager.hideOrderLine();
//                mIsGrabbed = false;
//                if (mIsSelected) {
//                    this.setAlpha(1.0f);
//                    mIsSelected = false;
//                }
                break;
        }
        return true;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
    }

    public GameElement getGameElement() {
        return mGameElement;
    }

}
