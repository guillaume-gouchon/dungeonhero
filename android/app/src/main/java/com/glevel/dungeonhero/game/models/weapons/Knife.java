package com.glevel.dungeonhero.game.models.weapons;

import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public class Knife extends Weapon {

    /**
     * 
     */
    private static final long serialVersionUID = 3632389181937576479L;

    public Knife() {
        super(0, 0, 2, 0, 10, 1, 10000, 8, 2, 3, new int[] { 40, 0, 0, 0 }, "close_combat");
    }

}
