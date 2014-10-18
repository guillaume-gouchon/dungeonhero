package com.glevel.dungeonhero.game.base;

import com.glevel.dungeonhero.game.graphics.GameElementSprite;
import com.glevel.dungeonhero.game.graphics.GraphicHolder;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.io.Serializable;

public abstract class GameElement implements Serializable, GraphicHolder {

    private static final long serialVersionUID = -5880458091427517171L;

    private final int name;
    protected transient GameElementSprite sprite;
    private Ranks rank;
    protected transient Tile tilePosition;

    private final String spriteName;
    private final int spriteWidth, spriteHeight, nbSpritesX, nbSpritesY;

    public GameElement(int name, String spriteName, Ranks rank, int spriteWidth, int spriteHeight, int nbSpritesX, int nbSpritesY) {
        this.name = name;
        this.spriteName = spriteName;
        this.rank = rank;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.nbSpritesX = nbSpritesX;
        this.nbSpritesY = nbSpritesY;
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

    public void destroy() {
        setTilePosition(null);
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

    @Override
    public int getNbSpritesY() {
        return nbSpritesY;
    }

    @Override
    public int getSpriteWidth() {
        return spriteWidth;
    }

    @Override
    public int getSpriteHeight() {
        return spriteHeight;
    }

    @Override
    public int getNbSpritesX() {
        return nbSpritesX;
    }

}
