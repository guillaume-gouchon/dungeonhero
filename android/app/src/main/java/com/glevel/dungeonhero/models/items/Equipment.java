package com.glevel.dungeonhero.models.items;

import com.glevel.dungeonhero.R;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Equipment extends Item implements Serializable {

    private static final long serialVersionUID = -1500593967008364005L;

    public Equipment(int name, int image, int color) {
        super(name, image, color, true);
    }

    public static int getEquipmentEmptyImage(int index) {
        switch (index) {
            case 0:
                return R.drawable.ic_weapon;
            case 1:
                return R.drawable.ic_weapon;
            case 2:
                return R.drawable.ic_armor;
            case 3:
                return R.drawable.ic_ring;
            case 4:
                return R.drawable.ic_ring;
            default:
                return 0;
        }
    }

}
