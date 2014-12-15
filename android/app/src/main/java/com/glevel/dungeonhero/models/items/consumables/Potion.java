package com.glevel.dungeonhero.models.items.consumables;

import com.glevel.dungeonhero.models.effects.Effect;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Potion extends Consumable implements Serializable {

    private static final long serialVersionUID = 8959726100020921265L;

    private final Effect effect;

    public Potion(String identifier, Effect effect) {
        super(identifier);
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }

}
