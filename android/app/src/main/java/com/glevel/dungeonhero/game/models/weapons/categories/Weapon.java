package com.glevel.dungeonhero.game.models.weapons.categories;

import java.io.Serializable;

import org.andengine.util.color.Color;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.GameUtils;
import com.glevel.dungeonhero.game.andengine.custom.CustomColors;
import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.game.models.Battle;
import com.glevel.dungeonhero.game.models.map.Tile.TerrainType;
import com.glevel.dungeonhero.game.models.units.Cannon;
import com.glevel.dungeonhero.game.models.units.Soldier;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.game.models.units.categories.Unit.Action;
import com.glevel.dungeonhero.game.models.units.categories.Unit.InjuryState;
import com.glevel.dungeonhero.game.models.units.categories.Vehicle;
import com.glevel.dungeonhero.game.models.weapons.Mortar;

public abstract class Weapon implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -3726243358409900250L;
	private final int name;
	private final int image;
	private final int apPower;
	private final int atPower;
	protected final int range;
	private final int magazineSize;
	private final int reloadSpeed;
	private final int shootSpeed;
	private final int[] accuracy;
	private final String sound;

	private int cadence;
	private int ammoAmount;
	private int reloadCounter;// while > 0 there are ammo left, while < 0
								// reloading
	private int aimCounter = 0;

	// price
	private static final int WEAPON_BASE_PRICE = 2;

	public Weapon(int name, int image, int apPower, int atPower, int range, int nbMagazines, int cadence,
			int magazineSize, int reloadSpeed, int shootSpeed, int[] accuracy, String sound) {
		this.name = name;
		this.image = image;
		this.apPower = apPower;
		this.atPower = atPower;
		this.range = range;
		this.ammoAmount = nbMagazines * magazineSize;
		this.cadence = cadence;
		this.magazineSize = magazineSize;
		this.reloadCounter = magazineSize;
		this.reloadSpeed = reloadSpeed;
		this.shootSpeed = shootSpeed;
		this.accuracy = accuracy;
		this.sound = sound;
	}

	public int getPrice() {
		int price = WEAPON_BASE_PRICE;
		price += 0.1 * (apPower + atPower + cadence / 2.0) * ammoAmount / magazineSize;
		return price;
	}

	public int getMagazineSize() {
		return magazineSize;
	}

	public int getReloadCounter() {
		return reloadCounter;
	}

	public void setReloadCounter(int reloadCounter) {
		this.reloadCounter = reloadCounter;
	}

	public int getName() {
		return name;
	}

	public int getImage() {
		return image;
	}

	public int getAmmoAmount() {
		return ammoAmount;
	}

	public void setAmmoAmount(int ammoAmount) {
		this.ammoAmount = ammoAmount;
	}

	public int getCadence() {
		return cadence;
	}

	public void setCadence(int cadence) {
		this.cadence = cadence;
	}

	public int getRange() {
		return range;
	}

	public int getReloadSpeed() {
		return reloadSpeed;
	}

	public int getAPColorEfficiency() {
		return efficiencyValueToColor(apPower);
	}

	public int getATColorEfficiency() {
		return efficiencyValueToColor(atPower);
	}

	public int getShootSpeed() {
		return shootSpeed;
	}

	private int efficiencyValueToColor(int efficiency) {
		return 0;
	}

	public void resolveFireShot(Battle battle, Unit shooter, Unit target) {
		// does it touch the target ? Calculate the chance to hit
		resolveDamageDiceRoll(getToHit(shooter, target), shooter, target);
	}

	private int getToHit(Unit shooter, Unit target) {
		float distance = MapLogic.getDistanceBetween(shooter, target);

		int tohit = accuracy[distanceToRangeCategory(distance)];

		// add terrain protection
		tohit *= target.getUnitTerrainProtection();

		// target is hiding : tohit depends on target's experience
		if (target.getCurrentAction() == Action.HIDING || target.getCurrentAction() == Action.RELOADING) {
			tohit -= 5 * (target.getExperience().ordinal() + 1);
		}

		// tohit depends on weapon cadence
		tohit -= cadence * 3;

		// experience modifier
		tohit += shooter.getExperience().ordinal() * 7;

		// panic modifier
		tohit -= shooter.getPanic();

		return tohit;
	}

	public Color getDistanceColor(Unit shooter, Unit target) {
		int tohit = getToHit(shooter, target);
		if (tohit < 25) {
			return Color.RED;
		} else if (tohit < 50) {
			return CustomColors.ORANGE;
		} else if (tohit < 75) {
			return Color.YELLOW;
		} else {
			return Color.GREEN;
		}
	}

	protected void resolveDamageDiceRoll(int tohit, Unit shooter, Unit target) {

		if (target instanceof Soldier || target instanceof Cannon) {
			int diceRoll = (int) (Math.random() * 100);
			if (diceRoll < tohit) {
				// hit !
				if (diceRoll < tohit / 4) {
					// critical !
					target.setHealth(InjuryState.DEAD);
				} else if (diceRoll < tohit / 2) {
					// heavy !
					target.applyDamage(2);
				} else {
					if (Math.random() < 0.5) {
						// light injured
						target.applyDamage(1);
					} else {
						// nothing
					}
				}
			}
		} else if (target instanceof Vehicle) {
			Vehicle vehicle = (Vehicle) target;
			if (atPower < vehicle.getArmor()) {
				return;
			}

			// back and sides of tanks are more vulnerable
			int sidesBonus = 0;
			float absoluteAngle = Math.abs(MapLogic.getAngle(target.getSprite(), shooter.getSprite().getX(), shooter
					.getSprite().getY()));
			if (absoluteAngle > 135.0f) {
				// back
				sidesBonus = 2;
			} else if (absoluteAngle > 45.0f) {
				// sides
				sidesBonus = 1;
			}
			int damage = (int) (Math.random() * (sidesBonus + atPower - vehicle.getArmor()));
			target.applyDamage(damage);
		}

	}

	protected static int distanceToRangeCategory(float distance) {
		if (distance < 25 * GameUtils.PIXEL_BY_METER) {
			return 0;
		} else if (distance < 50 * GameUtils.PIXEL_BY_METER) {
			return 1;
		} else if (distance < 75 * GameUtils.PIXEL_BY_METER) {
			return 2;
		} else {
			return 3;
		}
	}

	public boolean canUseWeapon(Unit shooter, Unit target, boolean canSeeTarget) {
		if (target instanceof Vehicle && atPower < ((Vehicle) target).getArmor() || apPower == 0
				&& (target instanceof Soldier || target instanceof Cannon)) {
			// weapon is useless against target
			return false;
		} else if (ammoAmount <= 0) {
			// out of ammo
			return false;
		} else if (MapLogic.getDistanceBetween(shooter, target) > range * GameUtils.PIXEL_BY_METER) {
			// out of range
			return false;
		} else if (!canSeeTarget && !(this instanceof IndirectWeapon)) {
			// needs to see target
			return false;
		} else if (this instanceof Mortar && shooter.getTilePosition().getTerrain() == TerrainType.house) {
			// mortar cannot shoot from inside a house
			return false;
		}

		return true;
	}

	public int getEfficiencyAgainst(Unit target) {
		if (target instanceof Soldier || target instanceof Cannon) {
			return apPower;
		} else {
			return atPower;
		}
	}

	public int getAimCounter() {
		return aimCounter;
	}

	public void setAimCounter(int aimCounter) {
		this.aimCounter = aimCounter;
	}

	public String getSound() {
		return sound;
	}

}
