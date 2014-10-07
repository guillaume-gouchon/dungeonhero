package com.glevel.dungeonhero.game.models.units.categories;

import java.util.ArrayList;
import java.util.List;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.game.data.ArmiesData;
import com.glevel.dungeonhero.game.graphics.TankSprite;
import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.game.logic.pathfinding.Node;
import com.glevel.dungeonhero.game.models.Battle;
import com.glevel.dungeonhero.game.models.Battle.Phase;
import com.glevel.dungeonhero.game.models.map.Map;
import com.glevel.dungeonhero.game.models.map.Tile;
import com.glevel.dungeonhero.game.models.map.Tile.TerrainType;
import com.glevel.dungeonhero.game.models.orders.DefendOrder;
import com.glevel.dungeonhero.game.models.orders.FireOrder;
import com.glevel.dungeonhero.game.models.orders.MoveOrder;
import com.glevel.dungeonhero.game.models.orders.Order;
import com.glevel.dungeonhero.game.models.units.Soldier;
import com.glevel.dungeonhero.game.models.weapons.Turret;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public abstract class Vehicle extends Unit {

	/**
     * 
     */
	private static final long serialVersionUID = -110116920924422447L;

	// price
	private static final int VEHICLE_BASE_PRICE = 15;
	private static final float RECRUIT_PRICE_MODIFIER = 0.7f;
	private static final float VETERAN_PRICE_MODIFIER = 1.0f;
	private static final float ELITE_PRICE_MODIFIER = 1.5f;

	// movement
	private static final int REVERSE_THRESHOLD = 20 * GameConstants.PIXEL_BY_METER;// in
																				// meters
	private static final float REVERSE_SPEED = 0.5f;
	private static final float ROTATION_SPEED = 0.6f;
	private transient List<Tile> realTilesPosition = new ArrayList<Tile>();
	private static final float VEHICLE_RADIUS_SIZE = 2.0f;// in meters

	// fire
	private static final float MG_MAX_FIRE_ANGLE = 25.0f;

	// private float nextX = -1.0f, nextY = -1.0f;

	protected static enum VehicleType {
		LIGHT, TANK
	}

	public Vehicle(ArmiesData army, int name, int image, Experience experience, List<Weapon> weapons, int moveSpeed, VehicleType type, int armor, int width,
			int height, String spriteName, float spriteScale) {
		super(army, name, image, experience, weapons, moveSpeed, spriteName, spriteScale);
		this.vehicleType = type;
		this.armor = armor;
		this.width = width;
		this.height = height;
		setOrder(new DefendOrder());
	}

	private final VehicleType vehicleType;
	private final int width;
	private final int height;
	protected int armor;

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public int getPrice() {
		int price = VEHICLE_BASE_PRICE;

		// add weapons price
		for (Weapon weapon : getWeapons()) {
			price += weapon.getPrice();
		}

		// experience modifier
		switch (getExperience()) {
		case RECRUIT:
			price *= RECRUIT_PRICE_MODIFIER;
			break;
		case VETERAN:
			price *= VETERAN_PRICE_MODIFIER;
			break;
		case ELITE:
			price *= ELITE_PRICE_MODIFIER;
			break;
		}

		// armor + movement speed modifiers
		price += (armor + moveSpeed) * 3;

		return price;
	}

	@Override
	public float getUnitTerrainProtection() {
		return 1.0f;
	}

	@Override
	public float getUnitSpeed() {

		if (getHealth() == InjuryState.BADLYINJURED) {
			return 0;
		}

		// depends on health
		float healthFactor = 1 - getHealth().ordinal() * 0.5f;

		// depends on terrain
		switch (getTilePosition().getGround()) {
		case concrete:
			return 1.0f * healthFactor;
		case grass:
			return 0.7f * healthFactor;
		case mud:
			return 0.4f * healthFactor;
		case water:
			return 0.1f * healthFactor;
		}

		return 0;
	}

	@Override
	public boolean canMove() {
		if (getHealth() == InjuryState.BADLYINJURED) {
			return false;
		}
		return super.canMove();
	}

	@Override
	public boolean canMoveIn(Node node) {
		Tile tile = (Tile) node;
		if (tile.getTerrain() != null && tile.getTerrain() != TerrainType.field && tile.getTerrain() != TerrainType.bush
				&& tile.getTerrain() != TerrainType.tree) {
			return false;
		}

		return super.canMoveIn(tile);
	}

	@Override
	public void updateMovementPath(Map map) {
		// if (nextX < 0) {
		// MoveOrder moveOrder = (MoveOrder) getOrder();
		//
		// Tile destinationTile = MapLogic.getTileAtCoordinates(map,
		// moveOrder.getXDestination(), moveOrder.getYDestination());
		// Tile originTile = MapLogic.getTileAtCoordinates(map, sprite.getX(),
		// sprite.getY());
		// List<Tile> path = new AStar<Tile>().search(map.getTiles(),
		// originTile, destinationTile, true, this, 30);
		//
		// if (path != null
		// && path.size() > 1
		// && path.size() - 5 > Math.abs(destinationTile.getTileColumn()
		// - originTile.getTileColumn())
		// + Math.abs(destinationTile.getTileRow()
		// - originTile.getTileRow())) {
		// nextX = path.get(path.size() / 2 - 1).getX()
		// * GameUtils.PIXEL_BY_TILE;
		// nextY = path.get(path.size() / 2 - 1).getY()
		// * GameUtils.PIXEL_BY_TILE;
		// }
		// }
	}

	@Override
	public void setOrder(Order order) {
		// nextX = -1.0f;
		// nextY = -1.0f;
		if (!isDead() && sprite != null) {
			if (order instanceof MoveOrder) {
				((TankSprite) sprite).updateMovingAnimation(true);
			} else {
				((TankSprite) sprite).updateMovingAnimation(false);
			}
		}
		super.setOrder(order);
	}

	@Override
	public void move(Battle battle) {
		if (!canMove()) {
			setOrder(new DefendOrder());
			return;
		}

		currentAction = Action.MOVING;

		MoveOrder moveOrder = (MoveOrder) getOrder();
		float x = moveOrder.getXDestination();
		float y = moveOrder.getYDestination();
		// float x = nextX < 0 ? moveOrder.getXDestination() : nextX;
		// float y = nextX < 0 ? moveOrder.getYDestination() : nextY;

		// cannot rotate and move at the same time
		RotationStatus rotationStatus = updateUnitRotation(x, y);
		if (rotationStatus == RotationStatus.ROTATING) {
			((TankSprite) getSprite()).updateMovingAnimation(false);
			return;
		}

		((TankSprite) getSprite()).updateMovingAnimation(true);

		float dx = x - sprite.getX();
		float dy = y - sprite.getY();
		double angle = Math.atan(dy / dx);
		float dd = moveSpeed * 10 * 0.04f * getUnitSpeed() * (rotationStatus == RotationStatus.REVERSE ? REVERSE_SPEED : 1.0f);

		boolean hasArrived = false;
		float distanceLeft = MapLogic.getDistanceBetween(x, y, sprite.getX(), sprite.getY());
		if (distanceLeft < dd) {
			hasArrived = true;
			dd = distanceLeft;
		}

		float[] newPosition = MapLogic.getCoordinatesAfterTranslation(sprite.getX(), sprite.getY(), dd, angle, dx > 0);

		Tile nextTile = MapLogic.getTileAtCoordinates(battle.getMap(), newPosition[0], newPosition[1]);
		if (nextTile == null) {
			return;
		}

		if (!canMoveIn(nextTile)) {
			// run over soldiers
			if (nextTile.getContent() instanceof Soldier) {
				Soldier soldier = (Soldier) nextTile.getContent();
				instantlyKilledSomeone(battle, soldier);
			} else {
				return;
			}
		}

		sprite.setPosition(newPosition[0], newPosition[1]);

		if (nextTile.getTileX() != getTilePosition().getTileX() || nextTile.getTileY() != getTilePosition().getTileY()) {
			setTilePosition(battle, nextTile);
		}

		if (hasArrived) {
			updateMovementPath(battle.getMap());
			setOrder(new DefendOrder());
		}
	}

	@Override
	protected RotationStatus updateUnitRotation(float xDestination, float yDestination) {
		RotationStatus rotationStatus = RotationStatus.NONE;

		float dx = xDestination - sprite.getX();
		float dy = yDestination - sprite.getY();
		double finalAngle = Math.atan(dy / dx) * 180 / Math.PI;
		if (dx > 0) {
			finalAngle += 90;
		} else {
			finalAngle -= 90;
		}
		double dTau = finalAngle - sprite.getRotation();

		// reverse if not far !
		if (Math.abs(dTau) > 135.0f && MapLogic.getDistanceBetween(xDestination, yDestination, sprite.getX(), sprite.getY()) < REVERSE_THRESHOLD) {
			if (dx > 0) {
				finalAngle -= 180;
			} else {
				finalAngle += 180;
			}
			dTau = finalAngle - sprite.getRotation();
			rotationStatus = RotationStatus.REVERSE;
		}

		if (dTau > 180) {
			dTau -= 360;
		} else if (dTau < -180) {
			dTau += 360;
		}

		double rotationStep = 0;
		if (dTau > 0) {
			rotationStep = Math.min(dTau, ROTATION_SPEED);
		} else if (dTau < 0) {
			rotationStep = Math.max(dTau, -ROTATION_SPEED);
		}
		sprite.setRotation((float) (sprite.getRotation() + rotationStep));

		if (sprite.getRotation() > 360) {
			sprite.setRotation(sprite.getRotation() - 360);
		}

		if (sprite.getRotation() < -360) {
			sprite.setRotation(sprite.getRotation() + 360);
		}

		return Math.abs(rotationStep) < ROTATION_SPEED ? rotationStatus : RotationStatus.ROTATING;
	}

	@Override
	public void fire(Battle battle, Unit target) {
		// indirect shoot
		if (target == null) {
			FireOrder fireOrder = (FireOrder) getOrder();

			// only rotate
			if (getWeapons().get(0) instanceof Turret) {
				// turrets take time to rotate
				Turret turret = (Turret) getWeapons().get(0);
				boolean isRotatingOver = turret.rotateTurret(sprite, fireOrder.getXDestination(), fireOrder.getYDestination());
				if (!isRotatingOver) {
					return;
				}
			}

			setOrder(new DefendOrder());
			return;
		}

		if (target.isDead() || !MapLogic.canSee(battle.getMap(), this, target)) {
			// if target is dead or is not visible anymore, stop to shoot
			setOrder(new DefendOrder());
			return;
		}

		// get most suitable weapon
		boolean canShoot = false;
		for (Weapon weapon : getWeapons()) {
			if (weapon.canUseWeapon(this, target, MapLogic.canSee(battle.getMap(), this, target))) {
				fireWithWeapon(battle, weapon, target);
				canShoot = true;
			}
		}

		if (!canShoot) {
			// no weapon available for this fire order
			setOrder(new DefendOrder());
		}
	}

	public boolean fireWhileMoving(Battle battle, Unit target) {

		if (target.isDead() || !MapLogic.canSee(battle.getMap(), this, target)) {
			// if target is dead or is not visible anymore, stop to shoot
			return false;
		}

		// get most suitable weapon
		Weapon weapon = getBestWeapon(battle, target);
		if (weapon != null) {
			if (weapon instanceof Turret && target instanceof Vehicle) {
				// cannons cannot shoot while moving but can rotate
				Turret turret = (Turret) weapon;
				turret.rotateTurret(sprite, target.getSprite().getX(), target.getSprite().getY());
				return true;
			} else if (Math.abs(MapLogic.getAngle(sprite, target.getSprite().getX(), target.getSprite().getY())) >= MG_MAX_FIRE_ANGLE) {
				return false;
			}

			if (weapon.getReloadCounter() > 0) {
				if (weapon.getAimCounter() == 0) {
					weapon.setAimCounter(-10);
					// aiming
					this.currentAction = Action.AIMING;
				} else if (weapon.getAimCounter() < 0) {
					weapon.setAimCounter(weapon.getAimCounter() + 1);
					if (weapon.getAimCounter() == 0) {
						weapon.setAimCounter(weapon.getCadence());
					}
					// aiming
					this.currentAction = Action.AIMING;
				} else if (battle.getGameCounter() % (11 - weapon.getShootSpeed()) == 0) {
					// firing !!!
					currentAction = Action.FIRING;

					if (weapon.getAimCounter() == weapon.getCadence()) {
						battle.getOnNewSoundToPlay().playGeolocalizedSound(weapon.getSound(), sprite.getX(), sprite.getY());
					}

					// add muzzle flash sprite
					sprite.startFireAnimation(weapon);

					weapon.setAmmoAmount(weapon.getAmmoAmount() - 1);
					weapon.setReloadCounter(weapon.getReloadCounter() - 1);
					weapon.setAimCounter(weapon.getAimCounter() - 1);

					// if not a lot of ammo, more aiming !
					if (weapon.getAmmoAmount() == weapon.getMagazineSize() * 2) {
						weapon.setCadence(Math.max(1, weapon.getCadence() / 2));
					}

					// increase target panic
					target.getShots(battle, this);

					if (!target.isDead()) {// prevent the multiple frags bug

						weapon.resolveFireShot(battle, this, target);

						if (target.isDead()) {
							target.died(battle);
							killedSomeone(target);
						}
					}
				}
			} else if (weapon.getReloadCounter() == 0) {
				// need to reload
				weapon.setReloadCounter(-weapon.getReloadSpeed());
			} else {
				// reloading
				if (battle.getGameCounter() % 12 == 0) {
					weapon.setReloadCounter(weapon.getReloadCounter() + 1);
					if (weapon.getReloadCounter() == 0) {
						// reloading is over
						weapon.setReloadCounter(weapon.getMagazineSize());
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void setTilePosition(Battle battle, Tile tilePosition) {
		super.setTilePosition(battle, tilePosition);
		updateTilesPosition(battle);
	}

	private void updateTilesPosition(Battle battle) {
		if (realTilesPosition != null) {
			for (Tile t : realTilesPosition) {
				t.setContent(null);
			}
		}

		realTilesPosition = new ArrayList<Tile>();
		List<Tile> adjacentTiles = MapLogic.getAdjacentTiles(battle.getMap(), getTilePosition(),
				(int) Math.ceil(VEHICLE_RADIUS_SIZE * GameConstants.PIXEL_BY_METER / GameConstants.PIXEL_BY_TILE), true);
		for (Tile t : adjacentTiles) {
			// run over soldiers
			if (battle.getPhase() == Phase.combat && t.getContent() instanceof Soldier) {
				Soldier soldier = (Soldier) t.getContent();
				instantlyKilledSomeone(battle, soldier);
			}

			t.setContent(this);
			realTilesPosition.add(t);
		}
	}

	@Override
	public boolean canBeDeployedThere(Battle battle, Tile tile) {
		List<Tile> adjacentTiles = MapLogic.getAdjacentTiles(battle.getMap(), tile,
				(int) Math.ceil(VEHICLE_RADIUS_SIZE * GameConstants.PIXEL_BY_METER / GameConstants.PIXEL_BY_TILE), true);
		for (Tile t : adjacentTiles) {
			if (t.getContent() != null && t.getContent() != this) {
				return false;
			}
		}

		return super.canBeDeployedThere(battle, tile);
	}

}
