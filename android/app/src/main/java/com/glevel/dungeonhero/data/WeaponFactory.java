package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Buff;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.Equipment;
import com.glevel.dungeonhero.models.items.Requirement;
import com.glevel.dungeonhero.models.items.equipments.Weapon;

/**
 * Created by guillaume ON 10/6/14.
 */
public class WeaponFactory {

    public static Equipment buildSword() {
        Weapon item = new Weapon(R.string.troll, R.drawable.ic_chest, 5, 3);
        item.addBuff(new Buff(5, Characteristics.ATTACK, null));
        item.addRequirement(new Requirement(8, Characteristics.STRENGTH));
        return item;
    }

}
