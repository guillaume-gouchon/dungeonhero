package com.glevel.dungeonhero.models.characters.heroes;


import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;
import com.glevel.dungeonhero.utils.billing.InAppProduct;

import java.util.List;

/**
 * Created by guillaume on 10/2/14.
 */
public class Hero extends Unit implements InAppProduct {

    protected boolean mHasBeenBought = false;

    private final String productId;
    private int xp;
    private int level;

    public Hero(int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int currentAttack, int block, int currentBlock, int name, int description, int coins, List<Item> items, List<PassiveSkill> passive, List<ActiveSkill> active, String productId, int xp, int level) {
        super(image, spriteName, hp, currentHP, strength, dexterity, spirit, attack, currentAttack, block, currentBlock, name, description, coins, items, passive, active);
        this.productId = productId;
        this.xp = xp;
        this.level = level;
    }

    @Override
    public String getProductId() {
        return productId;
    }

    @Override
    public void setHasBeenBought(boolean hasBeenBought) {
        mHasBeenBought = hasBeenBought;
    }

    @Override
    public boolean isAvailable() {
        return isFree() || mHasBeenBought;
    }

    @Override
    public boolean isFree() {
        return productId == null;
    }

}
