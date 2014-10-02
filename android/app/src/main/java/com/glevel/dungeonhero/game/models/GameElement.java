package com.glevel.dungeonhero.game.models;

import java.io.Serializable;

import org.andengine.util.color.Color;

import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.game.models.map.Tile;

public abstract class GameElement implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -5880458091427517171L;
	protected final int name;
	private final String spriteName;
	private transient Tile tilePosition;
	protected transient UnitSprite sprite;
	private Rank rank;
	private boolean isVisible = false;
	private float spriteScale;
	private float currentX = -1.0f, currentY, currentRotation;

	public static enum Rank {
		neutral, enemy, ally
	}

	public UnitSprite getSprite() {
		return sprite;
	}

	public void setSprite(UnitSprite sprite) {
		this.sprite = sprite;
	}

	public GameElement(int name, String spriteName, float spriteScale) {
		this.name = name;
		this.spriteName = spriteName;
		this.setSpriteScale(spriteScale);
	}

	public int getName() {
		return name;
	}

	public Tile getTilePosition() {
		return tilePosition;
	}

	public void setTilePosition(Battle battle, Tile tilePosition) {
		if (this.tilePosition != null) {
			this.tilePosition.setContent(null);
		}
		this.tilePosition = tilePosition;
		if (tilePosition != null) {
			tilePosition.setContent(this);
		}
	}

	public String getSpriteName() {
		return spriteName;
	}

	public Color getSelectionColor() {
		switch (rank) {
		case enemy:
			return new Color(1.0f, 0.0f, 0.0f, 0.7f);
		case ally:
			return new Color(1.0f, 1.0f, 1.0f, 0.7f);
		default:
			return new Color(1.0f, 1.0f, 0.0f, 0.7f);
		}
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
		if (rank == Rank.ally) {
			setVisible(true);
			sprite.setCanBeDragged(true);
		} else {
			setVisible(false);
			sprite.setCanBeDragged(false);
		}
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		getSprite().setVisible(isVisible);
	}

	public float getSpriteScale() {
		return spriteScale;
	}

	public void setSpriteScale(float spriteScale) {
		this.spriteScale = spriteScale;
	}

	public void setCurrentX(float currentX) {
		this.currentX = currentX;
	}

	public void setCurrentY(float currentY) {
		this.currentY = currentY;
	}

	public void setCurrentRotation(float currentRotation) {
		this.currentRotation = currentRotation;
	}

	public float getCurrentX() {
		return currentX;
	}

	public float getCurrentY() {
		return currentY;
	}

	public float getCurrentRotation() {
		return currentRotation;
	}

}
