package com.glevel.dungeonhero.models.items.equipments;

public class Ring extends Equipment {

    private static final long serialVersionUID = 1124163815757262905L;

    public Ring(String identifier, int level, int price) {
        super(identifier, level, (int) (price * (1 + (level * 0.25))));
    }

}