package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

/**
 * Created by guillaume on 19/10/14.
 */
public class StunEffect extends Effect {

    private static final long serialVersionUID = -2548241591886680058L;

    public StunEffect(String spriteName, Characteristics target, int value, int level) {
        super(spriteName, target, value, INFINITE_EFFECT, null, level);
    }

}
