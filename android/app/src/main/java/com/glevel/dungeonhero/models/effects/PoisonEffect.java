package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

/**
 * Created by guillaume on 19/10/14.
 */
public class PoisonEffect extends Effect {

    private static final long serialVersionUID = 6907242603812137514L;

    public PoisonEffect(String spriteName, int value, int duration, Effect special, int level) {
        super(spriteName, Characteristics.HP, value, duration, special, level);
    }

}
