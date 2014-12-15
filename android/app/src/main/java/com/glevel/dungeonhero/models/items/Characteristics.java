package com.glevel.dungeonhero.models.items;

import com.glevel.dungeonhero.R;

/**
 * s
 * Created by guillaume on 19/10/14.
 */
public enum Characteristics {
    HP(R.string.hp, R.drawable.ic_health), STRENGTH(R.string.strength, R.drawable.ic_strength),
    DEXTERITY(R.string.dexterity, R.drawable.ic_dexterity), SPIRIT(R.string.spirit, R.drawable.ic_spirit),
    DAMAGE(R.string.damage, R.drawable.ic_damage), BLOCK(R.string.block, R.drawable.ic_block),
    DODGE(R.string.dodge, R.drawable.ic_dodge), MOVEMENT(R.string.movement, R.drawable.ic_move),
    INITIATIVE(R.string.initiative, R.drawable.ic_initiative), CRITICAL(R.string.critical, R.drawable.ic_critics),
    PROTECTION(R.string.protection, R.drawable.ic_armor);

    private final int name;
    private final int image;

    private Characteristics(int name, int image) {
        this.name = name;
        this.image = image;
    }

    public int getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

}
