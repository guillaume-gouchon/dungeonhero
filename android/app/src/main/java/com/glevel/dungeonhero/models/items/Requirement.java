package com.glevel.dungeonhero.models.items;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class Requirement implements Serializable {

    private static final long serialVersionUID = 2763812604684913301L;

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
