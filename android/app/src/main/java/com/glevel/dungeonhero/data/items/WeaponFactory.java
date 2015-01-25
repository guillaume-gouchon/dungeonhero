package com.glevel.dungeonhero.data.items;

import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.effects.PoisonEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.equipments.Equipment;
import com.glevel.dungeonhero.models.items.equipments.weapons.RangeWeapon;
import com.glevel.dungeonhero.models.items.equipments.weapons.TwoHandedWeapon;
import com.glevel.dungeonhero.models.items.equipments.weapons.Weapon;
import com.glevel.dungeonhero.models.items.requirements.HeroRequirement;
import com.glevel.dungeonhero.models.items.requirements.StatRequirement;

/**
 * Created by guillaume ON 10/6/14.
 */
public class WeaponFactory {

    public static Equipment getRandomWeapon(int level) {
        int random = (int) (Math.random() * 16);
        switch (random) {
            case 0:
                return buildAxe(level);
            case 1:
                return buildBattleAxe(level);
            case 2:
                return buildBoomerang(level);
            case 3:
                return buildBow(level);
            case 4:
                return buildBroadSword(level);
            case 5:
                return buildCrossbow(level);
            case 6:
                return buildDagger(level);
            case 7:
                return buildLargeShield(level);
            case 8:
                return buildLongSword(level);
            case 9:
                return buildRoundShield(level);
            case 10:
                return buildShortSword(level);
            case 11:
                return buildShuriken(level);
            case 12:
                return buildSpear(level);
            case 13:
                return buildMorgenstern(level);
            case 14:
                return buildThrowingDaggers(level);
            default:
                return buildWizardStaff(level);
        }
    }

    public static Equipment buildAxe(int level) {
        Weapon item = new Weapon("axe", 2, 7, level, 200);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 9, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, -5, null, level));
        item.addEffect(new PermanentEffect(Characteristics.CRITICAL, 5, null, level));
        return item;
    }

    public static Equipment buildBattleAxe(int level) {
        TwoHandedWeapon item = new TwoHandedWeapon("battle_axe", 2, 14, level, 400);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 13, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, -10, null, level));
        item.addEffect(new PermanentEffect(Characteristics.CRITICAL, 15, null, level));
        return item;
    }

    public static Equipment buildBoomerang(int level) {
        RangeWeapon item = new RangeWeapon("boomerang", 2, 3, level, 300);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 13, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, -20, null, level));
        item.addEffect(new StunEffect("stun.png", Characteristics.STRENGTH, 0, level));
        return item;
    }

    public static Equipment buildBow(int level) {
        RangeWeapon item = new RangeWeapon("bow", 2, 6, level, 250);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 10, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, -10, null, level));
        return item;
    }

    public static Equipment buildBroadSword(int level) {
        TwoHandedWeapon item = new TwoHandedWeapon("broad_sword", 3, 10, level, 300);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 12, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, 5, null, level));
        item.addEffect(new PermanentEffect(Characteristics.CRITICAL, 10, null, level));
        return item;
    }

    public static Equipment buildCrossbow(int level) {
        RangeWeapon item = new RangeWeapon("crossbow", 2, 9, level, 400);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 10, level));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 10, level));
        item.addEffect(new PermanentEffect(Characteristics.CRITICAL, 10, null, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, -15, null, level));
        return item;
    }

    public static Equipment buildDagger(int level) {
        Weapon item = new Weapon("dagger", 2, 3, level, 100);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        return item;
    }

    public static Equipment buildLargeShield(int level) {
        Weapon item = new Weapon("large_shield", 1, 1, level, 220);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 12, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, 15, null, level));
        return item;
    }

    public static Equipment buildLongSword(int level) {
        Weapon item = new Weapon("long_sword", 3, 6, level, 250);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 10, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, 5, null, level));
        return item;
    }

    public static Equipment buildRoundShield(int level) {
        Weapon item = new Weapon("round_shield", 1, 1, level, 120);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 7, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, 10, null, level));
        return item;
    }

    public static Equipment buildShortSword(int level) {
        Weapon item = new Weapon("short_sword", 2, 5, level, 150);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 6, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, 5, null, level));
        return item;
    }

    public static Equipment buildShuriken(int level) {
        RangeWeapon item = new RangeWeapon("shuriken", 2, 3, level, 160);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 12, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, -20, null, level));
        item.addEffect(new PermanentEffect(Characteristics.CRITICAL, 20, null, level));
        return item;
    }

    public static Equipment buildSpear(int level) {
        TwoHandedWeapon item = new TwoHandedWeapon("spear", 2, 7, level, 150);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, -10, null, level));
        item.addEffect(new PermanentEffect(Characteristics.CRITICAL, 10, null, level));
        return item;
    }

    public static Equipment buildMorgenstern(int level) {
        Weapon item = new Weapon("spiked_mace", 2, 9, level, 280);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 12, level));
        item.addEffect(new PermanentEffect(Characteristics.CRITICAL, 20, null, level));
        return item;
    }

    public static Equipment buildThrowingDaggers(int level) {
        RangeWeapon item = new RangeWeapon("throwing_daggers", 2, 3, level, 120);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 10, level));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, -20, null, level));
        return item;
    }

    public static Equipment buildWizardStaff(int level) {
        TwoHandedWeapon item = new TwoHandedWeapon("wizard_staff", 2, 5, level, 100);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, 15, null, level));
        return item;
    }

    public static Equipment buildSwordOfPotential() {
        Weapon item = new Weapon("potential_sword", 4, 7, 0, 800);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 10, 0));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, 5, null, 0));
        item.addEffect(new PoisonEffect("elec.png", -2, 1, null, 0));
        return item;
    }

    public static Equipment buildElvenThrowingDaggers() {
        Weapon item = new RangeWeapon("elven_throwing_daggers", 2, 5, 0, 500);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 10, 0));
        item.addEffect(new PermanentEffect(Characteristics.BLOCK, -20, null, 0));
        item.addEffect(new PoisonEffect("poison.png", -2, 2, null, 0));
        return item;
    }

    public static Equipment buildDarkElvenDagger() {
        Weapon item = new Weapon("dark_elven_dagger", 3, 3, 0, 400);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addEffect(new PermanentEffect(Characteristics.CRITICAL, 10, null, 0));
        item.addEffect(new PoisonEffect("poison.png", -2, 3, null, 0));
        return item;
    }

}
