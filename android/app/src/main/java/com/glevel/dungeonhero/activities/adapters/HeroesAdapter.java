package com.glevel.dungeonhero.activities.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.views.CustomCarousel;

import java.util.List;

public class HeroesAdapter extends CustomCarousel.Adapter<Hero> {

    public HeroesAdapter(Context context, int layoutResource, List<Hero> dataList, View.OnClickListener itemClickedListener) {
        super(context, layoutResource, dataList, itemClickedListener);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = (View) super.instantiateItem(container, position);
        layout.setTag(R.string.id, position);

        Hero hero = mDataList.get(position);

        ((TextView) layout.findViewById(R.id.name)).setText(hero.getName());
        ((ImageView) layout.findViewById(R.id.image)).setImageResource(hero.getImage());
        ((TextView) layout.findViewById(R.id.description)).setText(hero.getDescription());
        ((TextView) layout.findViewById(R.id.strength)).setText("" + hero.getStrength());
        ((TextView) layout.findViewById(R.id.dexterity)).setText("" + hero.getDexterity());
        ((TextView) layout.findViewById(R.id.spirit)).setText("" + hero.getSpirit());
        ((TextView) layout.findViewById(R.id.hp)).setText("" + hero.getHp());

        return layout;
    }

}
