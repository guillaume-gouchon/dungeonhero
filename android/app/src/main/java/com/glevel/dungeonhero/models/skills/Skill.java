package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.R;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public abstract class Skill implements Serializable {

    private static final long serialVersionUID = 457816281507623195L;
    private static final int SKILL_MAX_LEVEL = 6;

    private final int name;
    private final int description;
    private final int image;
    private int level;

    public Skill(int name, int description, int image, int level) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.level = level;
    }

    public int getName() {
        return name;
    }

    public int getDescription() {
        return description;
    }

    public int getImage() {
        return image;
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
        switch (level) {
            case 0:
                return R.color.bg_level_0;
            case 1:
                return R.color.bg_level_1;
            case 2:
                return R.color.bg_level_2;
            case 3:
                return R.color.bg_level_3;
            case 4:
                return R.color.bg_level_4;
            case 5:
                return R.color.bg_level_5;
            default:
                return R.color.bg_level_6;
        }
    }

}
