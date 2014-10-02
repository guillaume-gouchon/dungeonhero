package com.glevel.dungeonhero.game.models.weapons;

import com.glevel.dungeonhero.game.models.map.Tile.TerrainType;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.game.models.weapons.categories.IndirectWeapon;

public class Mortar extends IndirectWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 8787512407777567492L;

    public Mortar(int name, int image, int apPower, int atPower, int range, int nbMagazines, int cadence,
            int magazineSize, int reloadSpeed, int shootSpeed, int explosionSize) {
        super(name, image, apPower, atPower, range, nbMagazines, cadence, magazineSize, reloadSpeed, shootSpeed,
                explosionSize, "mortar");
    }

    @Override
    protected void resolveDamageDiceRoll(int tohit, Unit shooter, Unit target) {
        // mortars are useless on targets in houses
        if (range > 200 && target.getTilePosition().getTerrain() == TerrainType.house) {
            return;
        }

        super.resolveDamageDiceRoll(tohit, shooter, target);
    }

}
