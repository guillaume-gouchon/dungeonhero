package com.glevel.dungeonhero.game.data;

import com.glevel.dungeonhero.models.characters.heroes.Barbarian;
import com.glevel.dungeonhero.models.characters.heroes.Hero;
import com.glevel.dungeonhero.models.characters.heroes.Thief;
import com.glevel.dungeonhero.models.characters.heroes.Wizard;

import java.util.Arrays;
import java.util.List;

/**
 * Created by guillaume on 10/3/14.
 */
public class GameData {

    public static List<Hero> getHeroes() {
        return Arrays.asList(new Hero[]{new Barbarian(), new Thief(), new Wizard()});
    }

}
