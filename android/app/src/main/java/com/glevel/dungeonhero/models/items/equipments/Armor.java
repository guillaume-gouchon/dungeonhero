package com.glevel.dungeonhero.models.items.equipments;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Armor extends Equipment implements Serializable {

    private static final long serialVersionUID = 4191323307341633727L;

    private final int protection;

    public Armor(String identifier, int protection, int level, int price) {
        super(identifier, level, (int) (price * (1 + (level * 0.25))));
        this.protection = protection;
    }

    public int getProtection() {
        return (int) Math.max(protection * (1 + 0.2 * level), protection + level);
    }


}
