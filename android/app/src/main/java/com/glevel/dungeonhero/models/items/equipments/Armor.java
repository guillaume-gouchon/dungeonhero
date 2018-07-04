package com.glevel.dungeonhero.models.items.equipments;

public class Armor extends Equipment {

    private static final long serialVersionUID = -2482472714192488344L;
    private final int protection;

    public Armor(String identifier, int protection, int level, int price) {
        super(identifier, level, (int) (price * (1 + (level * 0.25))));
        this.protection = protection;
    }

    public int getProtection() {
        return (int) Math.max(protection * (1 + 0.2 * level), protection + level);
    }


}
