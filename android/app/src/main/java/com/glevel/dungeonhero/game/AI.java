package com.glevel.dungeonhero.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.glevel.dungeonhero.game.data.BattlesData;
import com.glevel.dungeonhero.game.data.UnitsData;
import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.game.models.Battle;
import com.glevel.dungeonhero.game.models.ObjectivePoint;
import com.glevel.dungeonhero.game.models.Player;
import com.glevel.dungeonhero.game.models.map.Tile;
import com.glevel.dungeonhero.game.models.orders.DefendOrder;
import com.glevel.dungeonhero.game.models.orders.FireOrder;
import com.glevel.dungeonhero.game.models.orders.HideOrder;
import com.glevel.dungeonhero.game.models.orders.MoveOrder;
import com.glevel.dungeonhero.game.models.units.Soldier;
import com.glevel.dungeonhero.game.models.units.Tank;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.game.models.units.categories.Vehicle;
import com.glevel.dungeonhero.game.models.weapons.Bazooka;
import com.glevel.dungeonhero.game.models.weapons.HMG;
import com.glevel.dungeonhero.game.models.weapons.Mortar;
import com.glevel.dungeonhero.game.models.weapons.PM;
import com.glevel.dungeonhero.game.models.weapons.Rifle;
import com.glevel.dungeonhero.game.models.weapons.Turret;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public class AI {

	public static void updateAI(Battle battle) {
		for (Player player : battle.getPlayers()) {
			if (player.isAI()) {
				updateOrders(battle, player);
			}
		}
	}

	private static void updateOrders(Battle battle, Player aiPlayer) {
		Player humanPlayer = battle.getEnemyPlayer(aiPlayer);

		List<MoveOrder> moveOrdersPool = new ArrayList<MoveOrder>();

		// defend strategic points
		for (Unit unit : humanPlayer.getUnits()) {
			if (!unit.isDead()) {
				for (ObjectivePoint objective : battle.getObjectives()) {
					if (objective.getOwner() == aiPlayer.getArmy()
							&& MapLogic.getDistanceBetween(unit,
									objective.getX(), objective.getY()) < GameUtils.PIXEL_BY_METER * 30) {
						// this strategic point is about to be lost
						moveOrdersPool.add(0, new MoveOrder(objective.getX(),
								objective.getY()));
					} else if (objective.getOwner() != aiPlayer.getArmy()) {
						// conquer this strategic point !
						moveOrdersPool.add(new MoveOrder(objective.getX(),
								objective.getY()));
					}
				}
			}
		}

		aiUnits: for (Unit aiUnit : aiPlayer.getUnits()) {
			if (!aiUnit.isDead()) {
				for (Unit playerUnit : humanPlayer.getUnits()) {
					if (!playerUnit.isDead()
							// give fire order if enemy unit is very close
							&& MapLogic.getDistanceBetween(playerUnit, aiUnit) < 30 * GameUtils.PIXEL_BY_METER
							&& MapLogic.canSee(battle.getMap(), aiUnit,
									playerUnit)) {
						aiUnit.setOrder(new FireOrder(playerUnit));
						continue aiUnits;
					}
				}

				if (aiUnit.getOrder() == null
						|| (aiUnit.getOrder() instanceof DefendOrder || aiUnit
								.getOrder() instanceof HideOrder)
						&& Math.random() < 0.3) {
					if (moveOrdersPool.size() > 0 && Math.random() < 0.5) {
						// conquer strategic points
						aiUnit.setOrder(moveOrdersPool.get(0));
						moveOrdersPool.remove(0);
					} else {
						// random moves
						aiUnit.setOrder(new MoveOrder((float) Math.random()
								* battle.getMap().getWidth()
								* GameUtils.PIXEL_BY_TILE, (float) Math
								.random()
								* battle.getMap().getHeight()
								* GameUtils.PIXEL_BY_TILE));
					}
				} else if (!(aiUnit.getOrder() instanceof MoveOrder || Math
						.random() < 0.3)) {
					if (aiUnit.getTilePosition().getTerrain() != null) {
						aiUnit.setOrder(new HideOrder());
					} else {
						aiUnit.setOrder(new DefendOrder());
					}
				}
			}
		}

	}

	private enum AI_AGGRESSIVITY {
		defensive, balanced, offensive
	}

	public static void createArmy(Player player, Battle battle) {
		player.setRequisition(battle.getRequisition(player));

		// AI strategy depends on the map
		AI_AGGRESSIVITY aggressivity = getAIAggressivity(battle, player);

		switch (aggressivity) {
		case defensive:
			// buy tank
			buyUnitsRandomly(player, Tank.class, Turret.class, 0, 1, 0.6f);

			// buy AT cannon
			buyUnitsRandomly(player, Soldier.class, Turret.class, 0, 1, 0.8f);

			// buy HMG
			buyUnitsRandomly(player, Soldier.class, HMG.class, 1, 2, 1.0f);

			// buy bazookas
			buyUnitsRandomly(player, Soldier.class, Bazooka.class, 1, 2, 0.6f);

			// buy mortars
			buyUnitsRandomly(player, Soldier.class, Mortar.class, 0, 1, 0.8f);

			// buy riflemen
			buyUnitsRandomly(player, Soldier.class, Rifle.class, 1, 4, 1.0f);

			// buy scouts
			buyUnitsRandomly(player, Soldier.class, PM.class, 1, 3, 1.0f);
			break;
		case balanced:
			// buy tank
			buyUnitsRandomly(player, Tank.class, Turret.class, 0, 1, 0.7f);

			// buy AT cannon
			buyUnitsRandomly(player, Soldier.class, Turret.class, 0, 1, 0.7f);

			// buy HMG
			buyUnitsRandomly(player, Soldier.class, HMG.class, 1, 2, 1.0f);

			// buy bazookas
			buyUnitsRandomly(player, Soldier.class, Bazooka.class, 0, 2, 0.8f);

			// buy mortars
			buyUnitsRandomly(player, Soldier.class, Mortar.class, 0, 1, 0.8f);

			// buy riflemen
			buyUnitsRandomly(player, Soldier.class, Rifle.class, 2, 4, 1.0f);

			// buy scouts
			buyUnitsRandomly(player, Soldier.class, PM.class, 2, 4, 1.0f);
			break;
		case offensive:
			// buy tank
			buyUnitsRandomly(player, Tank.class, Turret.class, 0, 1, 0.8f);

			// buy AT cannon
			buyUnitsRandomly(player, Soldier.class, Turret.class, 0, 0, 0.0f);

			// buy HMG
			buyUnitsRandomly(player, Soldier.class, HMG.class, 0, 2, 0.8f);

			// buy bazookas
			buyUnitsRandomly(player, Soldier.class, Bazooka.class, 1, 2, 0.8f);

			// buy mortars
			buyUnitsRandomly(player, Soldier.class, Mortar.class, 0, 1, 0.8f);

			// buy scouts
			buyUnitsRandomly(player, Soldier.class, PM.class, 2, 4, 1.0f);

			// buy riflemen
			buyUnitsRandomly(player, Soldier.class, Rifle.class, 1, 2, 1.0f);
			break;
		}

	}

	private static <U extends Unit, W extends Weapon> Player buyUnitsRandomly(
			Player player, Class<U> unitClass, Class<W> weaponClass,
			int nbFrom, int nbTo, float factor) {
		// buy random number of units
		int nbUnitsToBuy = (int) (nbFrom + Math.round((nbTo - nbFrom) * factor
				* Math.random()));
		for (int n = 0; n < nbUnitsToBuy; n++) {

			if (!canBuyMoreUnit(player)) {
				break;
			}

			List<Unit> availableUnits = UnitsData.getAllUnits(player.getArmy());
			Collections.shuffle(availableUnits);
			for (Unit unit : availableUnits) {
				// check if unit is of the type wanted and if it can be bought
				if (unit.getClass() == unitClass
						&& unit.getWeapons().get(0).getClass() == weaponClass
						&& canBuyIt(player, unit)) {
					// buy it
					player.getUnits().add(unit);
					player.setRequisition(player.getRequisition()
							- unit.getPrice());
					break;
				}
			}

		}

		return player;
	}

	private static boolean canBuyMoreUnit(Player player) {
		return player.getUnits().size() < GameUtils.MAX_UNIT_PER_ARMY
				&& player.getRequisition() >= UnitsData
						.getAllUnits(player.getArmy()).get(0).getPrice();
	}

	private static boolean canBuyIt(Player player, Unit unit) {
		return player.getRequisition() >= unit.getPrice();
	}

	private static AI_AGGRESSIVITY getAIAggressivity(Battle battle,
			Player player) {
		switch (BattlesData.values()[battle.getBattleId()]) {
		case OOSTERBEEK:
			return AI_AGGRESSIVITY.balanced;
		case NIMEGUE:
			return player.isAlly() ? AI_AGGRESSIVITY.offensive
					: AI_AGGRESSIVITY.defensive;
		case ARNHEM_STREETS:
			return player.isAlly() ? AI_AGGRESSIVITY.defensive
					: AI_AGGRESSIVITY.offensive;
		default:
			return AI_AGGRESSIVITY.balanced;
		}
	}

	public static void deployTroops(Battle battle, Player player) {
		int[] deploymentBoundaries = battle.getDeploymentBoundaries(player);
		// deploy vehicles first
		for (Unit unit : player.getUnits()) {
			if (unit instanceof Vehicle) {
				// get a random position
				Tile tile = null;
				do {
					tile = battle.getMap().getTiles()[(int) (Math.random() * (battle
							.getMap().getHeight() - 1))][(int) (1 + deploymentBoundaries[0] + Math
							.random()
							* (deploymentBoundaries[1]
									- deploymentBoundaries[0] - 1))];
				} while (!unit.canMoveIn(tile));

				// position the units
				unit.setTilePosition(battle, tile);
			}
		}

		// then other units
		for (Unit unit : player.getUnits()) {
			if (!(unit instanceof Vehicle)) {
				// get a random position
				Tile tile = null;
				do {
					tile = battle.getMap().getTiles()[(int) (Math.random() * (battle
							.getMap().getHeight() - 1))][(int) (1 + deploymentBoundaries[0] + Math
							.random()
							* (deploymentBoundaries[1]
									- deploymentBoundaries[0] - 1))];
				} while (!unit.canMoveIn(tile));

				// position the units
				unit.setTilePosition(battle, tile);
			}
		}
	}
}
