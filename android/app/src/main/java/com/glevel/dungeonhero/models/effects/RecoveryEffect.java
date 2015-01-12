package com.glevel.dungeonhero.models.effects;

/**
 * Created by guillaume on 19/10/14.
 */
public class RecoveryEffect extends Effect {

    private static final long serialVersionUID = 2109747444465353571L;

    public RecoveryEffect(String spriteName) {
        super(spriteName, null, 0, INSTANT_EFFECT, null, 0);
    }

}
