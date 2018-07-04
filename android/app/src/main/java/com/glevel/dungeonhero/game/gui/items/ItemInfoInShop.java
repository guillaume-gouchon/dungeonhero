package com.glevel.dungeonhero.game.gui.items;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.items.Item;

public class ItemInfoInShop extends ItemInfo {

    public ItemInfoInShop(Context context, Item item, Hero hero, boolean isSelling, final OnItemActionSelected onItemActionSelected) {
        super(context, item, hero);

        // actions
        TextView mainActionButton = findViewById(R.id.main_action_btn);
        mainActionButton.setText(isSelling ? context.getString(R.string.sell_item_for, item.getSellPrice()) : context.getString(R.string.buy_item_for, item.getPrice()));
        mainActionButton.setEnabled(isSelling || hero.getGold() >= item.getPrice() && hero.getItems().size() < GameConstants.NB_ITEMS_MAX_IN_BAG);
        mainActionButton.setOnClickListener(v -> onItemActionSelected.onActionExecuted(ItemActionsInShop.DO_TRANSACTION));
        mainActionButton.setVisibility(View.VISIBLE);
    }

    public enum ItemActionsInShop {
        DO_TRANSACTION
    }

    public interface OnItemActionSelected {

        void onActionExecuted(ItemActionsInShop action);

    }

}
