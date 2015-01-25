package com.glevel.dungeonhero.activities.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.base.GraphicsManager;
import com.glevel.dungeonhero.game.gui.ElementDetails;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.skills.Skill;
import com.glevel.dungeonhero.views.CustomCarousel;
import com.glevel.dungeonhero.views.SpriteView;

import java.util.List;

public class HeroesAdapter extends CustomCarousel.Adapter<Hero> {

    private static final String TAG = "HeroesAdapter";

    private Activity mActivity;
    private Resources mResources;
    private Dialog mSkillInfoDialog;

    public HeroesAdapter(Activity activity, int layoutResource, List<Hero> dataList, View.OnClickListener itemClickedListener) {
        super(activity, layoutResource, dataList, itemClickedListener);
        mActivity = activity;
        mResources = activity.getResources();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiate item " + position);

        View layout = (View) super.instantiateItem(container, position);
        layout.setTag(R.string.id, position);

        Hero hero = mDataList.get(position);

        ((TextView) layout.findViewById(R.id.name)).setText(hero.getName(mResources));
        ((SpriteView) layout.findViewById(R.id.image)).setImageResource(hero.getImage(mResources));
        ((SpriteView) layout.findViewById(R.id.image)).setSpriteName(GraphicsManager.ASSETS_PATH + hero.getIdentifier() + ".png");
        ((TextView) layout.findViewById(R.id.description)).setText(hero.getDescription(mResources));
        ((TextView) layout.findViewById(R.id.strength)).setText("" + hero.getStrength());
        ((TextView) layout.findViewById(R.id.dexterity)).setText("" + hero.getDexterity());
        ((TextView) layout.findViewById(R.id.spirit)).setText("" + hero.getSpirit());
        ((TextView) layout.findViewById(R.id.hp)).setText("" + hero.getHp());
        ((TextView) layout.findViewById(R.id.movement)).setText("" + hero.calculateMovement());
        if (!hero.isAvailable()) {
            layout.findViewById(R.id.lock).setVisibility(View.VISIBLE);
        }
        showSkills(layout, hero);

        return layout;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private void showSkills(View rootLayout, Hero hero) {
        ViewGroup skillLayout = (ViewGroup) rootLayout.findViewById(R.id.skills);
        for (int n = 0; n < skillLayout.getChildCount(); n++) {
            if (n < hero.getSkills().size()) {
                updateSkillLayout(skillLayout.getChildAt(n), hero.getSkills().get(n));
            } else {
                skillLayout.getChildAt(n).setVisibility(View.GONE);
            }
        }
    }

    private void updateSkillLayout(View itemView, Skill skill) {
        ImageView image = (ImageView) itemView.findViewById(R.id.image);

        itemView.setTag(R.string.skill, skill);
        itemView.setAlpha(0.6f);

        image.setImageResource(skill.getImage(mResources));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSkillInfo((Skill) v.getTag(R.string.skill));
            }
        });
    }

    private void showSkillInfo(Skill skill) {
        if (mSkillInfoDialog == null || !mSkillInfoDialog.isShowing()) {
            mSkillInfoDialog = new ElementDetails(mActivity, skill);
            mSkillInfoDialog.show();
        }
    }

}
