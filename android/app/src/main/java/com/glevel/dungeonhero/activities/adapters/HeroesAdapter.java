package com.glevel.dungeonhero.activities.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.heroes.Hero;
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

        TextView heroName = (TextView) layout.findViewById(R.id.heroName);
        heroName.setText(hero.getClass().getName());

        // TODO

        return layout;
    }


}
