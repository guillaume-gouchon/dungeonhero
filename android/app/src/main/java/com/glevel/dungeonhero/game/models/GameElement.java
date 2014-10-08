package com.glevel.dungeonhero.game.models;

import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

import java.io.Serializable;

public abstract class GameElement implements Serializable {

    private static final long serialVersionUID = -5880458091427517171L;

    private final int name;
    private final String spriteName;
    private Ranks rank;
    private transient Tile tilePosition;
    private transient Sprite sprite;

    public GameElement(int name, String spriteName, Ranks rank) {
        this.name = name;
        this.spriteName = spriteName;
        this.rank = rank;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getName() {
        return name;
    }

    public Tile getTilePosition() {
        return tilePosition;
    }

    public void setTilePosition(Tile tilePosition) {
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
            case ENEMY:
                return new Color(1.0f, 0.0f, 0.0f, 0.7f);
            case ALLY:
                return new Color(1.0f, 1.0f, 1.0f, 0.7f);
            default:
                return new Color(1.0f, 1.0f, 0.0f, 0.7f);
        }
    }

    public Ranks getRank() {
        return rank;
    }

    public void setRank(Ranks rank) {
        this.rank = rank;
    }

}
