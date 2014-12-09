package com.glevel.dungeonhero.models;

import org.andengine.util.color.Color;

/**
 * Created by guillaume on 10/14/14.
 */
public class FightResult {

    public enum States {
        DAMAGE(Color.RED), BLOCK(Color.CYAN), DODGE(Color.CYAN), CRITICAL(Color.RED);

        private Color color;

        private States(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    private final int damage;
    private final States state;

    public FightResult(States state, int damage) {
        this.state = state;
        this.damage = damage;
    }

    public States getState() {
        return state;
    }

    public int getDamage() {
        return damage;
    }

}
