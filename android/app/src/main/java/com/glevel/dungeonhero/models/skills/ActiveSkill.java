package com.glevel.dungeonhero.models.skills;

import com.glevel.dungeonhero.models.Buff;
import com.glevel.dungeonhero.models.dungeons.Tile;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class ActiveSkill extends Skill implements Serializable {

    private static final long serialVersionUID = 6075354202501161474L;
    private boolean isUsed = false;

    private Buff buff;
    private int damage;

    public ActiveSkill(int name, int description, int image) {
        super(name, description, image);
    }

    public void use(Tile tile) {
        isUsed = true;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void reset() {
        isUsed = false;
    }

    public Buff getBuff() {
        return buff;
    }

    public void setBuff(Buff buff) {
        this.buff = buff;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}
