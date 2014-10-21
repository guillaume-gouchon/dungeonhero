package com.glevel.dungeonhero.models.items;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Item implements Serializable {

    private static final long serialVersionUID = -4963410849686406982L;

    private final int name;
    private final int description;
    private final int image;
    private final int color;
    private final boolean droppable;

    private boolean isIdentified;

    public Item(int name, int description, int image, int color, boolean droppable) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.color = color;
        this.droppable = droppable;
    }

    public int getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public boolean isIdentified() {
        return isIdentified;
    }

    public void setIdentified(boolean isIdentified) {
        this.isIdentified = isIdentified;
    }

    public int getColor() {
        return color;
    }

    public boolean isDroppable() {
        return droppable;
    }

    public int getDescription() {
        return description;
    }
    
}
