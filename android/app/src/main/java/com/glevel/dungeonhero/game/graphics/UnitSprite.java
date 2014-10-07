package com.glevel.dungeonhero.game.graphics;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.glevel.dungeonhero.game.InputManager;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public abstract class UnitSprite extends CenteredSprite {

	private transient static final int ACTION_MOVE_THRESHOLD = 80;
	private transient static final int VALID_ORDER_THRESHOLD = 80;

	private transient final GameElement mGameElement;
	private transient InputManager mInputManager;
	private transient boolean mIsGrabbed = false;
	private transient boolean mIsSelected = false;
	private transient boolean wasSelected = false;
	private transient boolean canBeDragged = false;

	public UnitSprite(GameElement gameElement, InputManager inputManager, float pX, float pY, ITiledTextureRegion pTextureRegion,
			VertexBufferObjectManager mVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, mVertexBufferObjectManager);
		mGameElement = gameElement;
		mInputManager = inputManager;
		setScale(mGameElement.getSpriteScale());
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			// element is selected
			wasSelected = mInputManager.selectedElement != null;
			mInputManager.onSelectGameElement(this);
			this.setAlpha(0.8f);
			mIsSelected = true;
			break;
		case TouchEvent.ACTION_MOVE:
			if (canBeDragged && mIsSelected && !mIsGrabbed
					&& (Math.abs(pTouchAreaLocalX) + Math.abs(pTouchAreaLocalY) > ACTION_MOVE_THRESHOLD || mInputManager.isDeploymentPhase())) {
				// element is dragged
				mIsGrabbed = true;
			}
			if (mIsSelected && mIsGrabbed) {
				mInputManager.updateOrderLine(this, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			}
			mInputManager.setmLastX(pSceneTouchEvent.getMotionEvent().getX());
			mInputManager.setmLastY(pSceneTouchEvent.getMotionEvent().getY());
			break;
		case TouchEvent.ACTION_UP:
			if (mIsGrabbed) {
				// cancel if small distance
				if (Math.abs(pSceneTouchEvent.getX() - getX()) + Math.abs(pSceneTouchEvent.getY() - getY()) > VALID_ORDER_THRESHOLD) {
					// give order to unit
					mInputManager.giveOrderToUnit(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				}
			} else if (canBeDragged && wasSelected
					&& Math.abs(pSceneTouchEvent.getX() - getX()) + Math.abs(pSceneTouchEvent.getY() - getY()) < VALID_ORDER_THRESHOLD) {
				mInputManager.giveHideOrder(this);
			}
			mInputManager.hideOrderLine();
			mIsGrabbed = false;
			if (mIsSelected) {
				this.setAlpha(1.0f);
				mIsSelected = false;
			}
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

	public void setCanBeDragged(boolean canBeDragged) {
		this.canBeDragged = canBeDragged;
	}

	@Override
	public void setPosition(float pX, float pY) {
		mGameElement.setCurrentX(pX);
		mGameElement.setCurrentY(pY);
		super.setPosition(pX, pY);
	}

	@Override
	public void setRotation(float pRotation) {
		mGameElement.setCurrentRotation(pRotation);
		super.setRotation(pRotation);
	}

	public abstract void startFireAnimation(Weapon weapon);

}
