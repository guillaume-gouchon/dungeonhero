package com.glevel.dungeonhero.models.items.consumables;

import com.glevel.dungeonhero.models.effects.Effect;

public class Potion extends Consumable {

    private static final long serialVersionUID = 317434819716374400L;
    private final Effect effect;

    public Potion(String identifier, Effect effect, int price) {
        super(identifier, price);
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }

}
