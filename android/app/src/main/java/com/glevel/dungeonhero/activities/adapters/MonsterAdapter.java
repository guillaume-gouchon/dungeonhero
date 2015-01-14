package com.glevel.dungeonhero.activities.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.gui.SomethingDetails;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.skills.Skill;
import com.glevel.dungeonhero.utils.MusicManager;

import java.util.Collections;
import java.util.List;

public class MonsterAdapter extends ArrayAdapter<Monster> {

    private final Activity mActivity;
    private final Hero mHero;
    private final Resources mResources;
    private final Animation mBounceAnimation;
    private Dialog mSkillInfoDialog;

    public MonsterAdapter(Activity activity, int layoutResource, List<Monster> dataList, Hero hero) {
        super(activity, layoutResource, dataList);
        mActivity = activity;
        mResources = activity.getResources();
        mHero = hero;
        mBounceAnimation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.bounce_monster);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.monster_item, null);
        bindMonsterToView(getItem(position), layout);
        return layout;
    }

    private void bindMonsterToView(Monster monster, final View layout) {
        ((TextView) layout.findViewById(R.id.name)).setText(monster.getName(mResources));
        ((ImageView) layout.findViewById(R.id.image)).setImageResource(monster.getImage(mResources));

        if (monster.getSkills().size() > 0) {
            showSkills(layout, monster);
        } else {
            layout.findViewById(R.id.skills).setVisibility(View.GONE);
        }

        int nbFrags = Collections.frequency(mHero.getFrags(), monster.getIdentifier());
        ((TextView) layout.findViewById(R.id.frags)).setText("" + nbFrags);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(getContext(), R.raw.monster);
                layout.findViewById(R.id.image).startAnimation(mBounceAnimation);
            }
        });
    }

    private void showSkills(View rootLayout, Monster monster) {
        ViewGroup skillLayout = (ViewGroup) rootLayout.findViewById(R.id.skills);
        for (int n = 0; n < skillLayout.getChildCount(); n++) {
            if (n < monster.getSkills().size()) {
                updateSkillLayout(skillLayout.getChildAt(n), monster.getSkills().get(n));
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

    public void showSkillInfo(Skill skill) {
        if (mSkillInfoDialog == null || !mSkillInfoDialog.isShowing()) {
            mSkillInfoDialog = new SomethingDetails(mActivity, skill);
            mSkillInfoDialog.show();
        }
    }

}
