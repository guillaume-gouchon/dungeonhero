package com.glevel.dungeonhero.models.items.requirements;

import com.glevel.dungeonhero.models.characters.Hero;

/**
 * Created by guillaume on 19/10/14.
 */
public class HeroRequirement extends Requirement {

    private static final long serialVersionUID = -718162325038755948L;

    private final Hero.HeroTypes heroType;

    public HeroRequirement(Hero.HeroTypes heroType) {
        this.heroType = heroType;
    }

    public Hero.HeroTypes getHeroType() {
        return heroType;
    }

}
