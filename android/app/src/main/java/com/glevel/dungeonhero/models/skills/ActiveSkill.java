package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.models.effects.Effect;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class ActiveSkill extends Skill implements Serializable {

    private static final long serialVersionUID = 6075354202501161474L;
    private boolean isUsed = false;

    private final boolean personal;
    private final int radius;
    private final Effect effect;

    public ActiveSkill(int name, int description, int image, int level, boolean personal, int radius, Effect effect) {
        super(name, description, image, level);
        this.personal = personal;
        this.radius = radius;
        this.effect = effect;
    }

    public void use() {
        isUsed = true;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void reset() {
        isUsed = false;
    }

    public Effect getEffect() {
        return effect;
    }

    public boolean isPersonal() {
        return personal;
    }

    public int getRadius() {
        return radius;
    }

}
