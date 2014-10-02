package com.glevel.dungeonhero.game.models.weapons.categories;

public abstract class IndirectWeapon extends DeflectionWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 2274584769324690466L;

    public IndirectWeapon(int name, int image, int apPower, int atPower, int range, int nbMagazines, int cadence,
            int magazineSize, int reloadSpeed, int shootSpeed, int explosionSize, String sound) {
        super(name, image, apPower, atPower, range, nbMagazines, cadence, magazineSize, reloadSpeed, shootSpeed,
                explosionSize, sound);
    }

}
