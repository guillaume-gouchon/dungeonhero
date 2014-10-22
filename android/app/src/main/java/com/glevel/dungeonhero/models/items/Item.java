package com.glevel.dungeonhero.models.items;

import com.glevel.dungeonhero.R;

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

    protected boolean isIdentified;

    public Item(int name, int description, int image, int color, boolean droppable) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.color = color;
        this.droppable = droppable;
    }

    public int getName() {
        if (!isIdentified) {
            return R.string.unknown_item;
        }
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
        if (description > 0 && !isIdentified) {
            return R.string.unknown_item;
        }
        return description;
    }

}
