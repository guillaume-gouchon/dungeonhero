package com.glevel.dungeonhero.models;

import org.andengine.util.color.Color;

public class FightResult {

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

    public enum States {
        MISS(Color.YELLOW), DAMAGE(Color.RED), BLOCK(new Color(0f, 0.8f, 0f)), DODGE(new Color(0f, 0.8f, 0f)), CRITICAL(Color.RED);

        private final Color color;

        States(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

}
