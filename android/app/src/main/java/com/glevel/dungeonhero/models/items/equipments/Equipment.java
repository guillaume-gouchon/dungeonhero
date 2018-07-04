package com.glevel.dungeonhero.models.items.equipments;

import android.content.Context;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Levelable;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.requirements.Requirement;

import java.util.ArrayList;
import java.util.List;

public abstract class Equipment extends Item implements Levelable {

    private static final long serialVersionUID = 662104930156421984L;

    private final List<Effect> effects = new ArrayList<>();
    private final List<Requirement> requirements = new ArrayList<>();
    protected final int level;

    protected Equipment(String identifier, int level, int price) {
        super(identifier, true, price);
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

    @Override
    public int getLevel() {
        return level;
    }

    public String getNameWithLevel(Context context) {
        return context.getString(getName(context.getResources())) + (level > 0 ? "\u00A0+" + level : "");
    }

}
