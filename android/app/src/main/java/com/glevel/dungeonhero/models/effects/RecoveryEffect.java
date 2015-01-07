package com.glevel.dungeonhero.models.effects;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class RecoveryEffect extends Effect implements Serializable {

    private static final long serialVersionUID = 3434030449727561337L;

    public RecoveryEffect(String spriteName) {
        super(spriteName, null, 0, INSTANT_EFFECT, null, 0);
    }

}
