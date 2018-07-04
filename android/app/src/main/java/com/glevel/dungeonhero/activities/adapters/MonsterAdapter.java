package com.glevel.dungeonhero.activities.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.gui.ElementDetails;
import com.glevel.dungeonhero.models.StorableResource;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.utils.ApplicationUtils;
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

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.monster_item, null);
        bindMonsterToView(getItem(position), layout);
        return layout;
    }

    private void bindMonsterToView(Monster monster, final View layout) {
        ((TextView) layout.findViewById(R.id.name)).setText(monster.getName(mResources));
        ((ImageView) layout.findViewById(R.id.image)).setImageResource(monster.getImage(mResources));

        addMonsterInfo(layout, monster);

        int nbFrags = Collections.frequency(mHero.getFrags(), monster.getIdentifier());
        ((TextView) layout.findViewById(R.id.frags)).setText(String.valueOf(nbFrags));

        layout.setOnClickListener(v -> {
            MusicManager.playSound(getContext(), R.raw.monster);
            layout.findViewById(R.id.image).startAnimation(mBounceAnimation);
        });
    }

    private void addMonsterInfo(View rootLayout, Monster monster) {
        ViewGroup skillLayout = rootLayout.findViewById(R.id.skills);

        int index = 0;

        // main weapon
        bindElementToView(skillLayout.getChildAt(index++), monster.getEquipments()[0]);

        // armor
        if (monster.getEquipments()[2] != null) {
            bindElementToView(skillLayout.getChildAt(index++), monster.getEquipments()[2]);
        }

        // skills
        for (int n = index; n < skillLayout.getChildCount(); n++) {
            if (n - index < monster.getSkills().size()) {
                bindElementToView(skillLayout.getChildAt(n), monster.getSkills().get(n - index));
            } else {
                skillLayout.getChildAt(n).setVisibility(View.GONE);
            }
        }
    }

    private void bindElementToView(View itemView, StorableResource element) {
        ImageView image = itemView.findViewById(R.id.image);
        image.setImageResource(element.getImage(mResources));
        ApplicationUtils.setAlpha(image, 0.6f);
        itemView.setTag(R.string.item, element);
        itemView.setOnClickListener(v -> showElementInfo((StorableResource) v.getTag(R.string.item)));
    }

    private void showElementInfo(StorableResource element) {
        if (mSkillInfoDialog == null || !mSkillInfoDialog.isShowing()) {
            mSkillInfoDialog = new ElementDetails(mActivity, element);
            mSkillInfoDialog.show();
        }
    }

}
