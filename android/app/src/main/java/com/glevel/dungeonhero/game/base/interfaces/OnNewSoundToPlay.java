package com.glevel.dungeonhero.game.base.interfaces;

public interface OnNewSoundToPlay {

    public void playSound(String soundName, boolean isLooped);

    public void playGeoSound(String soundName, float x, float y);

}
