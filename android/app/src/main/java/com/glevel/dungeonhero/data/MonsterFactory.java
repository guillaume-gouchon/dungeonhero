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
        Monster monster = new Monster(R.drawable.goblin, "goblin.png", 5, 5, 6, 8, 2, 7, R.string.goblin, R.string.choose_hero_message);
        monster.equip(WeaponFactory.buildSword());
        return monster;
    }

    public static Monster buildChaosWarrior() {
        return new Monster(R.drawable.chaos_warrior, "chaos_warrior.png", 5, 5, 6, 10, 2, 7, R.string.chaos_warrior, R.string.choose_hero_message);
    }

    public static Monster buildChaosWizard() {
        return new Monster(R.drawable.chaos_wizard, "chaos_wizard.png", 5, 5, 6, 10, 2, 7, R.string.chaos_wizard, R.string.choose_hero_message);
    }

    public static Monster buildDemon() {
        return new Monster(R.drawable.demon, "demon.png", 5, 5, 6, 10, 2, 7, R.string.demon, R.string.choose_hero_message);
    }

    public static Monster buildDemonKing() {
        return new Monster(R.drawable.demon_king, "demon_king.png", 5, 5, 6, 10, 2, 7, R.string.demon_king, R.string.choose_hero_message);
    }

    public static Monster buildGargoyle() {
        return new Monster(R.drawable.gargoyle, "gargoyle.png", 5, 5, 6, 10, 2, 7, R.string.gargoyle, R.string.choose_hero_message);
    }

    public static Monster buildGoblinKing() {
        return new Monster(R.drawable.goblin2, "goblin2.png", 5, 5, 6, 10, 2, 7, R.string.goblin_king, R.string.choose_hero_message);
    }

    public static Monster buildOgre() {
        return new Monster(R.drawable.ogre, "ogre.png", 5, 5, 6, 10, 2, 7, R.string.ogre, R.string.choose_hero_message);
    }

    public static Monster buildOgreKing() {
        return new Monster(R.drawable.ogre2, "ogre2.png", 5, 5, 6, 10, 2, 7, R.string.ogre_king, R.string.choose_hero_message);
    }

    public static Monster buildOrc() {
        return new Monster(R.drawable.orc, "orc.png", 5, 5, 6, 10, 2, 7, R.string.orc, R.string.choose_hero_message);
    }

    public static Monster buildOrcCaptain() {
        return new Monster(R.drawable.orc2, "orc2.png", 5, 5, 6, 10, 2, 7, R.string.orc_captain, R.string.choose_hero_message);
    }

    public static Monster buildTroll() {
        return new Monster(R.drawable.troll, "troll.png", 5, 5, 6, 10, 2, 7, R.string.troll, R.string.choose_hero_message);
    }

    public static List<Monster> getRoomContent(int threatLevel) {
        List<Monster> l = new ArrayList<>();
        int nbMonsters;
        int random = (int) (Math.random() * 10);
        if (random < 1) {
            nbMonsters = 0;
        } else if (random < 8) {
            nbMonsters = 1;
        } else {
            nbMonsters = 2;
        }
        for (int n = 0; n < nbMonsters; n++) {
            l.add(getRandomMonster(threatLevel));
        }
        return l;
    }

    public static Monster getRandomMonster(int threatLevel) {
        int diceRoll = (int) (2 + Math.floor(Math.random() * 10));
        switch (threatLevel) {
            case 1:
                switch (diceRoll) {
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        return buildGoblin();
                    case 6:
                    case 7:
                    case 8:
                        return buildOrc();
                    case 9:
                    case 10:
                    case 11:
                        return buildGoblinKing();
                    case 12:
                        return buildOrcCaptain();
                }
            case 2:
                switch (diceRoll) {
                    case 2:
                    case 3:
                        return buildOrc();
                    case 4:
                    case 5:
                        return buildOrcCaptain();
                    case 6:
                    case 7:
                        return buildTroll();
                    case 8:
                    case 9:
                        return buildOgre();
                    case 10:
                    case 11:
                        return buildOgreKing();
                    case 12:
                        return buildGargoyle();
                }
        }
        return null;
    }

}
