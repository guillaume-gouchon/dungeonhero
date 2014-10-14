package com.glevel.dungeonhero.models.characters;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Monster extends Unit {

    private static final long serialVersionUID = 8983588742551521654L;

    public Monster(int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int block, int name, int description, int coins) {
        super(Ranks.ENEMY, image, spriteName, hp, currentHP, strength, dexterity, spirit, attack, block, name, description, coins);
    }

}
