package com.glevel.dungeonhero.game.models.units.categories;

import java.util.List;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.GameUtils;
import com.glevel.dungeonhero.game.data.ArmiesData;
import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.game.logic.pathfinding.MovingElement;
import com.glevel.dungeonhero.game.logic.pathfinding.Node;
import com.glevel.dungeonhero.game.models.Battle;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.game.models.ObjectivePoint;
import com.glevel.dungeonhero.game.models.map.Map;
import com.glevel.dungeonhero.game.models.map.Tile;
import com.glevel.dungeonhero.game.models.orders.DefendOrder;
import com.glevel.dungeonhero.game.models.orders.FireOrder;
import com.glevel.dungeonhero.game.models.orders.HideOrder;
import com.glevel.dungeonhero.game.models.orders.MoveOrder;
import com.glevel.dungeonhero.game.models.orders.Order;
import com.glevel.dungeonhero.game.models.units.Cannon;
import com.glevel.dungeonhero.game.models.units.Soldier;
import com.glevel.dungeonhero.game.models.units.Tank;
import com.glevel.dungeonhero.game.models.weapons.Knife;
import com.glevel.dungeonhero.game.models.weapons.Turret;
import com.glevel.dungeonhero.game.models.weapons.categories.IndirectWeapon;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public abstract class Unit extends GameElement implements MovingElement {

	/**
     * 
     */
	private static final long serialVersionUID = -1514358997270651189L;

	private static final float CLOSE_COMBAT_MAX_DISTANCE = 6.0f;// in meters
	private static final float START_AMBUSH_DISTANCE = 15.0f;// in meters

	protected final ArmiesData army;
	private final int image;
	protected final int moveSpeed;
	private List<Weapon> weapons;
	protected Experience experience;
	private String spriteName;
	private float spriteScale;

	private InjuryState health;
	private int frags;
	private boolean isAvailable;
	private Order order;
	protected Action currentAction;
	private int panic;

	protected static enum RotationStatus {
		NONE, ROTATING, REVERSE
	}

	public static enum InjuryState {
		NONE(R.color.green), INJURED(R.color.yellow), BADLYINJURED(R.color.orange), DEAD(R.color.red);

		private final int color;

		private InjuryState(int color) {
			this.color = color;
		}

		public int getColor() {
			return color;
		}

	}

	public static enum Experience {
		RECRUIT(0), VETERAN(R.drawable.ic_veteran), ELITE(R.drawable.ic_elite);

		private final int image;

		private Experience(int image) {
			this.image = image;
		}

		public int getImage() {
			return image;
		}

	}

	public static enum Action {
		WAITING, MOVING, RUNNING, FIRING, HIDING, RELOADING, AIMING, DEFENDING, FIGHTING
	}

	public Unit(ArmiesData army, int name, int image, Experience experience, List<Weapon> weapons, int moveSpeed,
			String spriteName, float spriteScale) {
		super(name, spriteName, spriteScale);
		this.army = army;
		this.image = image;
		this.experience = experience;
		this.weapons = weapons;
		this.moveSpeed = moveSpeed;
		this.health = InjuryState.NONE;
		this.currentAction = Action.WAITING;
		this.setPanic(0);
		this.frags = 0;
		this.spriteName = spriteName;
		this.spriteScale = spriteScale;
		setOrder(new HideOrder());
	}

	protected abstract float getUnitSpeed();

	public abstract float getUnitTerrainProtection();

	public abstract int getPrice();

	public InjuryState getHealth() {
		return health;
	}

	public void setHealth(InjuryState health) {
		this.health = health;
	}

	public List<Weapon> getWeapons() {
		return weapons;
	}

	public void setWeapons(List<Weapon> weapons) {
		this.weapons = weapons;
	}

	public Experience getExperience() {
		return experience;
	}

	public void setExperience(Experience experience) {
		this.experience = experience;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		if (!canMove() && order instanceof MoveOrder) {
			setOrder(new DefendOrder());
			return;
		}
		this.order = order;
	}

	public Action getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public int getFrags() {
		return frags;
	}

	public int getImage() {
		return image;
	}

	public ArmiesData getArmy() {
		return army;
	}

	public int getRealSellPrice(boolean isSelling) {
		if (isSelling) {
			return (int) (getPrice() * GameUtils.SELL_PRICE_FACTOR);
		} else {
			return getPrice();
		}
	}

	/**
	 * Create a new instance of Unit. Used when we buy a unit.
	 * 
	 * @param army
	 * @return
	 */
	public Unit copy() {
		Unit unit = null;
		if (this instanceof Soldier) {
			unit = new Soldier(army, name, image, experience, weapons, moveSpeed, spriteName, spriteScale);
		} else if (this instanceof Cannon) {
			unit = new Cannon(army, name, image, experience, weapons, moveSpeed, spriteName, spriteScale);
		} else if (this instanceof Tank) {
			Vehicle vehicle = (Vehicle) this;
			unit = new Tank(army, name, image, experience, weapons, moveSpeed, vehicle.getArmor(), spriteName,
					spriteScale);
		}
		return unit;
	}

	public void updateMovementPath(Map map) {
	}

	public void move(Battle battle) {
		this.currentAction = Action.MOVING;
		MoveOrder moveOrder = (MoveOrder) order;

		updateUnitRotation(moveOrder.getXDestination(), moveOrder.getYDestination());
		float dx = moveOrder.getXDestination() - sprite.getX();
		float dy = moveOrder.getYDestination() - sprite.getY();
		double angle = Math.atan(dy / dx);
		float dd = moveSpeed * 10 * 0.03f * getUnitSpeed();

		boolean hasArrived = false;
		float distanceLeft = MapLogic.getDistanceBetween(moveOrder.getXDestination(), moveOrder.getYDestination(),
				sprite.getX(), sprite.getY());
		if (distanceLeft < dd) {
			hasArrived = true;
			dd = distanceLeft;
		}

		float[] newPosition = MapLogic.getCoordinatesAfterTranslation(sprite.getX(), sprite.getY(), dd, angle, dx > 0);

		Tile nextTile = MapLogic.getTileAtCoordinates(battle.getMap(), newPosition[0], newPosition[1]);

		if (nextTile == null || !canMoveIn(nextTile)) {
			return;
		}

		sprite.setPosition(newPosition[0], newPosition[1]);

		if (getTilePosition() != null
				&& (nextTile.getTileX() != getTilePosition().getTileX() || nextTile.getTileY() != getTilePosition()
						.getTileY())) {
			setTilePosition(battle, nextTile);
		}

		if (hasArrived) {
			setOrder(null);
		}
	}

	protected RotationStatus updateUnitRotation(float xDestination, float yDestination) {
		float dx = xDestination - sprite.getX();
		float dy = yDestination - sprite.getY();
		double angle = Math.atan(dy / dx);
		if (dx > 0) {
			sprite.setRotation((float) (angle * 180 / Math.PI + 90));
		} else {
			sprite.setRotation((float) (angle * 180 / Math.PI + 270));
		}
		return RotationStatus.NONE;
	}

	public void fire(Battle battle, Unit target) {
		// indirect shoot
		if (target == null) {
			FireOrder fireOrder = (FireOrder) order;

			// only rotate
			if (getWeapons().get(0) instanceof Turret) {
				// turrets take time to rotate
				Turret turret = (Turret) getWeapons().get(0);
				boolean isRotatingOver = turret.rotateTurret(sprite, fireOrder.getXDestination(),
						fireOrder.getYDestination());
				if (!isRotatingOver) {
					return;
				}
			}

			setOrder(new DefendOrder());
			return;
		}

		if (target.isDead() || !(getWeapons().get(0) instanceof IndirectWeapon)
				&& !MapLogic.canSee(battle.getMap(), this, target)) {
			// if target is dead or is not visible anymore, stop to shoot
			setOrder(new DefendOrder());
			return;
		}

		// get most suitable weapon
		Weapon weapon = getBestWeapon(battle, target);
		if (weapon != null) {
			fireWithWeapon(battle, weapon, target);
		} else {
			// no weapon available for this fire order
			setOrder(new DefendOrder());
		}
	}

	protected void fireWithWeapon(Battle battle, Weapon weapon, Unit target) {
		if (weapon instanceof Turret) {
			// turrets take time to rotate
			Turret turret = (Turret) weapon;
			boolean isRotatingOver = turret.rotateTurret(sprite, target.getSprite().getX(), target.getSprite().getY());
			if (!isRotatingOver) {
				return;
			}
		} else if (canMove()) {
			RotationStatus rotationStatus = updateUnitRotation(target.getSprite().getX(), target.getSprite().getY());
			if (rotationStatus == RotationStatus.ROTATING) {
				return;
			}
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
					battle.getOnNewSoundToPlay().playSound(weapon.getSound(), sprite.getX(), sprite.getY());
				}

				if (!(weapon instanceof Knife)) {
					// add muzzle flash sprite
					sprite.startFireAnimation(weapon);
				}

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
			this.currentAction = Action.RELOADING;
			weapon.setReloadCounter(-weapon.getReloadSpeed());
		} else {
			// reloading
			this.currentAction = Action.RELOADING;
			if (battle.getGameCounter() % 12 == 0) {
				weapon.setReloadCounter(weapon.getReloadCounter() + 1);
				if (weapon.getReloadCounter() == 0) {
					// reloading is over
					weapon.setReloadCounter(weapon.getMagazineSize());
				}
			}
		}

		if (weapon instanceof Knife) {
			currentAction = Action.FIGHTING;
		}
	}

	public Weapon getBestWeapon(Battle battle, Unit target) {
		if (this instanceof Soldier && target instanceof Soldier
				&& MapLogic.getDistanceBetween(this, target) < CLOSE_COMBAT_MAX_DISTANCE * GameUtils.PIXEL_BY_METER) {
			// close combat !
			return new Knife();
		}

		boolean canSeeTarget = MapLogic.canSee(battle.getMap(), this, target);
		Weapon bestWeapon = null;
		for (Weapon weapon : weapons) {
			if (weapon.canUseWeapon(this, target, canSeeTarget)) {
				if (bestWeapon == null || weapon.getEfficiencyAgainst(target) > bestWeapon.getEfficiencyAgainst(target)) {
					bestWeapon = weapon;
				}
			}
		}

		return bestWeapon;
	}

	public void applyDamage(int damage) {
		health = InjuryState.values()[Math.min(InjuryState.DEAD.ordinal(), health.ordinal() + damage)];
	}

	public void defendPosition(Battle battle) {
		this.currentAction = Action.DEFENDING;

		if (battle.getGameCounter() % 3 == 0) {
			if (this.panic > 0) {
				this.panic--;
			}
		}
	}

	public void hide(Battle battle) {
		this.currentAction = Action.HIDING;

		if (battle.getGameCounter() % 3 == 0) {
			if (this.panic > 0) {
				this.panic--;
			}
		}
	}

	public int getPanic() {
		return panic;
	}

	public void setPanic(int panic) {
		this.panic = panic;
	}

	public void resolveOrder(Battle battle) {
		if (panic > 0) {
			// test if the unit can react
			if (Math.random() * 10 + getExperience().ordinal() < panic) {
				// the unit is under fire
				// if (Math.random() < 0.1) {
				// battle.getOnNewSoundToPlay().playSound("need_support",
				// sprite.getX(), sprite.getY());
				// }
				hide(battle);
				return;
			}
		}

		if (order instanceof MoveOrder) {
			if (this instanceof Vehicle) {
				// vehicles can move and fire at the same time
				for (Unit u : battle.getEnemies(this)) {
					if (!u.isDead() && MapLogic.canSee(battle.getMap(), this, u)) {
						boolean done = ((Vehicle) this).fireWhileMoving(battle, u);
						if (done) {
							return;
						}
					}
				}
				((Turret) getWeapons().get(0)).rotateTurretTo(sprite, sprite.getRotation());
			}
			updateMovementPath(battle.getMap());
		} else if (order instanceof DefendOrder) {
			// search for enemies
			for (Unit u : battle.getEnemies(this)) {
				if (!u.isDead() && MapLogic.canSee(battle.getMap(), this, u) && getBestWeapon(battle, u) != null) {
					setOrder(new FireOrder(u));
					return;
				}
			}
			// stay ambush
			defendPosition(battle);
		} else if (order instanceof FireOrder) {
			fire(battle, ((FireOrder) order).getTarget());
		} else if (order instanceof HideOrder) {
			// ambush !
			for (Unit u : battle.getEnemies(this)) {
				if (!u.isDead() && MapLogic.canSee(battle.getMap(), this, u) && getBestWeapon(battle, u) != null
						&& MapLogic.getDistanceBetween(this, u) < START_AMBUSH_DISTANCE * GameUtils.PIXEL_BY_METER) {
					setOrder(new FireOrder(u));
					return;
				}
			}
			hide(battle);
		}
	}

	public void takeInitiative() {
		setOrder(new DefendOrder());
	}

	public boolean isDead() {
		return health == InjuryState.DEAD;
	}

	public void getShots(Battle battle, Unit shooter) {
		// increase panic
		if (panic < 10) {
			panic++;
		}

		// fight back
		if (order == null || order instanceof DefendOrder || order instanceof MoveOrder && Math.random() < 0.3) {
			if (MapLogic.canSee(battle.getMap(), this, shooter) && getBestWeapon(battle, shooter) != null) {
				setOrder(new FireOrder(shooter));
				// battle.getOnNewSoundToPlay().playSound("incoming",
				// sprite.getX(), sprite.getY());
			}
		}
	}

	public void killedSomeone(Unit unitKilled) {
		if (unitKilled.getArmy() != army) {
			// add frags
			if (unitKilled instanceof Tank) {
				frags += 5;
			} else if (unitKilled instanceof Vehicle) {
				frags += 2;
			} else if (unitKilled instanceof Cannon) {
				frags += 2;
			} else if (unitKilled instanceof Soldier) {
				frags += 1;
			}
		}
	}

	public void died(Battle battle) {
		if (this instanceof Soldier || this instanceof Cannon) {
			setTilePosition(battle, null);
			sprite.setZIndex(10);
		}

		if (!sprite.isVisible()) {
			sprite.setVisible(true);
		}
		sprite.setCanBeDragged(false);
		setOrder(null);

		// draw sprite
		if (this instanceof Tank || this instanceof Cannon) {
			// smoke
			battle.getOnNewSprite().drawAnimatedSprite(getSprite().getX(), getSprite().getY() - 70, "smoke.png", 120,
					2.0f, -1, false, sprite.getZIndex() + 1);
			battle.getOnNewSoundToPlay().playSound("explosion", sprite.getX(), sprite.getY());
		} else if (this instanceof Soldier) {
			// blood
			battle.getOnNewSprite().drawAnimatedSprite(getSprite().getX(), getSprite().getY(), "blood.png", 120, 0.6f,
					0, false, sprite.getZIndex() + 1);
			battle.getOnNewSoundToPlay().playSound("death", sprite.getX(), sprite.getY());
		}

	}

	@Override
	public void setTilePosition(Battle battle, Tile tilePosition) {
		super.setTilePosition(battle, tilePosition);

		// conquer objectives if any
		if (tilePosition != null && tilePosition.getObjective() != null) {
			ObjectivePoint objective = tilePosition.getObjective();
			if (objective.getOwner() != army) {
				objective.setOwner(army);
				frags++;
			}
		}
	}

	public boolean canMove() {
		return moveSpeed > 0;
	}

	@Override
	public boolean canMoveIn(Node node) {
		return ((Tile) node).getContent() == null || ((Tile) node).getContent() == this;
	}

	public void instantlyKilledSomeone(Battle battle, Unit target) {
		target.setHealth(InjuryState.DEAD);
		target.died(battle);
		killedSomeone(target);
	}

	public boolean canBeDeployedThere(Battle battle, Tile tile) {
		return canMoveIn(tile);
	}

}
