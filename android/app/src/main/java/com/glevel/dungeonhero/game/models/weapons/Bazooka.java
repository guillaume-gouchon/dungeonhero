package com.glevel.dungeonhero.game.models.weapons;

import com.glevel.dungeonhero.game.models.weapons.categories.DeflectionWeapon;

public class Bazooka extends DeflectionWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 7774015670722062430L;

    public Bazooka(int name, int image, int apPower, int atPower, int range, int nbMagazines, int cadence,
            int magazineSize, int reloadSpeed, int shootSpeed, int explosionSize) {
        super(name, image, apPower, atPower, range, nbMagazines, cadence, magazineSize, reloadSpeed, shootSpeed,
                explosionSize, "bazooka");
    }

}
