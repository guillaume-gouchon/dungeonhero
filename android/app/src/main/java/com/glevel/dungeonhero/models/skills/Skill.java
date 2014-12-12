package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.StorableResource;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public abstract class Skill extends StorableResource implements Serializable {

    private static final long serialVersionUID = 457816281507623195L;
    private static final int SKILL_MAX_LEVEL = 6;

    private int level;

    public Skill(String identifier, int level) {
        super(identifier);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean canBeImproved() {
        return level <= SKILL_MAX_LEVEL;
    }

    public void improve() {
        level++;
    }

    public int getColor() {
        return GameConstants.getLevelColor(level);
    }

}
