package com.glevel.dungeonhero.models.items.consumables;

import com.glevel.dungeonhero.models.items.Item;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public abstract class Consumable extends Item implements Serializable {

    private static final long serialVersionUID = 3416498502861499207L;

    public Consumable(String identifier, int price) {
        super(identifier, true, price);
    }

}
