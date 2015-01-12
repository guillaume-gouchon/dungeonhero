package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

/**
 * Created by guillaume on 19/10/14.
 */
public class PermanentEffect extends Effect {

    private static final long serialVersionUID = -8169401976568099446L;

    public PermanentEffect(Characteristics target, int value, PermanentEffect special, int level) {
        super(null, target, value, Effect.INFINITE_EFFECT, special, level);
    }

}
