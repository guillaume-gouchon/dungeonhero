package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.models.Reward;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Monster extends Unit {

    public Monster(String identifier, int hp, int currentHP, int strength, int dexterity, int spirit, int movement) {
        super(identifier, Ranks.ENEMY, hp, currentHP, strength, dexterity, spirit, movement);
    }

    public Reward getReward() {
        int gold = (int) (Math.random() * 5) * (5 + 10 * skills.size());
        return new Reward(null, gold, hp * 2 + (equipments[0] != null ? equipments[0].getLevel() + 1 : 0) * 8 + (equipments[2] != null ? equipments[2].getLevel() * 5 : 0) + 15 * skills.size());
    }

}
