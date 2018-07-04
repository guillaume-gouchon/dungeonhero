package com.glevel.dungeonhero.game.gui.items;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.consumables.Potion;
import com.glevel.dungeonhero.models.items.equipments.Equipment;

public class ItemInfoInGame extends ItemInfo {

    public ItemInfoInGame(Context context, Item item, Hero hero, final OnItemActionSelected onItemActionSelected) {
        super(context, item, hero);

        // actions
        TextView mainActionButton = findViewById(R.id.main_action_btn);
        TextView secondaryActionButton = findViewById(R.id.secondary_action_btn);

        if (item instanceof Equipment) {
            final Equipment equipment = (Equipment) item;
            if (hero.isEquipped(equipment)) {
                // unequip stuff
                mainActionButton.setText(R.string.unequip);
                mainActionButton.setOnClickListener(view -> {
                    onItemActionSelected.onActionExecuted(ItemActionsInGame.UNEQUIP);
                    dismiss();
                });
                mainActionButton.setVisibility(View.VISIBLE);
            } else if (hero.canEquipItem(equipment)) {
                // equip stuff
                mainActionButton.setText(R.string.equip);
                mainActionButton.setOnClickListener(view -> {
                    onItemActionSelected.onActionExecuted(ItemActionsInGame.EQUIP);
                    dismiss();
                });
                mainActionButton.setVisibility(View.VISIBLE);
            }
        } else if (item instanceof Potion) {
            // drink potion
            mainActionButton.setText(R.string.drink);
            mainActionButton.setOnClickListener(v -> {
                dismiss();
                onItemActionSelected.onActionExecuted(ItemActionsInGame.DRINK);
            });
            mainActionButton.setVisibility(View.VISIBLE);
        }

        if (!(item instanceof Equipment) || !hero.isEquipped((Equipment) item) && item.isDroppable()) {
            // drop item
            secondaryActionButton.setText(R.string.drop);
            secondaryActionButton.setOnClickListener(view -> {
                onItemActionSelected.onActionExecuted(ItemActionsInGame.DROP);
                dismiss();
            });
            secondaryActionButton.setVisibility(View.VISIBLE);
        }
    }

    public enum ItemActionsInGame {
        EQUIP, UNEQUIP, DROP, DRINK
    }

    public interface OnItemActionSelected {

        void onActionExecuted(ItemActionsInGame action);

    }

}
