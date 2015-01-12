package com.glevel.dungeonhero.models.effects;

/**
 * Created by guillaume on 19/10/14.
 */
public class HeroicEffect extends Effect {

    private static final long serialVersionUID = 4790750951825298101L;

    public HeroicEffect(String spriteName) {
        super(spriteName, null, 0, INSTANT_EFFECT, null, 0);
    }

}
