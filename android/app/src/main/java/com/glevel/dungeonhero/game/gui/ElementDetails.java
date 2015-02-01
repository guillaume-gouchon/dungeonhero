package com.glevel.dungeonhero.game.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Levelable;
import com.glevel.dungeonhero.models.StorableResource;

/**
 * Created by guillaume on 1/14/15.
 */
public class ElementDetails extends Dialog {

    protected final TextView mNameTV;
    protected final Resources mResources;

    public ElementDetails(Context context, StorableResource element) {
        super(context, R.style.DialogNoAnimation);
        setContentView(R.layout.in_game_item_info);
        setCancelable(true);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(params);
        }

        mResources = context.getResources();

        // name
        mNameTV = (TextView) findViewById(R.id.name);
        String name;
        if (element instanceof Levelable) {
            name = context.getString(R.string.thing_name_with_level, context.getString(element.getName(mResources)), ((Levelable) element).getLevel());
        } else {
            name = context.getString(element.getName(mResources));
        }
        mNameTV.setText(name);
        mNameTV.setCompoundDrawablesWithIntrinsicBounds(element.getImage(mResources), 0, 0, 0);

        // description
        TextView descriptionTV = (TextView) findViewById(R.id.description);
        int description = element.getDescription(mResources);
        if (description > 0) {
            descriptionTV.setText(description);
        } else {
            descriptionTV.setVisibility(View.GONE);
        }
    }

}
