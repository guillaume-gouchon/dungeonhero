package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.models.Reward;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Monster extends Unit {

    private static final long serialVersionUID = 8983588742551521654L;

    private Reward reward;

    public Monster(String identifier, int hp, int currentHP, int strength, int dexterity, int spirit, int movement) {
        super(identifier, Ranks.ENEMY, hp, currentHP, strength, dexterity, spirit, movement);
        createRandomReward();
    }

    private void createRandomReward() {
        int gold = (int) (Math.random() * 5) * (5 + 10 * skills.size());
        reward = new Reward(null, gold, strength + dexterity + spirit + 15 * skills.size());
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

}
