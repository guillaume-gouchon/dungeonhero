package com.glevel.dungeonhero.models.items.equipments.weapons;

import java.io.Serializable;

/**
 * Created by guillaume on 12/15/14.
 */
public class RangeWeapon extends TwoHandedWeapon implements Serializable {

    private static final long serialVersionUID = 4444072098726014881L;

    public RangeWeapon(String identifier, int minDamage, int deltaDamage, int level) {
        super(identifier, minDamage, deltaDamage, level);
    }

}
