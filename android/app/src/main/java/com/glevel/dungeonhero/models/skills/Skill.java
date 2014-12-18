package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.StorableResource;
import com.glevel.dungeonhero.models.effects.Effect;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public abstract class Skill extends StorableResource implements Serializable {

    private static final long serialVersionUID = 457816281507623195L;
    private static final int SKILL_MAX_LEVEL = 6;

    private int level;
    private final Effect effect;

    public Skill(String identifier, Effect effect, int level) {
        super(identifier);
        this.level = level;
        this.effect = effect;
    }

    public int getLevel() {
        return level;
    }

    public boolean canBeImproved() {
        return level <= SKILL_MAX_LEVEL;
    }

    public void improve() {
        level++;
        effect.improve();
        if (effect.getSpecial() != null) {
            effect.getSpecial().improve();
        }
    }

    public Effect getEffect() {
        return effect;
    }

    public int getColor() {
        return GameConstants.getLevelColor(level);
    }

}
