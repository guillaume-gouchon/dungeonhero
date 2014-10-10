package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;

import java.util.List;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Pnj extends Hero {

    private static final long serialVersionUID = 4682214659658928572L;

    public Pnj(Ranks ranks, int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int currentAttack, int block, int currentBlock, int name, int description, int coins, List<Item> items, List<PassiveSkill> passive, List<ActiveSkill> active, String productId, int xp, int level) {
        super(ranks, image, spriteName, hp, currentHP, strength, dexterity, spirit, attack, currentAttack, block, currentBlock, name, description, coins, items, passive, active, productId, xp, level);
    }

}
