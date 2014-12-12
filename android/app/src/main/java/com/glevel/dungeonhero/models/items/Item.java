package com.glevel.dungeonhero.models.items;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.StorableResource;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Item extends StorableResource implements Serializable {

    private static final long serialVersionUID = -4963410849686406982L;

    private final boolean droppable;

    public Item(String identifier, boolean droppable) {
        super(identifier);
        this.droppable = droppable;
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

}
