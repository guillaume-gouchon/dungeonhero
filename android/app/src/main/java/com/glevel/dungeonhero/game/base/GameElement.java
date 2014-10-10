package com.glevel.dungeonhero.game.base;

import com.glevel.dungeonhero.game.graphics.GameElementSprite;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.io.Serializable;

public abstract class GameElement implements Serializable {

    private static final long serialVersionUID = -5880458091427517171L;

    private final int name;
    private final String spriteName;
    private Ranks rank;
    private transient Tile tilePosition;
    protected transient GameElementSprite sprite;

    public GameElement(int name, String spriteName, Ranks rank) {
        this.name = name;
        this.spriteName = spriteName;
        this.rank = rank;
    }

    public abstract void createSprite(VertexBufferObjectManager vertexBufferObjectManager);

    public GameElementSprite getSprite() {
        return sprite;
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
                return new Color(0.0f, 1.0f, 0.0f, 0.7f);
            default:
                return new Color(1.0f, 1.0f, 1.0f, 0.7f);
        }
    }

    public boolean isEnemy(GameElement gameElement) {
        return (rank == Ranks.ME || rank == Ranks.ALLY) && gameElement.getRank() == Ranks.ENEMY || rank == Ranks.ENEMY && (gameElement.getRank() == Ranks.ME || gameElement.getRank() == Ranks.ALLY);
    }

    public Ranks getRank() {
        return rank;
    }

    public void setRank(Ranks rank) {
        this.rank = rank;
    }

}
