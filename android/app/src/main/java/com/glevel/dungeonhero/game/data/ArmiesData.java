package com.glevel.dungeonhero.game.data;

import android.graphics.Color;

import com.glevel.dungeonhero.R;

public enum ArmiesData {
    USA(R.string.usa_army, R.drawable.ic_army_usa, Color.GREEN), GERMANY(R.string.german_army,
            R.drawable.ic_army_germany, Color.BLACK);

    private final int name;
    private final int flagImage;
    private final int color;

    ArmiesData(int name, int flagImage, int color) {
        this.name = name;
        this.flagImage = flagImage;
        this.color = color;
    }

    public int getName() {
        return name;
    }

    public int getFlagImage() {
        return flagImage;
    }

    public int getColor() {
        return color;
    }

    public ArmiesData getEnemy() {
        if (ordinal() == 0) {
            return ArmiesData.values()[1];
        } else {
            return ArmiesData.values()[0];
        }
    }

    public boolean isAlly() {
        return this == ArmiesData.USA;
    }

}
