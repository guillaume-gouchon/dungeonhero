package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

/**
 * Created by guillaume on 19/10/14.
 */
public class DamageEffect extends Effect {

    private static final long serialVersionUID = -8460128387814092995L;

    public DamageEffect(String spriteName, int value, int level) {
        super(spriteName, Characteristics.HP, value, Effect.INSTANT_EFFECT, null, level);
    }

    public DamageEffect(String spriteName, int value, Effect special, int level) {
        super(spriteName, Characteristics.HP, value, Effect.INSTANT_EFFECT, special, level);
    }

}
