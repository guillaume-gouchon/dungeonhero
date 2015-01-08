package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

/**
 * Created by guillaume on 19/10/14.
 */
public class PoisonEffect extends Effect {

    public PoisonEffect(String spriteName, int value, int duration, Effect special, int level) {
        super(spriteName, Characteristics.HP, value, duration, special, level);
    }

}
