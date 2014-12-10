package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.Equipment;
import com.glevel.dungeonhero.models.items.Requirement;
import com.glevel.dungeonhero.models.items.equipments.Weapon;

/**
 * Created by guillaume ON 10/6/14.
 */
public class WeaponFactory {

    public static Equipment buildSword() {
        Weapon item = new Weapon(R.string.short_sword, R.drawable.ic_weapon, 5, 3);
        item.addEffect(new PermanentEffect(Characteristics.DAMAGE, 5, null));
        item.addRequirement(new Requirement(Characteristics.STRENGTH, 8));
        return item;
    }

}
