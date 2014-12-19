package com.glevel.dungeonhero.data.items;

import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.equipments.Armor;
import com.glevel.dungeonhero.models.items.requirements.HeroRequirement;
import com.glevel.dungeonhero.models.items.requirements.StatRequirement;

/**
 * Created by guillaume ON 10/6/14.
 */
public class ArmorFactory {

    public static Armor getRandomArmor(int level) {
        int random = (int) (Math.random() * 6);
        switch (random) {
            case 0:
                return buildRobe(level);
            case 1:
                return buildLeatherVest(level);
            case 2:
                return buildLeatherPlastron(level);
            case 3:
                return buildChainMail(level);
            case 4:
                return buildLamellar(level);
            default:
                return buildBreastPlate(level);
        }
    }

    public static Armor buildRobe(int level) {
        Armor item = new Armor("robe", 2, level, 150);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        return item;
    }

    public static Armor buildLeatherVest(int level) {
        Armor item = new Armor("leather_vest", 4, level, 180);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        return item;
    }

    public static Armor buildLeatherPlastron(int level) {
        Armor item = new Armor("leather_plastron", 6, level, 200);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addEffect(new PermanentEffect(Characteristics.DODGE, -5, null, level));
        return item;
    }

    public static Armor buildChainMail(int level) {
        Armor item = new Armor("chain_mail", 8, level, 250);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 10, level));
        item.addEffect(new PermanentEffect(Characteristics.DODGE, -5, null, level));
        return item;
    }

    public static Armor buildLamellar(int level) {
        Armor item = new Armor("lamellar", 10, level, 250);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 13, level));
        item.addEffect(new PermanentEffect(Characteristics.DODGE, -15, null, level));
        return item;
    }

    public static Armor buildBreastPlate(int level) {
        Armor item = new Armor("breast_plate", 12, level, 350);
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 16, level));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addEffect(new PermanentEffect(Characteristics.DODGE, -20, null, level));
        item.addEffect(new PermanentEffect(Characteristics.MOVEMENT, -1, null, level));
        return item;
    }

}
