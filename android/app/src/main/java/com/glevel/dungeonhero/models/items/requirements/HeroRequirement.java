package com.glevel.dungeonhero.models.items.requirements;

import com.glevel.dungeonhero.models.characters.Hero;

import java.io.Serializable;

/**
 * Created by guillaume on 19/10/14.
 */
public class HeroRequirement extends Requirement implements Serializable {

    private static final long serialVersionUID = -5601809542956982066L;

    private final Hero.HeroTypes heroType;

    public HeroRequirement(Hero.HeroTypes heroType) {
        this.heroType = heroType;
    }

    public Hero.HeroTypes getHeroType() {
        return heroType;
    }

}
