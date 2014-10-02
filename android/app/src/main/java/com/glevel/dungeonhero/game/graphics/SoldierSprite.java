package com.glevel.dungeonhero.game.graphics;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.GraphicsFactory;
import com.glevel.dungeonhero.game.InputManager;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.game.models.orders.DefendOrder;
import com.glevel.dungeonhero.game.models.orders.FireOrder;
import com.glevel.dungeonhero.game.models.orders.HideOrder;
import com.glevel.dungeonhero.game.models.orders.MoveOrder;
import com.glevel.dungeonhero.game.models.orders.Order;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public class SoldierSprite extends UnitSprite {

	private transient Sprite mainWeaponMuzzle;
	private transient Sprite hideSprite;
	private transient boolean isFiring;

	private int xMuzzleFlash = 40, yMuzzleFlash = -53;

	public SoldierSprite(GameElement gameElement, InputManager inputManager, float pX, float pY,
			TiledTextureRegion pTextureRegion, VertexBufferObjectManager mVertexBufferObjectManager) {
		super(gameElement, inputManager, pX, pY, pTextureRegion, mVertexBufferObjectManager);
		addHideSprite();
		setZIndex(30);

		if (gameElement.getName() == R.string.panzerschreck || gameElement.getName() == R.string.bazooka) {
			xMuzzleFlash = 44;
			yMuzzleFlash = -40;
		}

		addMuzzleFlashSprite();
	}

	private void addHideSprite() {
		hideSprite = new HideSprite(GraphicsFactory.mGfxMap.get("hide.png"), getVertexBufferObjectManager());
		hideSprite.setVisible(false);
		attachChild(hideSprite);
	}

	private void addMuzzleFlashSprite() {
		mainWeaponMuzzle = new Sprite(xMuzzleFlash, yMuzzleFlash, GraphicsFactory.mGfxMap.get("muzzle_flash.png"),
				getVertexBufferObjectManager());
		mainWeaponMuzzle.setVisible(false);
		attachChild(mainWeaponMuzzle);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (isFiring) {
			mainWeaponMuzzle.setVisible(true);
			isFiring = false;
		} else if (mainWeaponMuzzle.isVisible()) {
			mainWeaponMuzzle.setVisible(false);
		}
	}

	@Override
	public void startFireAnimation(Weapon weapon) {
		isFiring = true;
	}

	@Override
	public void setRotation(float pRotation) {
		hideSprite.setRotation(-pRotation);
		super.setRotation(pRotation);
	}

	public void setOrder(Order order) {
		hideSprite.setVisible(false);
		if (order instanceof MoveOrder) {
			this.animate(new long[] { 250, 250, 250, 250 }, new int[] { 3, 4, 5, 4 }, true);
		} else {
			this.stopAnimation();
			if (order instanceof FireOrder || order instanceof DefendOrder) {
				this.setCurrentTileIndex(0);
			} else if (order instanceof HideOrder) {
				this.setCurrentTileIndex(1);
				hideSprite.setVisible(true);
			} else {
				this.setCurrentTileIndex(4);
			}
		}
	}

	public void died() {
		this.stopAnimation();
		hideSprite.setVisible(false);
		this.setCurrentTileIndex(2);
	}

}
