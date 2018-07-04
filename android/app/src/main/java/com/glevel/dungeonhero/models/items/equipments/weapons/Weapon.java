package com.glevel.dungeonhero.models.items.equipments.weapons;

import com.glevel.dungeonhero.models.items.equipments.Equipment;

public class Weapon extends Equipment {

    private static final long serialVersionUID = -6732185202456111203L;
    private final int minDamage;
    private final int deltaDamage;

    public Weapon(String identifier, int minDamage, int deltaDamage, int level, int price) {
        super(identifier, level, (int) (price * (1 + (level * 0.25))));
        this.minDamage = minDamage;
        this.deltaDamage = deltaDamage;
    }

    public int getMinDamage() {
        return (int) Math.max(minDamage * (1 + 0.15 * level), minDamage + level);
    }

    public int getDeltaDamage() {
        return (int) Math.max(deltaDamage * (1 + 0.15 * level), deltaDamage + level);
    }

}
