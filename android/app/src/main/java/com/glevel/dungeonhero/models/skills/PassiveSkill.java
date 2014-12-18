package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.models.effects.Effect;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class PassiveSkill extends Skill implements Serializable {

    private static final long serialVersionUID = -2557103327292997487L;

    public PassiveSkill(String identifier, int level, Effect effect) {
        super(identifier, effect, level);
    }

}
