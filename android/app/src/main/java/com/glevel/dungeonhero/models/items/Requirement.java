package com.glevel.dungeonhero.models.items;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class Requirement implements Serializable {

    private static final long serialVersionUID = 2763812604684913301L;

    private final Characteristics target;
    private final int value;

    public Requirement(Characteristics target, int value) {
        this.target = target;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Characteristics getTarget() {
        return target;
    }

}
