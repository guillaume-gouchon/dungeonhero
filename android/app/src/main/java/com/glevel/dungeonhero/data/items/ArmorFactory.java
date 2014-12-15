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

    public static Armor buildRobe(int level) {
        Armor item = new Armor("robe", 1, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        return item;
    }

    public static Armor buildLeatherVest(int level) {
        Armor item = new Armor("leather_vest", 2, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        return item;
    }

    public static Armor buildLeatherPlastron(int level) {
        Armor item = new Armor("leather_plastron", 3, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_SPI));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.DEX));
        item.addEffect(new PermanentEffect(Characteristics.DODGE, -5, null));
        return item;
    }

    public static Armor buildChainMail(int level) {
        Armor item = new Armor("chain_mail", 4, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 10));
        item.addEffect(new PermanentEffect(Characteristics.DODGE, -5, null));
        return item;
    }

    public static Armor buildLamellar(int level) {
        Armor item = new Armor("lamellar", 5, level);
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR_DEX));
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 13));
        item.addEffect(new PermanentEffect(Characteristics.DODGE, -15, null));
        return item;
    }

    public static Armor buildBreastPlate(int level) {
        Armor item = new Armor("breast_plate", 6, level);
        item.addRequirement(new StatRequirement(Characteristics.STRENGTH, 16));
        item.addRequirement(new HeroRequirement(Hero.HeroTypes.STR));
        item.addEffect(new PermanentEffect(Characteristics.DODGE, -20, null));
        item.addEffect(new PermanentEffect(Characteristics.MOVEMENT, -1, null));
        return item;
    }

}
