package com.glevel.dungeonhero.factories;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.heroes.Hero;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume on 10/6/14.
 */
public class HeroFactory {

    public static List<Hero> getHeroes() {
        List<Hero> lstHeroes = new ArrayList<Hero>();
        lstHeroes.add(buildWarrior());
        lstHeroes.add(buildWarrior());
        lstHeroes.add(buildWarrior());
        lstHeroes.add(buildWarrior());
        return lstHeroes;
    }


    public static Hero buildWarrior() {
        return new Hero(R.drawable.ic_army_germany, "warrior", 12, 12, 12, 10, 7, 10, 10, 8, 8, R.string.app_name, R.string.choose_hero_message, 10, null, null, null, null, 0, 1);
    }

}
