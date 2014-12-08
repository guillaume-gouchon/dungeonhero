package com.glevel.dungeonhero.models.skills;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public abstract class Skill implements Serializable {

    private static final long serialVersionUID = 457816281507623195L;

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

    public void improve() {
        level++;
    }

}
