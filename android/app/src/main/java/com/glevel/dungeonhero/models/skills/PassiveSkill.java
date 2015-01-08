package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.models.effects.Effect;

/**
 * Created by guillaume ON 10/6/14.
 */
public class PassiveSkill extends Skill {

    public PassiveSkill(String identifier, int level, Effect effect) {
        super(identifier, effect, level);
    }

}
