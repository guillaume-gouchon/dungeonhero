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

/**
 * Created by guillaume ON 10/6/14.
 */
public class PotionFactory {

    public static Potion buildForcePotion() {
        Potion item = new Potion("force_potion", new BuffEffect("blood.png", Characteristics.DAMAGE, 10, 6, null));
        return item;
    }

    public static Potion buildGreaterHealingPotion() {
        Potion item = new Potion("greater_healing_potion", new DamageEffect("blood.png", 30));
        return item;
    }

    public static Potion buildHealingPotion() {
        Potion item = new Potion("healing_potion", new DamageEffect("blood.png", 10));
        return item;
    }

    public static Potion buildHeroicPotion() {
        Potion item = new Potion("heroic_potion", new HeroicEffect("blood.png"));
        return item;
    }

    public static Potion buildInvisibilityPotion() {
        Potion item = new Potion("invisibility_potion", new CamouflageEffect("blood.png", 5));
        return item;
    }

    public static Potion buildLuckPotion() {
        Potion item = new Potion("luck_potion", new BuffEffect("blood.png", Characteristics.DODGE, 50, 6, null));
        return item;
    }

    public static Potion buildMagicShieldPotion() {
        Potion item = new Potion("magic_shield_potion", new BuffEffect("blood.png", Characteristics.BLOCK, 40, 6, null));
        return item;
    }

    public static Potion buildRecoveryPotion() {
        Potion item = new Potion("recovery_potion", new RecoveryEffect("blood.png"));
        return item;
    }

    public static Potion buildSpeedPotion() {
        Potion item = new Potion("speed_potion", new BuffEffect("blood.png", Characteristics.MOVEMENT, 5, 6, null));
        return item;
    }

    public static Potion buildUnkwownLiquid() {
        Effect effect;
        int random = (int) (Math.random() * 6);
        switch (random) {
            case 0:
                effect = new DamageEffect("blood.png", 100, null);
                break;
            case 1:
                effect = new BuffEffect("blood.png", Characteristics.DODGE, 50, 5, null);
                break;
            case 2:
                effect = new BuffEffect("blood.png", Characteristics.PROTECTION, 50, 5, null);
                break;
            case 3:
                // poison
                effect = new PoisonEffect("blood.png", 2, 3, null);
                break;
            case 4:
                // sleep potion
                effect = new StunEffect("blood.png", Characteristics.SPIRIT, 5);
                break;
            default:
                // nothing
                effect = new BuffEffect(null, Characteristics.MOVEMENT, 0, 0, null);
                break;
        }

        Potion item = new Potion("speed_potion", effect);
        return item;
    }

}
