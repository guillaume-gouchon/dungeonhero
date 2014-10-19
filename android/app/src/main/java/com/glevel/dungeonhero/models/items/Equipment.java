package com.glevel.dungeonhero.models.items;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Buff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Equipment extends Item implements Serializable {

    private static final long serialVersionUID = -1500593967008364005L;

    private final List<Buff> buffs = new ArrayList<Buff>();
    private final List<Requirement> requirements = new ArrayList<Requirement>();

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

    public void addBuff(Buff buff) {
        buffs.add(buff);
    }

    public void addRequirement(Requirement requirement) {
        requirements.add(requirement);
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

}
