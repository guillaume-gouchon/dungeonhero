package com.glevel.dungeonhero.game.gui.skills;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.gui.ElementDetails;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.skills.Skill;

/**
 * Created by guillaume on 1/14/15.
 */
public class ImproveSkill extends ElementDetails {

    public ImproveSkill(Context context, Skill skill, Hero hero, final View.OnClickListener onMainButtonClicked) {
        super(context, skill);

        // actions
        TextView actionButton = (TextView) findViewById(R.id.main_action_btn);
        actionButton.setText(R.string.improve_skill);
        actionButton.setVisibility(skill.canBeImproved() && hero.getSkillPoints() > 0 ? View.VISIBLE : View.GONE);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onMainButtonClicked.onClick(v);
            }
        });
    }

}
