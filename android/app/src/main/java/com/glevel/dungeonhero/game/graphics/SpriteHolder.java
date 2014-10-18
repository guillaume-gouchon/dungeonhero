package com.glevel.dungeonhero.game.graphics;

/**
 * Created by guillaume on 10/15/14.
 */
public class SpriteHolder implements GraphicHolder {

    private final String spriteName;
    private final int spriteWidth, spriteHeight, nbSpritesX, nbSpritesY;

    public SpriteHolder(String spriteName, int spriteWidth, int spriteHeight, int nbSpritesX, int nbSpritesY) {
        this.spriteName = spriteName;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.nbSpritesX = nbSpritesX;
        this.nbSpritesY = nbSpritesY;
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
