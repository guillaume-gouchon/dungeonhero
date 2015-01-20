package com.glevel.dungeonhero.data.characters;

import com.glevel.dungeonhero.data.items.ArmorFactory;
import com.glevel.dungeonhero.data.items.WeaponFactory;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Ranks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class HeroFactory {

    public static List<Hero> getAll() {
        List<Hero> lst = new ArrayList<Hero>();
        lst.add(buildBerserker());
        lst.add(buildElfRanger());
        lst.add(buildWizard());
        lst.add(buildThief());
        lst.add(buildDwarfWarrior());
        lst.add(buildDruid());
        return lst;
    }

    public static Hero buildBerserker() {
        Hero hero = new Hero("berserker", Ranks.ME, 22, 22, 13, 11, 6, 4, null, 0, 1, Hero.HeroTypes.STR_DEX);
        hero.equip(WeaponFactory.buildBroadSword(0));
        hero.equip(ArmorFactory.buildLeatherVest(0));
        hero.getSkills().add(SkillFactory.buildRage());
        hero.getSkills().add(SkillFactory.buildSwirlOfSwords());
        hero.getSkills().add(SkillFactory.buildWarcry());
        return hero;
    }

    public static Hero buildDwarfWarrior() {
        Hero hero = new Hero("dwarf_warrior", Ranks.ME, 28, 28, 15, 6, 9, 3, "dwarf_warrior", 0, 1, Hero.HeroTypes.STR);
        hero.equip(WeaponFactory.buildAxe(0));
        hero.equip(WeaponFactory.buildRoundShield(0));
        hero.equip(ArmorFactory.buildLeatherVest(0));
        hero.getSkills().add(SkillFactory.buildGroundSlam());
        hero.getSkills().add(SkillFactory.buildParryScience());
        hero.getSkills().add(SkillFactory.buildDrunkenMaster());
        return hero;
    }

    public static Hero buildElfRanger() {
        Hero hero = new Hero("elf_ranger", Ranks.ME, 16, 16, 6, 13, 11, 5, "elf_ranger", 0, 1, Hero.HeroTypes.DEX_SPI);
        hero.equip(WeaponFactory.buildBow(0));
        hero.equip(ArmorFactory.buildLeatherVest(0));
        hero.getSkills().add(SkillFactory.buildFrostArrow());
        hero.getSkills().add(SkillFactory.buildCharm());
        hero.getSkills().add(SkillFactory.buildDodgeMaster());
        hero.getSkills().add(SkillFactory.buildStarFall());
        return hero;
    }

    public static Hero buildWizard() {
        Hero hero = new Hero("wizard", Ranks.ME, 16, 16, 6, 10, 14, 4, "wizard", 0, 1, Hero.HeroTypes.SPI);
        hero.equip(WeaponFactory.buildWizardStaff(0));
        hero.equip(ArmorFactory.buildRobe(0));
        hero.getSkills().add(SkillFactory.buildFireball());
        hero.getSkills().add(SkillFactory.buildSleep());
        hero.getSkills().add(SkillFactory.buildThunderStorm());
        hero.getSkills().add(SkillFactory.buildStoneSkin());
        hero.getSkills().add(SkillFactory.buildTerror());
        return hero;
    }

    public static Hero buildDruid() {
        Hero hero = new Hero("druid", Ranks.ME, 18, 18, 11, 8, 11, 4, "druid", 0, 1, Hero.HeroTypes.STR_SPI);
        hero.equip(WeaponFactory.buildWizardStaff(0));
        hero.equip(ArmorFactory.buildLeatherVest(0));
        hero.getSkills().add(SkillFactory.buildParalysingPlants());
        hero.getSkills().add(SkillFactory.buildHealingHerbs());
        hero.getSkills().add(SkillFactory.buildWolfHowl());
        hero.getSkills().add(SkillFactory.buildCrowCurse());
        return hero;
    }

    public static Hero buildThief() {
        Hero hero = new Hero("thief", Ranks.ME, 20, 20, 8, 14, 8, 5, "thief", 0, 1, Hero.HeroTypes.DEX);
        hero.equip(WeaponFactory.buildDagger(0));
        hero.equip(ArmorFactory.buildLeatherVest(0));
        hero.getSkills().add(SkillFactory.buildCamouflage());
        hero.getSkills().add(SkillFactory.buildFatalBlow());
        hero.getSkills().add(SkillFactory.buildPoisonousDarts());
        hero.getSkills().add(SkillFactory.buildDodgeMaster());
        return hero;
    }

}
