package com.glevel.dungeonhero.models.items;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Item {

    private final int name;
    private final int image;

    public Item(int name, int image) {
        this.name = name;
        this.image = image;
    }

    public int getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
    
}
