package com.glevel.dungeonhero.models.items.equipments;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Ring extends Equipment {

    public Ring(String identifier, int level, int price) {
        super(identifier, level, (int) (price * (1 + (level * 0.25))));
    }

}