package com.glevel.dungeonhero.models.items.equipments.weapons;

import com.glevel.dungeonhero.models.items.equipments.Equipment;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Weapon extends Equipment implements Serializable {

    private static final long serialVersionUID = 4191323307341633727L;

    private final int minDamage;
    private final int deltaDamage;

    public Weapon(String identifier, int minDamage, int deltaDamage, int level, int price) {
        super(identifier, level, (int) (price * (1 + (level * 0.25))));
        this.minDamage = minDamage;
        this.deltaDamage = deltaDamage;
    }

    public int getMinDamage() {
        return (int) (minDamage * (1 + 0.15 * level));
    }

    public int getDeltaDamage() {
        return (int) (deltaDamage * (1 + 0.15 * level));
    }

}
