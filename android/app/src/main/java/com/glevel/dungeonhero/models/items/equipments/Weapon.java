package com.glevel.dungeonhero.models.items.equipments;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.items.Equipment;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Weapon extends Equipment implements Serializable {

    private static final long serialVersionUID = 4191323307341633727L;

    public Weapon(int name, int image) {
        super(name, image, R.color.bg_weapon);
    }

}
