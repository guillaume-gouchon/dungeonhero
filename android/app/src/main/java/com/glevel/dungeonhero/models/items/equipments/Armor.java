package com.glevel.dungeonhero.models.items.equipments;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.items.Equipment;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Armor extends Equipment implements Serializable {

    private static final long serialVersionUID = 4191323307341633727L;

    private final int protection;

    public Armor(int name, int description, int image, int protection) {
        super(name, description, image, R.color.bg_armor);
        this.protection = protection;
    }

    public int getProtection() {
        return protection;
    }

}
