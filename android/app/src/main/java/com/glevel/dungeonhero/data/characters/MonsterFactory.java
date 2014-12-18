package com.glevel.dungeonhero.data.characters;

import com.glevel.dungeonhero.data.items.ArmorFactory;
import com.glevel.dungeonhero.data.items.RingFactory;
import com.glevel.dungeonhero.data.items.WeaponFactory;
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
        Monster monster = new Monster("goblin", 5, 5, 6, 12, 6, 5);
        monster.equip(WeaponFactory.buildDagger(0));
        return monster;
    }

    public static Monster buildOrc() {
        Monster monster = new Monster("orc", 10, 10, 11, 9, 5, 4);
        monster.equip(WeaponFactory.buildShortSword(0));
        monster.equip(ArmorFactory.buildLeatherVest(0));
        return monster;
    }

    public static Monster buildOrcCaptain() {
        Monster monster = new Monster("orc_captain", 13, 13, 13, 9, 7, 4);
        monster.equip(WeaponFactory.buildLongSword(0));
        monster.equip(WeaponFactory.buildRoundShield(0));
        monster.equip(ArmorFactory.buildLeatherPlastron(0));
        return monster;
    }

    public static Monster buildTroll() {
        Monster monster = new Monster("troll", 22, 22, 15, 8, 2, 3);
        monster.equip(WeaponFactory.buildMorgenstern(0));
        return monster;
    }

    public static Monster buildOgre() {
        Monster monster = new Monster("ogre", 16, 16, 13, 9, 5, 4);
        monster.equip(WeaponFactory.buildMorgenstern(0));
        monster.equip(ArmorFactory.buildLeatherPlastron(1));
        return monster;
    }

    public static Monster buildOgreKing() {
        Monster monster = new Monster("ogre_king", 20, 20, 15, 11, 7, 4);
        monster.equip(WeaponFactory.buildMorgenstern(2));
        monster.equip(ArmorFactory.buildChainMail(2));
        return monster;
    }

    public static Monster buildGargoyle() {
        Monster monster = new Monster("gargoyle", 25, 25, 17, 12, 12, 5);
        monster.equip(WeaponFactory.buildBroadSword(2));
        return monster;
    }

    public static Monster buildChaosWarrior() {
        Monster monster = new Monster("chaos_warrior", 17, 17, 12, 9, 10, 4);
        monster.equip(WeaponFactory.buildLongSword(1));
        monster.equip(WeaponFactory.buildLargeShield(1));
        monster.equip(ArmorFactory.buildLamellar(1));
        return monster;
    }

    public static Monster buildChaosWizard() {
        Monster monster = new Monster("chaos_wizard", 5, 5, 6, 8, 2, 7);
        monster.equip(WeaponFactory.buildWizardStaff(2));
        monster.equip(ArmorFactory.buildRobe(2));
        monster.equip(RingFactory.getRandomRing(2));
        return monster;
    }

    public static Monster buildDemon() {
        return new Monster("demon", 5, 5, 6, 10, 2, 7);
    }

    public static Monster buildDemonKing() {
        return new Monster("demon_king", 5, 5, 6, 10, 2, 7);
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
                    case 5:
                    case 10:
                        return buildGoblin();
                    case 6:
                    case 7:
                    case 8:
                        return buildOrc();
                    case 9:
                        return buildOrcCaptain();
                    case 11:
                        return buildTroll();
                    case 12:
                        return buildOgre();
                }
            case 2:
                switch (diceRoll) {
                    case 2:
                    case 3:
                    case 4:
                    case 11:
                        return buildOrc();
                    case 5:
                        return buildOrcCaptain();
                    case 6:
                    case 7:
                        return buildTroll();
                    case 8:
                    case 9:
                        return buildOgre();
                    case 10:
                        return buildOgreKing();
                    case 12:
                        return buildGargoyle();
                }
        }
        return null;
    }

}
