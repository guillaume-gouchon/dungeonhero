package com.glevel.dungeonhero.models.items.requirements;

import com.glevel.dungeonhero.models.items.Characteristics;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class StatRequirement extends Requirement implements Serializable {

    private static final long serialVersionUID = 5714245105140860799L;

    private final Characteristics target;
    private final int value;

    public StatRequirement(Characteristics target, int value, int level) {
        this.target = target;
        this.value = getValueWithLevelModifier(value, level);
    }

    public int getValue() {
        return value;
    }

    public Characteristics getTarget() {
        return target;
    }

    public static int getValueWithLevelModifier(int value, int level) {
        return (int) (value * (1 + 0.2 * level));
    }

}
