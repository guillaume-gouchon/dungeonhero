package com.glevel.dungeonhero.models.effects;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class CamouflageEffect extends Effect implements Serializable {

    private static final long serialVersionUID = 5666509606513893631L;

    public CamouflageEffect(String spriteName, int value, int level) {
        super(spriteName, null, value, INFINITE_EFFECT, null, level);
    }

}
