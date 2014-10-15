package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.items.Equipment;
import com.glevel.dungeonhero.models.items.equipments.Weapon;

/**
 * Created by guillaume ON 10/6/14.
 */
public class WeaponFactory {

    public static Equipment buildSword() {
        return new Weapon(R.string.troll, R.drawable.ic_chest);
    }

}
