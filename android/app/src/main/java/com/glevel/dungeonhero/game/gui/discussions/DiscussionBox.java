package com.glevel.dungeonhero.game.gui.discussions;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Pnj;

/**
 * Created by guillaume on 1/14/15.
 */
public abstract class DiscussionBox extends Dialog {

    protected final Resources mResources;

    public DiscussionBox(Context context, Pnj pnj) {
        super(context, R.style.Dialog);
        setContentView(R.layout.in_game_discussion);
        setCancelable(false);
        findViewById(R.id.rootLayout).getBackground().setAlpha(70);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(params);
        }

        mResources = context.getResources();

        // character header
        TextView pnjName = (TextView) findViewById(R.id.name);
        pnjName.setText(pnj.getName(mResources));
        pnjName.setCompoundDrawablesWithIntrinsicBounds(pnj.getImage(mResources), 0, 0, 0);
    }

}
