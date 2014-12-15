package com.glevel.dungeonhero.models.items.equipments;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.requirements.Requirement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Equipment extends Item implements Serializable {

    private static final long serialVersionUID = -1500593967008364005L;

    private final List<Effect> effects = new ArrayList<Effect>();
    private final List<Requirement> requirements = new ArrayList<Requirement>();
    private final int level;

    public Equipment(String identifier, int level) {
        super(identifier, true);
        this.level = level;
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

    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void addRequirement(Requirement requirement) {
        requirements.add(requirement);
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public int getLevel() {
        return level;
    }

}
