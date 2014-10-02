package com.glevel.dungeonhero.game.models.units;

import java.util.List;

import com.glevel.dungeonhero.game.data.ArmiesData;
import com.glevel.dungeonhero.game.graphics.CannonSprite;
import com.glevel.dungeonhero.game.models.orders.Order;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public class Cannon extends Unit {

	/**
     * 
     */
	private static final long serialVersionUID = 7678595033112981614L;
	// move
	private static final float MOVE_INJURY_FACTOR = 0.25f;
	private static final float MOVE_SPEED_ON_CONCRETE_FACTOR = 1.0f;
	private static final float MOVE_SPEED_ON_GRASS_FACTOR = 1.0f;
	private static final float MOVE_SPEED_ON_MUD_FACTOR = 0.5f;
	private static final float MOVE_SPEED_ON_WATER_FACTOR = 0.1f;

	// terrain protection
	private static final float HOUSE_PROTECTION_FACTOR = 0.5f;
	private static final float BUSH_PROTECTION_FACTOR = 0.9f;
	private static final float FIELD_PROTECTION_FACTOR = 0.9f;
	private static final float WALL_PROTECTION_FACTOR = 0.6f;
	private static final float TREE_PROTECTION_FACTOR = 0.7f;

	// price
	private static final int SOLDIER_BASE_PRICE = 5;
	private static final float RECRUIT_PRICE_MODIFIER = 0.7f;
	private static final float VETERAN_PRICE_MODIFIER = 1.0f;
	private static final float ELITE_PRICE_MODIFIER = 1.5f;

	public Cannon(ArmiesData army, int name, int image, Experience experience, List<Weapon> weapons, int moveSpeed,
			String spriteName, float spriteScale) {
		super(army, name, image, experience, weapons, moveSpeed, spriteName, spriteScale);
	}

	@Override
	public float getUnitSpeed() {
		// depends on health
		float healthFactor = 1 - getHealth().ordinal() * MOVE_INJURY_FACTOR;
		// depends on terrain
		if (getTilePosition() != null) {
			switch (getTilePosition().getGround()) {
			case concrete:
				return MOVE_SPEED_ON_CONCRETE_FACTOR * healthFactor;
			case grass:
				return MOVE_SPEED_ON_GRASS_FACTOR * healthFactor;
			case mud:
				return MOVE_SPEED_ON_MUD_FACTOR * healthFactor;
			case water:
				return MOVE_SPEED_ON_WATER_FACTOR * healthFactor;
			}
		}

		return 0;
	}

	@Override
	public float getUnitTerrainProtection() {
		if (getTilePosition().getTerrain() != null) {
			switch (getTilePosition().getTerrain()) {
			case house:
				return HOUSE_PROTECTION_FACTOR;
			case bush:
				return BUSH_PROTECTION_FACTOR;
			case wall:
				return WALL_PROTECTION_FACTOR;
			case field:
				return FIELD_PROTECTION_FACTOR;
			case tree:
				return TREE_PROTECTION_FACTOR;
			}
		}
		return 1.0f;
	}

	@Override
	public int getPrice() {
		int price = SOLDIER_BASE_PRICE;

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
		return price;
	}

	@Override
	public void setOrder(Order order) {
		if (!isDead() && sprite != null) {
			((CannonSprite) sprite).setOrder(order);
		}

		super.setOrder(order);
	}

}
