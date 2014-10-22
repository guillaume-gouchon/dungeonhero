package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Monster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class MonsterFactory {

    public static List<Monster> getAll() {
        List<Monster> lst = new ArrayList<Monster>();
        lst.add(buildGoblin());
        lst.add(buildChaosWarrior());
        lst.add(buildChaosWizard());
        lst.add(buildDemon());
        lst.add(buildDemonKing());
        lst.add(buildGargoyle());
        lst.add(buildGoblinKing());
        lst.add(buildOgre());
        lst.add(buildOgreKing());
        lst.add(buildOrc());
        lst.add(buildOrcCaptain());
        lst.add(buildTroll());
        return lst;
    }

    public static Monster buildGoblin() {
        Monster monster = new Monster(R.drawable.goblin, "goblin.png", 5, 5, 6, 8, 2, 8, 8, 7, R.string.goblin, R.string.choose_hero_message, 10);
        monster.equip(WeaponFactory.buildSword());
        return monster;
    }

    public static Monster buildChaosWarrior() {
        return new Monster(R.drawable.chaos_warrior, "chaos_warrior.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.chaos_warrior, R.string.choose_hero_message, 10);
    }

    public static Monster buildChaosWizard() {
        return new Monster(R.drawable.chaos_wizard, "chaos_wizard.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.chaos_wizard, R.string.choose_hero_message, 10);
    }

    public static Monster buildDemon() {
        return new Monster(R.drawable.demon, "demon.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.demon, R.string.choose_hero_message, 10);
    }

    public static Monster buildDemonKing() {
        return new Monster(R.drawable.demon_king, "demon_king.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.demon_king, R.string.choose_hero_message, 10);
    }

    public static Monster buildGargoyle() {
        return new Monster(R.drawable.gargoyle, "gargoyle.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.gargoyle, R.string.choose_hero_message, 10);
    }

    public static Monster buildGoblinKing() {
        return new Monster(R.drawable.goblin2, "goblin2.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.goblin_king, R.string.choose_hero_message, 10);
    }

    public static Monster buildOgre() {
        return new Monster(R.drawable.ogre, "ogre.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.ogre, R.string.choose_hero_message, 10);
    }

    public static Monster buildOgreKing() {
        return new Monster(R.drawable.ogre2, "ogre2.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.ogre_king, R.string.choose_hero_message, 10);
    }

    public static Monster buildOrc() {
        return new Monster(R.drawable.orc, "orc.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.orc, R.string.choose_hero_message, 10);
    }

    public static Monster buildOrcCaptain() {
        return new Monster(R.drawable.orc2, "orc2.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.orc_captain, R.string.choose_hero_message, 10);
    }

    public static Monster buildTroll() {
        return new Monster(R.drawable.troll, "troll.png", 5, 5, 6, 10, 2, 8, 8, 7, R.string.troll, R.string.choose_hero_message, 10);
    }

}
