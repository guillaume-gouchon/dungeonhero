package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.models.items.Item;

import java.io.Serializable;

/**
 * Created by guillaume on 10/10/14.
 */
public abstract class Searchable extends Decoration implements Serializable {

    private static final long serialVersionUID = -705814371353878655L;

    private boolean isEmpty = false;

    public Searchable(int name, String spriteName) {
        super(name, spriteName);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Item search() {
        if (isEmpty) return null;

        isEmpty = true;
        return null;
    }

}
