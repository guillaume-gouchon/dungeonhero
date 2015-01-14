package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Levelable;
import com.glevel.dungeonhero.models.StorableResource;
import com.glevel.dungeonhero.models.effects.Effect;

/**
 * Created by guillaume ON 10/6/14.
 */
public abstract class Skill extends StorableResource implements Levelable {

    private static final long serialVersionUID = -5459063916950986709L;

    private int level;
    private final Effect effect;

    public Skill(String identifier, Effect effect, int level) {
        super(identifier);
        this.level = level;
        this.effect = effect;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public boolean canBeImproved() {
        return level <= GameConstants.SKILL_MAX_LEVEL;
    }

    public Skill improve() {
        level++;
        effect.improve();
        if (effect.getSpecial() != null) {
            effect.getSpecial().improve();
        }
        return this;
    }

    public Effect getEffect() {
        return effect;
    }

    public int getColor() {
        return GameConstants.getLevelColor(level);
    }

}
