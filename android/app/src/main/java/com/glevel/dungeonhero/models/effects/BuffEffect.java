package com.glevel.dungeonhero.models.effects;

import com.glevel.dungeonhero.models.items.Characteristics;

import org.andengine.util.color.Color;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class BuffEffect extends Effect implements Serializable {

    private static final long serialVersionUID = 7151836860014013471L;

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
