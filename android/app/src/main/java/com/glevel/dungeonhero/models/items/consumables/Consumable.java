package com.glevel.dungeonhero.models.items.consumables;

import com.glevel.dungeonhero.models.items.Item;

public abstract class Consumable extends Item {

    private static final long serialVersionUID = -1499223977177555673L;

    Consumable(String identifier, int price) {
        super(identifier, true, price);
    }

}
