package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class StunEffect extends Effect implements Serializable {

    private static final long serialVersionUID = 5666509606513893631L;

    public StunEffect(String spriteName, Characteristics target, int value, int level) {
        super(spriteName, target, value, INFINITE_EFFECT, null, level);
    }

}
