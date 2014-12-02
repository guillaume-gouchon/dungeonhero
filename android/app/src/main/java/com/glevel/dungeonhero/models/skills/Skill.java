package com.glevel.dungeonhero.models.skills;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Skill implements Serializable {

    private static final long serialVersionUID = 457816281507623195L;

    private int name;
    private int description;
    private int image;

    public Skill(int name, int description, int image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }
    
}
