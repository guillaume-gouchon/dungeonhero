package com.glevel.dungeonhero.models;

import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.skills.Skill;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class Buff implements Serializable {

    private static final long serialVersionUID = 3040363823518403336L;

    private final int value;
    private final Characteristics target;
    private final Skill special;

    private int duration;

    public Buff(int value, Characteristics target, Skill special) {
        this.value = value;
        this.duration = -1;
        this.target = target;
        this.special = special;
    }

    public Buff(int value, int duration, Characteristics target, Skill special) {
        this.value = value;
        this.duration = duration;
        this.target = target;
        this.special = special;
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

    public boolean consume() {
        if (duration != -1) {
            duration--;
            if (duration == 0) {
                return true;
            }
        }
        return false;
    }

    public Skill getSpecial() {
        return special;
    }
    
}
