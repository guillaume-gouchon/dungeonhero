package com.glevel.dungeonhero.data.characters;

import com.glevel.dungeonhero.data.items.ArmorFactory;
import com.glevel.dungeonhero.data.items.RingFactory;
import com.glevel.dungeonhero.data.items.WeaponFactory;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.PoisonEffect;
import com.glevel.dungeonhero.models.items.equipments.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by guillaume ON 10/6/14.
 */
public class MonsterFactory {

    public static List<Monster> getAll() {
        List<Monster> lst = new ArrayList<>();
        lst.add(buildGoblin());
        lst.add(buildOrc());
        lst.add(buildOrcCaptain());
        lst.add(buildTroll());
        lst.add(buildOgre());
        lst.add(buildOgreKing());
        lst.add(buildGargoyle());
        lst.add(buildChaosWarrior());
        lst.add(buildChaosWizard());
        lst.add(buildDemon());
        lst.add(buildDemonKing());
        return lst;
    }

    public static Monster buildGoblin() {
        Monster monster = new Monster("goblin", 5, 5, 6, 13, 6, 5);
        monster.equip(WeaponFactory.buildDagger(0));
        return monster;
    }

    public static Monster buildOrc() {
        Monster monster = new Monster("orc", 9, 9, 11, 8, 5, 4);
        monster.equip(WeaponFactory.buildAxe(0));
        monster.equip(ArmorFactory.buildLeatherVest(0));
        return monster;
    }

    public static Monster buildOrcCaptain() {
        Monster monster = new Monster("orc_captain", 13, 13, 12, 8, 8, 4);
        monster.equip(WeaponFactory.buildShortSword(0));
        monster.equip(WeaponFactory.buildRoundShield(0));
        monster.equip(ArmorFactory.buildLeatherVest(0));
        return monster;
    }

    public static Monster buildTroll() {
        Monster monster = new Monster("troll", 22, 22, 15, 7, 2, 3);
        monster.equip(new Weapon("claws", 3, 10, 0, 0));
        monster.getSkills().add(SkillFactory.buildFatalBlow());
        monster.getBuffs().add(new PoisonEffect("poison.png", 2, Effect.INFINITE_EFFECT, null, 0));
        return monster;
    }

    public static Monster buildOgre() {
        Monster monster = new Monster("ogre", 24, 24, 14, 8, 5, 3);
        monster.equip(WeaponFactory.buildMorgenstern(0));
        monster.equip(ArmorFactory.buildLeatherPlastron(0));
        return monster;
    }

    public static Monster buildOgreKing() {
        Monster monster = new Monster("ogre_king", 28, 28, 15, 9, 9, 4);
        monster.equip(WeaponFactory.buildBattleAxe(0));
        monster.equip(ArmorFactory.buildChainmail(0));
        return monster;
    }

    public static Monster buildGargoyle() {
        Monster monster = new Monster("gargoyle", 30, 30, 19, 8, 12, 5);
        monster.equip(new Weapon("claws", 3, 10, 1, 0));
        monster.equip(ArmorFactory.buildLeatherPlastron(1));
        monster.getSkills().add(SkillFactory.buildFatalBlow());
        return monster;
    }

    public static Monster buildChaosWarrior() {
        Monster monster = new Monster("chaos_warrior", 26, 26, 17, 13, 10, 4);
        monster.equip(WeaponFactory.buildLongSword(1));
        monster.equip(WeaponFactory.buildLargeShield(1));
        monster.equip(ArmorFactory.buildLamellar(1));
        return monster;
    }

    public static Monster buildChaosWizard() {
        Monster monster = new Monster("chaos_wizard", 19, 19, 9, 11, 17, 4);
        monster.equip(WeaponFactory.buildWizardStaff(2));
        monster.equip(ArmorFactory.buildRobe(2));
        monster.equip(RingFactory.getRandomRing(2));
        monster.getSkills().add(SkillFactory.buildTerror());
        monster.getSkills().add(SkillFactory.buildPoisonousDarts());
        return monster;
    }

