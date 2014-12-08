package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.models.Buff;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class PassiveSkill extends Skill implements Serializable {

    private static final long serialVersionUID = -2557103327292997487L;

    public PassiveSkill(int name, int description, int image, int level, Buff buff) {
        super(name, description, image, level);
    }

}
