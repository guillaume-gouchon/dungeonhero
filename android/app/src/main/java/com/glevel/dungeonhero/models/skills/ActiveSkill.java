package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.models.effects.Effect;

/**
 * Created by guillaume ON 10/6/14.
 */
public class ActiveSkill extends Skill {

    private static final long serialVersionUID = -3936496140028402150L;

    private boolean isUsed = false;
    private final boolean personal;
    private final int radius;

    public ActiveSkill(String identifier, int level, boolean personal, int radius, Effect effect) {
        super(identifier, effect, level);
        this.personal = personal;
        this.radius = radius;
    }

    public void use() {
        isUsed = true;
    }

    public void reset() {
        isUsed = false;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public boolean isPersonal() {
        return personal;
    }

    public int getRadius() {
        return radius;
    }

}
