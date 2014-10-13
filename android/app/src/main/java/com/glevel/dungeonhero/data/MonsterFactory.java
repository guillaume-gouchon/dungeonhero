package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Monster;

/**
 * Created by guillaume ON 10/6/14.
 */
public class MonsterFactory {

    public static Monster buildGoblin() {
        return new Monster(R.drawable.goblin, "goblin.png", 5, 5, 6, 10, 2, 8, 8, 8, 8, R.string.app_name, R.string.choose_hero_message, 10);
    }

}
