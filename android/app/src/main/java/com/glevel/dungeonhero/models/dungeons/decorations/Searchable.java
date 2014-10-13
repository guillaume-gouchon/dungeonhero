package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.models.Reward;

import java.io.Serializable;

/**
 * Created by guillaume on 10/10/14.
 */
public abstract class Searchable extends Decoration implements Serializable {

    private static final long serialVersionUID = -705814371353878655L;

    private boolean isEmpty = false;
    private Reward reward;

    public Searchable(int name, String spriteName, Reward reward) {
        super(name, spriteName);
        this.reward = reward;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Reward search() {
        if (isEmpty) return null;

        isEmpty = true;

        if (reward != null) {
            return reward;
        } else {
            // TODO random stuff
            return null;
        }
    }

}
