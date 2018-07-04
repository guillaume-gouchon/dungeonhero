package com.glevel.dungeonhero.data.items;

import com.glevel.dungeonhero.models.effects.BuffEffect;
import com.glevel.dungeonhero.models.effects.CamouflageEffect;
import com.glevel.dungeonhero.models.effects.DamageEffect;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.HeroicEffect;
import com.glevel.dungeonhero.models.effects.PoisonEffect;
import com.glevel.dungeonhero.models.effects.RecoveryEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.consumables.Potion;

import org.andengine.util.color.Color;

/**
 * Created by guillaume ON 10/6/14.
 */
public class PotionFactory {

    public static Potion getRandomPotion() {
        int random = (int) (Math.random() * 11);
        switch (random) {
            case 0:
                return buildForcePotion();
            case 1:
                return buildGreaterHealingPotion();
            case 2:
                return buildHeroicPotion();
            case 3:
                return buildLuckPotion();
            case 4:
                return buildRecoveryPotion();
            case 5:
                return buildSpeedPotion();
            case 6:
            case 7:
                return buildHealingPotion();
            case 8:
                return buildMagicShieldPotion();
            case 9:
                return buildUnknownLiquid();
            default:
                return buildInvisibilityPotion();
        }
    }

    public static Potion buildForcePotion() {
        return new Potion("force_potion", new BuffEffect(null, Characteristics.DAMAGE, 10, 6, null, 0, Color.RED), 70);
    }

    private static Potion buildGreaterHealingPotion() {
        return new Potion("greater_healing_potion", new DamageEffect(null, 30, 0), 150);
    }

    public static Potion buildHealingPotion() {
        return new Potion("healing_potion", new DamageEffect(null, 15, 0), 50);
    }

    private static Potion buildHeroicPotion() {
        return new Potion("heroic_potion", new HeroicEffect("curse.png"), 100);
    }

    private static Potion buildInvisibilityPotion() {
        return new Potion("invisibility_potion", new CamouflageEffect(null, 0, 0), 80);
    }

    private static Potion buildLuckPotion() {
        return new Potion("luck_potion", new BuffEffect(null, Characteristics.DODGE, 50, 6, null, 0, Color.GREEN), 70);
    }

    private static Potion buildMagicShieldPotion() {
        return new Potion("magic_shield_potion", new BuffEffect(null, Characteristics.BLOCK, 40, 6, null, 0, Color.YELLOW), 60);
    }

    private static Potion buildRecoveryPotion() {
        return new Potion("recovery_potion", new RecoveryEffect("curse.png"), 120);
    }

    private static Potion buildSpeedPotion() {
        return new Potion("speed_potion", new BuffEffect(null, Characteristics.MOVEMENT, 6, 6, null, 0, Color.BLUE), 40);
    }

    private static Potion buildUnknownLiquid() {
        Effect effect;
        int random = (int) (Math.random() * 6);
        switch (random) {
            case 0:
                // full heal
                effect = new DamageEffect("curse.png", 100, null, 0);
                break;
            case 1:
                // dodge
                effect = new BuffEffect(null, Characteristics.DODGE, 50, 5, null, 0, Color.GREEN);
                break;
            case 2:
                // protection
                effect = new BuffEffect(null, Characteristics.PROTECTION, 50, 5, null, 0, Color.YELLOW);
                break;
            case 3:
            case 4:
                // poison
                effect = new PoisonEffect("poison.png", -3, 3, null, 0);
                break;
            default:
                // sleep
                effect = new StunEffect("stun.png", Characteristics.SPIRIT, 4, 0);
                break;
        }

        return new Potion("unknown_liquid", effect, 50);
    }

}
