package com.glevel.dungeonhero.data.characters;

import com.glevel.dungeonhero.models.characters.Monster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class MonsterFactory {

    public static List<Monster> getAll() {
        List<Monster> lst = new ArrayList<>();
        lst.add(buildGoblin());
        lst.add(buildChaosWarrior());
        lst.add(buildChaosWizard());
        lst.add(buildDemon());
        lst.add(buildDemonKing());
        lst.add(buildGargoyle());
        lst.add(buildOgre());
        lst.add(buildOgreKing());
        lst.add(buildOrc());
        lst.add(buildOrcCaptain());
        lst.add(buildTroll());
        return lst;
    }

    public static Monster buildGoblin() {
        Monster monster = new Monster("goblin", 5, 5, 6, 12, 7, 5);
        return monster;
    }

    public static Monster buildOrc() {
        Monster monster = new Monster("orc", 10, 10, 11, 9, 5, 4);
        return monster;
    }

    public static Monster buildOrcCaptain() {
        Monster monster = new Monster("orc_captain", 13, 13, 13, 9, 7, 4);
        return monster;
    }

    public static Monster buildTroll() {
        Monster monster = new Monster("troll", 22, 22, 15, 8, 2, 3);
        return monster;
    }

    public static Monster buildChaosWarrior() {
        Monster monster = new Monster("chaos_warrior", 17, 17, 12, 9, 10, 4);
        return monster;
    }

    public static Monster buildChaosWizard() {
        Monster monster = new Monster("chaos_wizard", 5, 5, 6, 8, 2, 7);
        return monster;
    }

    public static Monster buildDemon() {
        return new Monster("demon", 5, 5, 6, 10, 2, 7);
    }

    public static Monster buildDemonKing() {
        return new Monster("demon_king", 5, 5, 6, 10, 2, 7);
    }

    public static Monster buildGargoyle() {
        return new Monster("gargoyle", 5, 5, 6, 10, 2, 7);
    }

    public static Monster buildOgre() {
        return new Monster("ogre", 5, 5, 6, 10, 2, 7);
    }

    public static Monster buildOgreKing() {
        return new Monster("ogre_king", 5, 5, 6, 10, 2, 7);
    }


    public static List<Monster> getRoomContent(int threatLevel) {
        List<Monster> l = new ArrayList<>();
        int nbMonsters;
        int random = (int) (Math.random() * 10);
        if (random < 1) {
            nbMonsters = 0;
        } else if (random < 5) {
            nbMonsters = 1;
        } else if (random < 8) {
            nbMonsters = 2;
        } else {
            nbMonsters = 3;
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
                        return buildGoblin();
                    case 5:
                    case 6:
                    case 7:
                        return buildOrc();
                    case 8:
                    case 9:
                        return buildOrcCaptain();
                    case 10:
                    case 11:
                        return buildTroll();
                    case 12:
                        return buildChaosWarrior();
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
