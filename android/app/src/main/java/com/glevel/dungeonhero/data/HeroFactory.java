package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
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
        lst.add(buildElf());
        lst.add(buildWizard());
        lst.add(buildThief());
        lst.add(buildWarrior());
        lst.add(buildDruid());
        return lst;
    }

    public static Hero buildBerserker() {
        Hero hero = new Hero(Ranks.ME, R.drawable.warrior, "warrior.png", 18, 18, 14, 9, 6, 5, R.string.berserker, R.string.berserker_description, 10, null, 0, 1);
        hero.getPassive().add(new Rage());
        hero.equip(WeaponFactory.buildSword());
        hero.addItem(WeaponFactory.buildSword());
        return hero;
    }

    public static Hero buildWarrior() {
        Hero hero = new Hero(Ranks.ME, R.drawable.dwarf, "dwarf.png", 22, 22, 13, 7, 8, 5, R.string.warrior, R.string.warrior_description, 10, null, 0, 1);
        hero.getPassive().add(new Nemesis(R.string.goblin));
        return hero;
    }

    public static Hero buildElf() {
        return new Hero(Ranks.ME, R.drawable.elf, "elf.png", 12, 12, 8, 12, 11, 6, R.string.ranger, R.string.ranger_description, 10, null, 0, 1);
    }

    public static Hero buildWizard() {
        return new Hero(Ranks.ME, R.drawable.wizard, "wizard.png", 10, 10, 6, 10, 14, 5, R.string.wizard, R.string.wizard_description, 10, null, 0, 1);
    }

    public static Hero buildDruid() {
        return new Hero(Ranks.ME, R.drawable.druid, "druid.png", 14, 14, 9, 9, 12, 5, R.string.druid, R.string.druid_description, 10, null, 0, 1);
    }

    public static Hero buildThief() {
        return new Hero(Ranks.ME, R.drawable.thief, "thief.png", 15, 15, 8, 12, 10, 6, R.string.thief, R.string.thief_description, 10, null, 0, 1);
    }

}
