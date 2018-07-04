package com.glevel.dungeonhero.models.items;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.StorableResource;
import com.glevel.dungeonhero.models.items.equipments.Equipment;

public abstract class Item extends StorableResource {

    private static final long serialVersionUID = -7794186148059317585L;

    private final boolean droppable;
    private final int price;

    protected Item(String identifier, boolean droppable, int price) {
        super(identifier);
        this.droppable = droppable;
        this.price = price;
    }

    public int getColor() {
        if (this instanceof Equipment) {
            return GameConstants.getLevelColor(((Equipment) this).getLevel());
        } else {
            return GameConstants.getLevelColor(0);
        }
    }

    public boolean isDroppable() {
        return droppable;
    }

    public int getPrice() {
        return price;
    }

    public int getSellPrice() {
        return price / 4;
    }

}
