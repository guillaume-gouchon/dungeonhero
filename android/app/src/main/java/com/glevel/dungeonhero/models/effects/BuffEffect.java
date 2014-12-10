package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class BuffEffect extends Effect implements Serializable {

    private static final long serialVersionUID = 7151836860014013471L;

    public BuffEffect(String spriteName, Characteristics target, int value, int duration, BuffEffect special) {
        super(spriteName, target, value, duration, special);
    }

}