    public static Monster buildDemon() {
        Monster monster = new Monster("demon", 28, 28, 20, 15, 15, 5);
        monster.equip(new Weapon("claws", 4, 15, 1, 0));
        monster.equip(ArmorFactory.buildLamellar(3));
        monster.getSkills().add(SkillFactory.buildRage().improve());
        return monster;
    }

    public static Monster buildDemonKing() {
        Monster monster = new Monster("demon_king", 36, 36, 24, 18, 19, 5);
        monster.equip(new Weapon("claws", 4, 15, 1, 0));
        monster.equip(ArmorFactory.buildBreastPlate(3));
        monster.getSkills().add(SkillFactory.buildFireball().improve());
        monster.getSkills().add(SkillFactory.buildRage().improve().improve().improve());
        return monster;
    }

    public static List<Monster> getRoomContent(int threatLevel) {
        List<Monster> l = new ArrayList<>();
        int nbMonsters;
        Random r = new Random();
        int random = r.nextInt(10);
        if (random < 1) {
            nbMonsters = 0;
        } else if (random < 5) {
            nbMonsters = 1;
        } else if (random < 9 || threatLevel == 1) {
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
        Random r = new Random();
        int diceRoll = 2 + r.nextInt(11);
        switch (threatLevel) {
            case 1:
                switch (diceRoll) {
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 10:
                    case 12:
                        return buildGoblin();
                    case 6:
                    case 7:
                    case 8:
                    case 11:
                        return buildOrc();
                    case 9:
                        return buildOrcCaptain();
                }
            case 2:
                switch (diceRoll) {
                    case 2:
                    case 3:
                    case 4:
                    case 11:
                    case 12:
                        return buildOrc();
                    case 5:
                    case 7:
                    case 6:
                        return buildOrcCaptain();
                    case 8:
                    case 9:
                        return buildTroll();
                    case 10:
                        return buildOgre();
                }
            case 3:
                switch (diceRoll) {
                    case 2:
                    case 3:
                        return buildOrc();
                    case 4:
                    case 11:
                        return buildOrcCaptain();
                    case 5:
                    case 7:
                    case 6:
                        return buildTroll();
                    case 8:
                    case 9:
                    case 12:
                        return buildOgre();
                    case 10:
                        return buildOgreKing();
                }
            case 4:
                switch (diceRoll) {
                    case 2:
                    case 3:
                    case 4:
                        return buildOgre();
                    case 11:
                    case 12:
                    case 5:
                        return buildOgreKing();
                    case 7:
                    case 6:
                    case 8:
                        return buildGargoyle();
                    case 9:
                    case 10:
                        return buildChaosWarrior();
                }
            case 5:
                switch (diceRoll) {
                    case 2:
                    case 3:
                        return buildOgre();
                    case 4:
                    case 11:
                    case 5:
                        return buildOgreKing();
                    case 7:
                    case 6:
                        return buildGargoyle();
                    case 9:
                    case 12:
                        return buildChaosWizard();
                    case 10:
                    case 8:
                        return buildChaosWarrior();
                }
            case 6:
                switch (diceRoll) {
                    case 2:
                    case 3:
                    case 4:
                        return buildChaosWarrior();
                    case 11:
                    case 12:
                        return buildGargoyle();
                    case 7:
                    case 6:
                        return buildChaosWizard();
                    case 9:
                    case 8:
                    case 5:
                        return buildDemon();
                    case 10:
                        return buildDemonKing();
                }
            case 7:
                switch (diceRoll) {
                    case 2:
                    case 4:
                        return buildChaosWarrior();
                    case 12:
                        return buildGargoyle();
                    case 7:
                    case 11:
                    case 6:
                        return buildChaosWizard();
                    case 9:
                    case 8:
                    case 10:
                        return buildDemon();
                    case 5:
                    case 3:
                        buildDemonKing();
                }
            default:
                switch (diceRoll) {
                    case 2:
                    case 3:
                    case 4:
                    case 11:
                    case 12:
                        return buildDemon();
                    case 7:
                    case 6:
                    case 5:
                        return buildChaosWizard();
                    case 9:
                    case 8:
                    case 10:
                        return buildDemonKing();
                }
        }
        return null;
    }

}
