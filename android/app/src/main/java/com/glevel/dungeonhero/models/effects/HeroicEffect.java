package com.glevel.dungeonhero.models.effects;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class HeroicEffect extends Effect implements Serializable {

    private static final long serialVersionUID = 5666509606513893631L;

    public HeroicEffect(String spriteName) {
        super(spriteName, null, 0, INSTANT_EFFECT, null);
    }

}