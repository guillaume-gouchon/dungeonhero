package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public abstract class Effect implements Serializable {

    private static final long serialVersionUID = 3040363823518403336L;

    public static final int INSTANT_EFFECT = 0;
    public static final int INFINITE_EFFECT = -1;

    private final String spriteName;
    private final int value;
    private final Characteristics target;
    private final Effect special;
    private int duration;

    public Effect(String spriteName, Characteristics target, int value, int duration, Effect special) {
        this.spriteName = spriteName;
        this.value = value;
        this.duration = duration;
        this.target = target;
        this.special = special;
    }

    public boolean consume() {
        if (duration == INSTANT_EFFECT) {
            return true;
        }
        if (duration != INFINITE_EFFECT) {
            duration--;
            if (duration == 0) {
                return true;
            }
        }
        return false;
    }

    public int getValue() {
        return value;
    }

    public Characteristics getTarget() {
        return target;
    }

    public int getDuration() {
        return duration;
    }

    public Effect getSpecial() {
        return special;
    }

    public String getSpriteName() {
        return spriteName;
    }

}
