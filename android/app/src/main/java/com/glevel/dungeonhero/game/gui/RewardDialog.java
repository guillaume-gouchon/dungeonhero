package com.glevel.dungeonhero.game.gui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.items.equipments.Equipment;

public class RewardDialog extends Dialog {

    public RewardDialog(Context context, Reward reward) {
        super(context, R.style.Dialog);
        setContentView(R.layout.in_game_reward);
        setCancelable(false);

        findViewById(R.id.rootLayout).getBackground().setAlpha(70);

        TextView itemTV = findViewById(R.id.item);
        TextView goldTV = findViewById(R.id.gold);
        TextView xpTV = findViewById(R.id.xp);

        if (reward == null) {
            // found nothing
            itemTV.setText(R.string.found_nothing);
            itemTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.found_nothing);
            goldTV.setVisibility(View.GONE);
            xpTV.setVisibility(View.GONE);
        } else {
            if (reward.getItem() != null) {
                // add item
                String itemName;
                if (reward.getItem() instanceof Equipment) {
                    itemName = ((Equipment) reward.getItem()).getNameWithLevel(context);
                } else {
                    itemName = context.getString(reward.getItem().getName(context.getResources()));
                }
                boolean isAn = itemName.startsWith("A") || itemName.startsWith("E") || itemName.startsWith("I") || itemName.startsWith("O") || itemName.startsWith("U");
                itemTV.setText(context.getString(isAn ? R.string.found_item_an : R.string.found_item_a, itemName));
                itemTV.setCompoundDrawablesWithIntrinsicBounds(0, reward.getItem().getImage(context.getResources()), 0, 0);
                itemTV.setVisibility(View.VISIBLE);
            } else {
                itemTV.setVisibility(View.GONE);
            }

            if (reward.getGold() > 0) {
                // add gold
                goldTV.setText(context.getString(R.string.found_gold, reward.getGold()));
                goldTV.setVisibility(View.VISIBLE);
            } else {
                goldTV.setVisibility(View.GONE);
            }

            if (reward.getXp() > 0) {
                // add xp
                xpTV.setText(context.getString(R.string.reward_xp, reward.getXp()));
                xpTV.setVisibility(View.VISIBLE);
            } else {
                xpTV.setVisibility(View.GONE);
            }
        }

        findViewById(R.id.dismiss_button).setOnClickListener(view -> dismiss());
    }

}
