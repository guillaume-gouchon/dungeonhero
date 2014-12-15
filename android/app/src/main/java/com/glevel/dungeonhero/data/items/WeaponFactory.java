package com.glevel.dungeonhero.data.items;

import com.glevel.dungeonhero.models.characters.Hero;
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

    public static Equipment buildAxe(int level) {
        Weapon item = new Weapon("axe", 8, 8, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 10));
        return item;
    }

    public static Equipment buildBattleAxe(int level) {
        TwoHandedWeapon item = new TwoHandedWeapon("battle_axe", 12, 24, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 12));
        return item;
    }

    public static Equipment buildBoomerang(int level) {
        RangeWeapon item = new RangeWeapon("boomerang", 5, 10, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 13));
        return item;
    }

    public static Equipment buildBow(int level) {
        RangeWeapon item = new RangeWeapon("bow", 8, 14, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 12));
        return item;
    }

    public static Equipment buildBroadSword(int level) {
        TwoHandedWeapon item = new TwoHandedWeapon("broad_sword", 14, 18, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 12));
        return item;
    }

    public static Equipment buildCrossbow(int level) {
        RangeWeapon item = new RangeWeapon("crossbow", 15, 20, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 8));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 12));
        return item;
    }

    public static Equipment buildDagger(int level) {
        Weapon item = new Weapon("dagger", 5, 8, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        return item;
    }

    public static Equipment buildLargeShield(int level) {
        Weapon item = new Weapon("large_shield", 2, 3, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 12));
        return item;
    }

    public static Equipment buildLongSword(int level) {
        Weapon item = new Weapon("long_sword", 2, 3, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 10));
        return item;
    }

    public static Equipment buildRoundShield(int level) {
        Weapon item = new Weapon("round_shield", 1, 2, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 7));
        return item;
    }

    public static Equipment buildShortSword(int level) {
        Weapon item = new Weapon("short_sword", 6, 9, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 6));
        return item;
    }

    public static Equipment buildShuriken(int level) {
        RangeWeapon item = new RangeWeapon("shuriken", 5, 7, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 12));
        return item;
    }

    public static Equipment buildSpear(int level) {
        TwoHandedWeapon item = new TwoHandedWeapon("spear", 9, 15, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        return item;
    }

    public static Equipment buildMorgenstern(int level) {
        Weapon item = new Weapon("spiked_mace", 10, 15, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 12));
        return item;
    }

    public static Equipment buildThrowingDaggers(int level) {
        RangeWeapon item = new RangeWeapon("throwing_daggers", 7, 9, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new StatRequirement(Characteristics.DEXTERITY, 10));
        return item;
    }

    public static Equipment buildWizardStaff(int level) {
        TwoHandedWeapon item = new TwoHandedWeapon("wizard_staff", 5, 7, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        return item;
    }

}
