package com.glevel.dungeonhero.game.models.weapons;

import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public class HMG extends Weapon {

    /**
     * 
     */
    private static final long serialVersionUID = -1692340744939005519L;

    public HMG(int name, int image, int apPower, int atPower, int range, int nbMagazines, int cadence,
            int magazineSize, int reloadSpeed, int shootSpeed, int[] accuracy) {
        super(name, image, apPower, atPower, range, nbMagazines, cadence, magazineSize, reloadSpeed, shootSpeed, accuracy, "mg");
    }

}
