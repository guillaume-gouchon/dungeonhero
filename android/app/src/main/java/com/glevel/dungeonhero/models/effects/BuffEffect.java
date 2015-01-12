package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

import org.andengine.util.color.Color;

/**
 * Created by guillaume on 19/10/14.
 */
public class BuffEffect extends Effect {

    private static final long serialVersionUID = -2502520164928739832L;

    private final Color buffColor;

    public BuffEffect(String spriteName, Characteristics target, int value, int duration, Effect special, int level) {
        super(spriteName, target, value, duration, special, level);
        buffColor = null;
    }

    public BuffEffect(String spriteName, Characteristics target, int value, int duration, Effect special, int level, Color buffColor) {
        super(spriteName, target, value, duration, special, level);
        this.buffColor = buffColor;
    }

    public Color getBuffColor() {
        return buffColor;
    }

}
