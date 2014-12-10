package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class DamageEffect extends Effect implements Serializable {

    private static final long serialVersionUID = -44833170357322744L;

    public DamageEffect(String spriteName, int value) {
        super(spriteName, Characteristics.HP, value, Effect.INSTANT_EFFECT, null);
    }

    public DamageEffect(String spriteName, int value, BuffEffect special) {
        super(spriteName, Characteristics.HP, value, Effect.INSTANT_EFFECT, special);
    }

}
