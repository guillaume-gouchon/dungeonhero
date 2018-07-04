package com.glevel.dungeonhero.game.gui.discussions;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Pnj;

abstract class DiscussionBox extends Dialog {

    final Resources mResources;

    DiscussionBox(Context context, Pnj pnj) {
        super(context, R.style.Dialog);
        setContentView(R.layout.in_game_discussion);
        setCancelable(false);
        findViewById(R.id.rootLayout).getBackground().setAlpha(70);

        mResources = context.getResources();

        // character header
        TextView pnjName = findViewById(R.id.name);
        pnjName.setText(pnj.getName(mResources));
        pnjName.setCompoundDrawablesWithIntrinsicBounds(pnj.getImage(mResources), 0, 0, 0);
    }

}
