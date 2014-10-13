package com.glevel.dungeonhero.models;

import com.glevel.dungeonhero.models.items.Item;

/**
 * Created by guillaume on 10/13/14.
 */
public class Reward {

    private final Item item;
    private final int gold;
    private final int xp;

    public Reward(Item item, int gold, int xp) {
        this.item = item;
        this.gold = gold;
        this.xp = xp;
    }

    public Reward(Item item) {
        this.item = item;
        this.gold = 0;
        this.xp = 0;
    }

    public Item getItem() {
        return item;
    }

    public int getGold() {
        return gold;
    }

    public int getXp() {
        return xp;
    }

}
