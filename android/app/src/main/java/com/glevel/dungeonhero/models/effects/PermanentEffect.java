package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class PermanentEffect extends Effect implements Serializable {

    private static final long serialVersionUID = -3595619934836089070L;

    public PermanentEffect(Characteristics target, int value, PermanentEffect special) {
        super(null, target, value, Effect.INFINITE_EFFECT, special);
    }

}
