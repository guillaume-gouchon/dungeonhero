package com.glevel.dungeonhero.activities.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.skills.Skill;
import com.glevel.dungeonhero.views.CustomCarousel;

import java.util.List;

public class HeroesAdapter extends CustomCarousel.Adapter<Hero> {

    private Activity mActivity;
    private Dialog mSkillInfoDialog;

    public HeroesAdapter(Activity activity, int layoutResource, List<Hero> dataList, View.OnClickListener itemClickedListener) {
        super(activity, layoutResource, dataList, itemClickedListener);
        mActivity = activity;
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
        ((TextView) layout.findViewById(R.id.movement)).setText("" + hero.calculateMovement());
        if (!hero.isAvailable()) {
            layout.findViewById(R.id.lock).setVisibility(View.VISIBLE);
        }
        showSkills(layout, hero);

        return layout;
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

        image.setImageResource(skill.getImage());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSkillInfo((Skill) v.getTag(R.string.skill));
            }
        });
    }

    public void showSkillInfo(final Skill skill) {
        if (mSkillInfoDialog == null || !mSkillInfoDialog.isShowing()) {
            mSkillInfoDialog = new Dialog(mActivity, R.style.DialogNoAnimation);
            mSkillInfoDialog.setContentView(R.layout.in_game_item_info);
            mSkillInfoDialog.setCancelable(true);

            TextView nameTV = (TextView) mSkillInfoDialog.findViewById(R.id.name);
            nameTV.setText(skill.getName());
            nameTV.setCompoundDrawablesWithIntrinsicBounds(skill.getImage(), 0, 0, 0);

            TextView descriptionTV = (TextView) mSkillInfoDialog.findViewById(R.id.description);
            if (skill.getDescription() > 0) {
                descriptionTV.setText(skill.getDescription());
            } else {
                descriptionTV.setVisibility(View.GONE);
            }

            mSkillInfoDialog.findViewById(R.id.actionButton).setVisibility(View.GONE);
            mSkillInfoDialog.findViewById(R.id.dropButton).setVisibility(View.GONE);
            mSkillInfoDialog.show();
        }
    }

}
