package com.glevel.dungeonhero.models.items;

/**
 * Created by guillaume on 19/10/14.
 */
public class Requirement {

    private final int value;
    private final Characteristics target;

    public Requirement(int value, Characteristics target) {
        this.value = value;
        this.target = target;
    }

    public int getValue() {
        return value;
    }

    public Characteristics getTarget() {
        return target;
    }

}
