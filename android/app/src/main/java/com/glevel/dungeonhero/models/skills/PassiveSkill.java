package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.models.effects.Effect;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class PassiveSkill extends Skill implements Serializable {

    private static final long serialVersionUID = -2557103327292997487L;

    private final Effect effect;

    public PassiveSkill(int name, int description, int image, int level, Effect effect) {
        super(name, description, image, level);
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }

}
