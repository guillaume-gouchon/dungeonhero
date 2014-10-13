package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Ranks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
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
        return new Hero(Ranks.ME, R.drawable.barbare, "barbare.png", 12, 12, 12, 10, 7, 10, 10, 8, 8, R.string.warrior, R.string.warrior_description, 10, null, 0, 1);
    }

}
