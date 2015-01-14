package com.glevel.dungeonhero.game.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Levelable;
import com.glevel.dungeonhero.models.StorableResource;

/**
 * Created by guillaume on 1/14/15.
 */
public abstract class SomethingDetails extends Dialog {

    protected final TextView mNameTV;
    protected final Resources mResources;

    public SomethingDetails(Context context, StorableResource thing) {
        super(context, R.style.DialogNoAnimation);
        setContentView(R.layout.in_game_item_info);
        setCancelable(true);

        mResources = context.getResources();

        // name
        mNameTV = (TextView) findViewById(R.id.name);
        String name;
        if (thing instanceof Levelable) {
            name = context.getString(R.string.thing_name_with_level, context.getString(thing.getName(mResources)), ((Levelable) thing).getLevel());
        } else {
            name = context.getString(thing.getName(mResources));
        }
        mNameTV.setText(name);
        mNameTV.setCompoundDrawablesWithIntrinsicBounds(thing.getImage(mResources), 0, 0, 0);

        // description
        TextView descriptionTV = (TextView) findViewById(R.id.description);
        int description = thing.getDescription(mResources);
        if (description > 0) {
            descriptionTV.setText(description);
        } else {
            descriptionTV.setVisibility(View.GONE);
        }
    }

}
