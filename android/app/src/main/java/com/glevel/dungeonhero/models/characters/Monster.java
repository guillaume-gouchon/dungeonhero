package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.models.Reward;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Monster extends Unit {

    private static final long serialVersionUID = 8983588742551521654L;

    private Reward reward;

    public Monster(int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int movement, int name, int description) {
        super(Ranks.ENEMY, image, spriteName, hp, currentHP, strength, dexterity, spirit, movement, name, description);
        createRandomReward();
    }

    private void createRandomReward() {
        // TODO
        reward = new Reward(null, 30, 10);
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

}
