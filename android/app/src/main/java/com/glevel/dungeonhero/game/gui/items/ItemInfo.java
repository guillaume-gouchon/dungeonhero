package com.glevel.dungeonhero.game.gui.items;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.gui.ElementDetails;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.equipments.Armor;
import com.glevel.dungeonhero.models.items.equipments.Equipment;
import com.glevel.dungeonhero.models.items.equipments.weapons.TwoHandedWeapon;
import com.glevel.dungeonhero.models.items.equipments.weapons.Weapon;
import com.glevel.dungeonhero.models.items.requirements.Requirement;
import com.glevel.dungeonhero.models.items.requirements.StatRequirement;
import com.glevel.dungeonhero.views.HintTextView;

/**
 * Created by guillaume on 1/14/15.
 */
public class ItemInfo extends ElementDetails {

    public ItemInfo(Context context, Item item, Hero hero) {
        super(context, item);

        if (item instanceof Equipment) {
            mNameTV.setText(((Equipment) item).getNameWithLevel(context));
        }

        ViewGroup statsLayout = (ViewGroup) findViewById(R.id.stats);
        ViewGroup requirementsLayout = (ViewGroup) findViewById(R.id.requirements);
        int indexStats = 0, indexRequirements = 0;

        if (item instanceof Equipment) {
            final Equipment equipment = (Equipment) item;

            // item name is red if non-suitable
            if (!hero.isEquipmentSuitable(equipment)) {
                mNameTV.setTextColor(mResources.getColor(R.color.red));
            }

            // add item stats
            if (item instanceof Weapon) {
                // weapon stats
                Weapon weapon = (Weapon) item;
                addStatToItemLayout(statsLayout.getChildAt(indexStats++), weapon.getMinDamage() + " - " + (weapon.getMinDamage() + weapon.getDeltaDamage()), R.drawable.ic_damage, R.string.damage, R.color.white);

                // add two handed if needed
                if (item instanceof TwoHandedWeapon) {
                    addStatToItemLayout(requirementsLayout.getChildAt(indexRequirements++), context.getString(R.string.two_handed), 0, R.string.two_handed_description, R.color.white);
                }
            } else if (item instanceof Armor) {
                // armor stats
                Armor armor = (Armor) item;
                addStatToItemLayout(statsLayout.getChildAt(indexStats++), "+" + armor.getProtection(), R.drawable.ic_armor, R.string.protection, R.color.green);
            }

            // add item effects
            for (Effect buff : equipment.getEffects()) {
                if (buff instanceof PermanentEffect && buff.getValue() != 0) {
                    addStatToItemLayout(statsLayout.getChildAt(indexStats++), (buff.getValue() > 0 ? "+" : "") + buff.getValue(), buff.getTarget().getImage(), buff.getTarget().getName(), buff.getValue() > 0 ? R.color.green : R.color.red);
                } else if (buff instanceof StunEffect) {
                    addStatToItemLayout(statsLayout.getChildAt(indexStats++), (buff.getValue() > 0 ? "+" : "") + buff.getValue(), R.drawable.ic_stun, R.string.chance_stun, buff.getValue() >= 0 ? R.color.green : R.color.red);
                }
            }

            // add requirements
            for (Requirement requirement : equipment.getRequirements()) {
                if (requirement instanceof StatRequirement) {
                    StatRequirement statRequirement = (StatRequirement) requirement;
                    addStatToItemLayout(requirementsLayout.getChildAt(indexRequirements++), context.getString(R.string.minimum, statRequirement.getValue()), statRequirement.getTarget().getImage(), statRequirement.getTarget().getName(), statRequirement.getValue() > hero.getCharacteristic(statRequirement.getTarget()) ? R.color.red : R.color.white);
                }
            }
        }
    }

    private void addStatToItemLayout(View view, String text, int image, int hint, int color) {
        HintTextView statView = (HintTextView) view;
        statView.setText(text);
        statView.setTextHint(hint);
        statView.setCompoundDrawablesWithIntrinsicBounds(image, 0, 0, 0);
        int colorResource = mResources.getColor(color);
        statView.setTextColor(colorResource);
        statView.setVisibility(View.VISIBLE);
    }

}
