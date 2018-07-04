package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.models.effects.Effect;

public class PassiveSkill extends Skill {

    private static final long serialVersionUID = 6506157824986711801L;

    public PassiveSkill(String identifier, int level, Effect effect) {
        super(identifier, effect, level);
    }

}
