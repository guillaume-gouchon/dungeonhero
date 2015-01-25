package com.glevel.dungeonhero.models;

import org.andengine.util.color.Color;

/**
 * Created by guillaume on 10/10/14.
 */
public enum Actions {
    MOVE(new Color(0.0f, 1.0f, 0.0f)), ATTACK(new Color(1.0f, 0.0f, 0.0f)), TALK(new Color(0.0f, 0.0f, 0.6f)), SEARCH(new Color(0.0f, 0.0f, 0.6f)), NONE(new Color(0.0f, 1.0f, 0.0f)), LIGHT(new Color(0.0f, 0.0f, 0.0f, 0.0f));

    private Color color;

    private Actions(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
