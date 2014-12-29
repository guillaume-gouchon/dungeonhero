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
        Potion item = new Potion("force_potion", new BuffEffect(null, Characteristics.DAMAGE, 10, 6, null, 0, Color.RED), 70);
        return item;
    }

    public static Potion buildGreaterHealingPotion() {
        Potion item = new Potion("greater_healing_potion", new DamageEffect(null, 30, 0), 150);
        return item;
    }

    public static Potion buildHealingPotion() {
        Potion item = new Potion("healing_potion", new DamageEffect(null, 10, 0), 50);
        return item;
    }

    public static Potion buildHeroicPotion() {
        Potion item = new Potion("heroic_potion", new HeroicEffect(null), 100);
        return item;
    }

    public static Potion buildInvisibilityPotion() {
        Potion item = new Potion("invisibility_potion", new CamouflageEffect(null, 10, 0), 80);
        return item;
    }

    public static Potion buildLuckPotion() {
        Potion item = new Potion("luck_potion", new BuffEffect(null, Characteristics.DODGE, 50, 6, null, 0, Color.GREEN), 70);
        return item;
    }

    public static Potion buildMagicShieldPotion() {
        Potion item = new Potion("magic_shield_potion", new BuffEffect(null, Characteristics.BLOCK, 40, 6, null, 0, Color.YELLOW), 60);
        return item;
    }

    public static Potion buildRecoveryPotion() {
        Potion item = new Potion("recovery_potion", new RecoveryEffect(null), 120);
        return item;
    }

    public static Potion buildSpeedPotion() {
        Potion item = new Potion("speed_potion", new BuffEffect(null, Characteristics.MOVEMENT, 5, 6, null, 0, Color.BLUE), 40);
        return item;
    }

    public static Potion buildUnknownLiquid() {
        Effect effect;
        int random = (int) (Math.random() * 6);
        switch (random) {
            case 0:
                effect = new DamageEffect("poison.png", 100, null, 0);
                break;
            case 1:
                effect = new BuffEffect(null, Characteristics.DODGE, 50, 5, null, 0, Color.GREEN);
                break;
            case 2:
                effect = new BuffEffect(null, Characteristics.PROTECTION, 50, 5, null, 0, Color.YELLOW);
                break;
            case 3:
                // poison
                effect = new PoisonEffect("poison.png", 2, 3, null, 0);
                break;
            case 4:
                // sleep potion
                effect = new StunEffect("stun.png", Characteristics.SPIRIT, 5, 0);
                break;
            default:
                // nothing
                effect = new BuffEffect(null, Characteristics.MOVEMENT, 0, 0, null, 0);
                break;
        }

        Potion item = new Potion("unknown_liquid", effect, 50);
        return item;
    }

}
