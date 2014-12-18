package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class PoisonEffect extends Effect implements Serializable {

    private static final long serialVersionUID = 8376399829147352445L;

    public PoisonEffect(String spriteName, int value, int duration, Effect special, int level) {
        super(spriteName, Characteristics.HP, value, duration, special, level);
    }

}
