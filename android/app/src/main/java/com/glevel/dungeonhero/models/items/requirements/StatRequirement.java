package com.glevel.dungeonhero.models.items.requirements;

import com.glevel.dungeonhero.models.items.Characteristics;

/**
 * Created by guillaume on 19/10/14.
 */
public class StatRequirement extends Requirement {

    private static final long serialVersionUID = -7523550796608275770L;

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
