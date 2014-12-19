package com.glevel.dungeonhero.models.items.equipments.weapons;

import java.io.Serializable;

/**
 * Created by guillaume on 12/15/14.
 */
public class TwoHandedWeapon extends Weapon implements Serializable {

    private static final long serialVersionUID = 7483693918022296123L;

    public TwoHandedWeapon(String identifier, int minDamage, int deltaDamage, int level, int price) {
        super(identifier, minDamage, deltaDamage, level, price);
    }

}
