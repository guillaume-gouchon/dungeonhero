package com.glevel.dungeonhero.game.models.weapons.categories;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.color.Color;

import com.glevel.dungeonhero.game.GameUtils;
import com.glevel.dungeonhero.game.andengine.custom.CustomColors;
import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.game.models.Battle;
import com.glevel.dungeonhero.game.models.map.Tile;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.game.models.units.categories.Vehicle;
import com.glevel.dungeonhero.game.models.units.categories.Unit.Action;

public abstract class DeflectionWeapon extends Weapon {

    /**
     * 
     */
    private static final long serialVersionUID = -3068800131021401992L;
    private static final float EXPLOSION_EPICENTER_SIZE_FACTOR = 0.3f;
    private static final int CHANCE_TO_HIT_IN_EPICENTER = 80;
    private static final int CHANCE_TO_HIT_AROUND_EPICENTER = 30;
    private static final int BASIC_MAXIMAL_DEFLECTION = 7;// in meters

    private final float explosionSize;// in meters

    public DeflectionWeapon(int name, int image, int apPower, int atPower, int range, int nbMagazines, int cadence,
            int magazineSize, int reloadSpeed, int shootSpeed, float explosionSize, String sound) {
        super(name, image, apPower, atPower, range, nbMagazines, cadence, magazineSize, reloadSpeed, shootSpeed, null,
                sound);
        this.explosionSize = explosionSize;
    }

    @Override
    public void resolveFireShot(Battle battle, Unit shooter, Unit target) {
        float distance = MapLogic.getDistanceBetween(shooter, target);

        // deflection depends on distance and experience of the shooter
        int deflection = (int) (Math.random() * (getMaxDeflection(shooter, distance)) * GameUtils.PIXEL_BY_METER);
        double angle = Math.random() * 360;

        // calculate impact position
        float[] impactPosition = MapLogic.getCoordinatesAfterTranslation(target.getSprite().getX(), target.getSprite()
                .getY(), deflection, angle, Math.random() < 0.5);

        // draw explosion sprite
        battle.getOnNewSprite().drawAnimatedSprite(impactPosition[0], impactPosition[1], "explosion.png", 40,
                3 * explosionSize * GameUtils.PIXEL_BY_METER / 64.0f, 0, true, 100);
        battle.getOnNewSoundToPlay().playSound("explosion", impactPosition[0], impactPosition[1]);

        // get all the units in the explosion
        Tile centerTile = MapLogic.getTileAtCoordinates(battle.getMap(), impactPosition[0], impactPosition[1]);
        if (centerTile != null) {
            int explosionSizeInTiles = (int) Math.ceil(explosionSize * GameUtils.PIXEL_BY_METER
                    / GameUtils.PIXEL_BY_TILE);
            List<Tile> adjacentTiles = MapLogic.getAdjacentTiles(battle.getMap(), centerTile, explosionSizeInTiles,
                    true);
            adjacentTiles.add(centerTile);

            List<Unit> hitUnits = new ArrayList<Unit>();

            for (Tile t : adjacentTiles) {
                if (t.getContent() != null) {
                    Unit unit = (Unit) t.getContent();

                    // units can be hit only once
                    if (hitUnits.contains(unit)) {
                        continue;
                    }

                    hitUnits.add(unit);

                    int distanceToImpact = MapLogic.getDistance(t, centerTile);// in
                                                                               // tiles

                    // increase panic
                    unit.getShots(battle, shooter);
                    if (distanceToImpact < explosionSizeInTiles * EXPLOSION_EPICENTER_SIZE_FACTOR) {
                        // great damage in the explosion's epicenter
                        resolveDamageDiceRoll(CHANCE_TO_HIT_IN_EPICENTER, shooter, unit);
                    } else {
                        // minor damage further
                        int tohit = CHANCE_TO_HIT_AROUND_EPICENTER;

                        // add terrain protection
                        tohit *= unit.getUnitTerrainProtection();

                        if (unit.getCurrentAction() == Action.HIDING || unit.getCurrentAction() == Action.RELOADING) {
                            // target is hiding : tohit depends on target's
                            // experience
                            tohit -= 5 * (unit.getExperience().ordinal() + 1);
                        }

                        resolveDamageDiceRoll(tohit, shooter, unit);
                    }

                    if (unit instanceof Vehicle && !unit.isDead()) {
                        battle.getOnNewSoundToPlay().playSound("clonk", unit.getSprite().getX(),
                                unit.getSprite().getY());
                    }
                }
            }
        }

    }

    private int getMaxDeflection(Unit shooter, float distance) {
        return BASIC_MAXIMAL_DEFLECTION + Weapon.distanceToRangeCategory(distance) - shooter.getExperience().ordinal();
    }

    @Override
    public Color getDistanceColor(Unit shooter, Unit target) {
        int defl = getMaxDeflection(shooter, MapLogic.getDistanceBetween(shooter, target)) - BASIC_MAXIMAL_DEFLECTION;
        if (defl <= -1) {
            return Color.GREEN;
        } else if (defl == 0) {
            return Color.YELLOW;
        } else if (defl == 1) {
            return CustomColors.ORANGE;
        } else {
            return Color.RED;
        }
    }

    public float getExplosionSize() {
        return explosionSize;
    }

}
