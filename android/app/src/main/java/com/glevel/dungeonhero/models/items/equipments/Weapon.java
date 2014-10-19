package com.glevel.dungeonhero.models.items.equipments;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.items.Equipment;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Weapon extends Equipment implements Serializable {

    private static final long serialVersionUID = 4191323307341633727L;

    private final int minDamage;
    private final int deltaDamage;

    public Weapon(int name, int image, int minDamage, int deltaDamage) {
        super(name, image, R.color.bg_weapon);
        this.minDamage = minDamage;
        this.deltaDamage = deltaDamage;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getDeltaDamage() {
        return deltaDamage;
    }
    
}
