package com.glevel.dungeonhero.game.base.interfaces;

public interface OnNewSpriteToDraw {

    public void drawSprite(float x, float y, String spriteName, int duration, int size);

    public void drawAnimatedSprite(float x, float y, String spriteName, int frameDuration, float scale, int loopCount, boolean removeAfter, int zIndex);

}
