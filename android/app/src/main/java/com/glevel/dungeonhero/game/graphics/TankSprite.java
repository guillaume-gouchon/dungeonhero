package com.glevel.dungeonhero.game.graphics;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.glevel.dungeonhero.game.GraphicsFactory;
import com.glevel.dungeonhero.game.InputManager;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.game.models.weapons.categories.DeflectionWeapon;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public class TankSprite extends UnitSprite {

	// firing
	private transient CenteredSprite turretSprite;
	private transient Sprite mainWeaponMuzzle, secondaryWeaponMuzzle;
	private transient boolean isMainWeaponFiring = false, isSecondaryWeaponFiring = false;

	// movement
	private transient boolean isMoving = false;
	private transient AnimatedSprite moveSmoke;

	public TankSprite(GameElement gameElement, InputManager inputManager, float pX, float pY,
			TiledTextureRegion pTextureRegion, VertexBufferObjectManager mVertexBufferObjectManager) {
		super(gameElement, inputManager, pX, pY, pTextureRegion, mVertexBufferObjectManager);
		addTurretSprite();
		addVehicleSmokeSprite();
		addMuzzleFlashSprites();
		setZIndex(40);
	}

	private void addTurretSprite() {
		turretSprite = new CenteredSprite(0, 0, GraphicsFactory.mGfxMap.get(getGameElement().getSpriteName().replace(
				".png", "")
				+ "_turret.png"), getVertexBufferObjectManager());
		attachChild(turretSprite);
	}

	private void addVehicleSmokeSprite() {
		moveSmoke = new AnimatedSprite(30, 92, GraphicsFactory.mGfxMap.get("tank_move_smoke.png"),
				getVertexBufferObjectManager());
		moveSmoke.setScale(1.0f, 0.8f);
		moveSmoke.setRotation(180);
		moveSmoke.setVisible(false);
		attachChild(moveSmoke);
	}

	private void addMuzzleFlashSprites() {
		secondaryWeaponMuzzle = new Sprite(76, -5, GraphicsFactory.mGfxMap.get("muzzle_flash.png"),
				getVertexBufferObjectManager());
		secondaryWeaponMuzzle.setScale(0.8f);
		secondaryWeaponMuzzle.setVisible(false);
		attachChild(secondaryWeaponMuzzle);

		mainWeaponMuzzle = new Sprite(62, -61, GraphicsFactory.mGfxMap.get("muzzle_flash.png"),
				getVertexBufferObjectManager());
		if (getGameElement().getSpriteName().equals("shermanM4A1.png")) {
			mainWeaponMuzzle.setPosition(65, -65);
		}
		mainWeaponMuzzle.setScale(1.5f);
		mainWeaponMuzzle.setVisible(false);
		turretSprite.attachChild(mainWeaponMuzzle);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (isMainWeaponFiring) {
			mainWeaponMuzzle.setVisible(true);
			isMainWeaponFiring = false;
		} else if (mainWeaponMuzzle.isVisible()) {
			mainWeaponMuzzle.setVisible(false);
		}

		if (isSecondaryWeaponFiring) {
			secondaryWeaponMuzzle.setVisible(true);
			isSecondaryWeaponFiring = false;
		} else if (secondaryWeaponMuzzle.isVisible()) {
			secondaryWeaponMuzzle.setVisible(false);
		}

		if (isMoving && !moveSmoke.isVisible()) {
			moveSmoke.setVisible(true);
			moveSmoke.animate(150, true);
		} else if (!isMoving && moveSmoke.isVisible()) {
			moveSmoke.setVisible(false);
			moveSmoke.stopAnimation();
			isMoving = false;
		}
	}

	@Override
	public void startFireAnimation(Weapon weapon) {
		if (weapon instanceof DeflectionWeapon) {
			isMainWeaponFiring = true;
		} else {
			isSecondaryWeaponFiring = true;
		}
	}

	public float getTurretSpriteRotation() {
		return turretSprite.getRotation();
	}

	public void setTurretSpriteRotation(float angle) {
		turretSprite.setRotation(angle);
	}

	public Sprite getTurretSprite() {
		return turretSprite;
	}

	public void updateMovingAnimation(boolean isMoving) {
		this.isMoving = isMoving;
	}

}
