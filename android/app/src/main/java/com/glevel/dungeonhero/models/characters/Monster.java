package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;

import java.util.List;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Monster extends Unit {

    private static final long serialVersionUID = 8983588742551521654L;

    public Monster(int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int currentAttack, int block, int currentBlock, int name, int description, int coins, List<Item> items, List<PassiveSkill> passive, List<ActiveSkill> active) {
        super(Ranks.ENEMY, image, spriteName, hp, currentHP, strength, dexterity, spirit, attack, currentAttack, block, currentBlock, name, description, coins, items, passive, active);
    }

}
