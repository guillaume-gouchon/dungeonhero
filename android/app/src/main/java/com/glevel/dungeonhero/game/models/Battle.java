package com.glevel.dungeonhero.game.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.glevel.dungeonhero.game.AI;
import com.glevel.dungeonhero.game.GameUtils;
import com.glevel.dungeonhero.game.GameUtils.DifficultyLevel;
import com.glevel.dungeonhero.game.data.ArmiesData;
import com.glevel.dungeonhero.game.data.BattlesData;
import com.glevel.dungeonhero.game.interfaces.OnNewSoundToPlay;
import com.glevel.dungeonhero.game.interfaces.OnNewSpriteToDraw;
import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.game.models.map.Map;
import com.glevel.dungeonhero.game.models.orders.MoveOrder;
import com.glevel.dungeonhero.game.models.units.categories.Unit;

public class Battle implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -5687413891626670339L;
	private final int battleId;
	private final int name;
	private final int image;
	private final String tileMapName;
	private transient final Map map;
	private final int alliesRequisition, axisRequisition;
	private final VictoryCondition alliesVictoryCondition,
			axisVictoryCondition;
	// deployment
	private final int alliesDeploymentZoneSize, axisDeploymentZoneSize;
	private final boolean isAllyLeftSide;

	private long id = 0L;
	private int campaignId = 0;
	private int importance;
	private transient List<Player> players = new ArrayList<Player>();
	private transient Phase phase = Phase.deployment;
	private boolean hasStarted = false;
	private transient List<ObjectivePoint> lstObjectives;
	private transient DifficultyLevel difficultyLevel;

	// Callbacks
	private transient OnNewSpriteToDraw onNewSprite;
	private transient OnNewSoundToPlay onNewSoundToPlay;

	// for campaign mode
	private boolean isDone = false;
	private int gameCounter = 0;

	public static enum Phase {
		deployment, combat
	}

	/**
	 * Single Battle Mode Constructor
	 * 
	 * @param data
	 */
	public Battle(BattlesData data) {
		this.battleId = data.getId();
		this.name = data.getName();
		this.image = data.getImage();
		this.map = new Map();
		this.tileMapName = data.getTileMapName();
		this.alliesRequisition = data.getAlliesRequisition();
		this.axisRequisition = data.getAxisRequisition();
		this.alliesVictoryCondition = new VictoryCondition(100);
		this.axisVictoryCondition = new VictoryCondition(100);
		this.alliesDeploymentZoneSize = data.getAlliesDeploymentZoneSize();
		this.axisDeploymentZoneSize = data.getAxisDeploymentZoneSize();
		this.isAllyLeftSide = data.getIsAllyLeftSide();
		this.lstObjectives = new ArrayList<ObjectivePoint>();
	}

	/**
	 * Campaign Mode Constructor
	 * 
	 * @param data
	 */
	public Battle(BattlesData data, int importance,
			VictoryCondition alliesVictoryCondition,
			VictoryCondition axisVictoryCondition) {
		this.battleId = data.getId();
		this.name = data.getName();
		this.image = data.getImage();
		this.importance = importance;
		this.map = new Map();
		this.tileMapName = data.getTileMapName();
		this.alliesRequisition = data.getAlliesRequisition();
		this.axisRequisition = data.getAxisRequisition();
		this.alliesVictoryCondition = alliesVictoryCondition;
		this.axisVictoryCondition = axisVictoryCondition;
		this.alliesDeploymentZoneSize = data.getAlliesDeploymentZoneSize();
		this.axisDeploymentZoneSize = data.getAxisDeploymentZoneSize();
		this.isAllyLeftSide = data.getIsAllyLeftSide();
		this.lstObjectives = new ArrayList<ObjectivePoint>();
	}

	public int getName() {
		return name;
	}

	public int getImage() {
		return image;
	}

	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}

	public Map getMap() {
		return map;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<Unit> getEnemies(Unit unit) {
		for (Player p : players) {
			if (p.getArmy() != unit.getArmy()) {
				return p.getUnits();
			}
		}
		return null;
	}

	public List<Unit> getEnemies(ArmiesData army) {
		for (Player p : players) {
			if (p.getArmy() != army) {
				return p.getUnits();
			}
		}
		return null;
	}

	public Player getEnemyPlayer(Player player) {
		for (Player p : players) {
			if (p != player) {
				return p;
			}
		}
		return null;
	}

	public Player getMe() {
		return getPlayers().get(0);
	}

	public OnNewSpriteToDraw getOnNewSprite() {
		return onNewSprite;
	}

	public void setOnNewSprite(OnNewSpriteToDraw onNewSprite) {
		this.onNewSprite = onNewSprite;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBattleId() {
		return battleId;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public boolean isSingleBattle() {
		return campaignId == 0;
	}

	public String getTileMapName() {
		return tileMapName;
	}

	public VictoryCondition getPlayerVictoryCondition(ArmiesData army) {
		if (army.isAlly()) {
			return alliesVictoryCondition;
		} else {
			return axisVictoryCondition;
		}
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public boolean isAllyLeftSide() {
		return isAllyLeftSide;
	}

	public int[] getDeploymentBoundaries(Player player) {
		if (player.isAlly()) {
			if (isAllyLeftSide) {
				return new int[] { 0, alliesDeploymentZoneSize };
			} else {
				return new int[] { map.getWidth() - alliesDeploymentZoneSize,
						map.getWidth() };
			}
		} else {
			if (isAllyLeftSide) {
				return new int[] { map.getWidth() - axisDeploymentZoneSize,
						map.getWidth() };
			} else {
				return new int[] { 0, axisDeploymentZoneSize };
			}
		}
	}

	public int getRequisition(Player player) {
		if (player.isAlly()) {
			return alliesRequisition;
		} else {
			return axisRequisition;
		}
	}

	public boolean isStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

	public List<ObjectivePoint> getObjectives() {
		return lstObjectives;
	}

	public void setLstObjectives(List<ObjectivePoint> lstObjectives) {
		this.lstObjectives = lstObjectives;
	}

	public int getGameCounter() {
		return gameCounter;
	}

	/**
	 * Updates Game Logic
	 * 
	 * @return winner if any
	 */
	public Player update() {
		gameCounter++;
		if (gameCounter > 999) {
			gameCounter = 0;
		}
		if (gameCounter % GameUtils.UPDATE_VISION_FREQUENCY == 0) {
			updateVisibility();
		}

		// update AI orders depending on difficulty level
		if (gameCounter
				% (GameUtils.AI_FREQUENCY * (difficultyLevel.ordinal() + 1)) == 0) {
			AI.updateAI(this);
		}

		for (Player player : players) {
			for (Unit unit : player.getUnits()) {
				if (!unit.isDead()) {
					if (unit.getOrder() != null) {
						// resolve unit action
						unit.resolveOrder(this);
					} else {
						// no order : take initiative
						unit.takeInitiative();
					}
				}
			}
			// check victory conditions
			if (gameCounter % GameUtils.CHECK_VICTORY_FREQUENCY == 0
					&& player.checkIfPlayerWon(this)) {
				return player;
			}
		}

		// play random atmoshpere sounds
		if (gameCounter % GameUtils.ATMO_SOUND_FREQUENCY == 0) {
			if (Math.random() < 0.2) {
				String atmoSound = GameUtils.ATMO_SOUNDS[(int) Math.round(Math
						.random() * (GameUtils.ATMO_SOUNDS.length - 1))];
				getOnNewSoundToPlay()
						.playSound(
								atmoSound,
								(float) (Math.random() * map.getWidth() * GameUtils.PIXEL_BY_TILE),
								(float) (Math.random() * map.getHeight() * GameUtils.PIXEL_BY_TILE));
			}
		}

		return null;
	}

	/**
	 * Updates player vision
	 */
	public void updateVisibility() {
		for (Unit unit : getEnemyPlayer(getMe()).getUnits()) {
			unit.setVisible(false);
		}

		for (Unit myUnit : getMe().getUnits()) {
			if (!myUnit.isDead()) {
				for (Unit enemyUnit : getEnemies(myUnit)) {
					if (MapLogic.canSee(map, myUnit, enemyUnit)) {
						enemyUnit.setVisible(true);
					}
				}
			}
		}
	}

	/**
	 * Updates units positions
	 */
	public void updateMoves() {
		for (Player player : players) {
			for (Unit unit : player.getUnits()) {
				if (unit.getOrder() != null
						&& unit.getOrder() instanceof MoveOrder) {
					// move unit
					unit.move(this);
				}
			}
		}
	}

	public OnNewSoundToPlay getOnNewSoundToPlay() {
		return onNewSoundToPlay;
	}

	public void setOnNewSoundToPlay(OnNewSoundToPlay onNewSoundToPlay) {
		this.onNewSoundToPlay = onNewSoundToPlay;
	}

	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

}
