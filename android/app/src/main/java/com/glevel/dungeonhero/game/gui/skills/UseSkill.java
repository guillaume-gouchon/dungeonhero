package com.glevel.dungeonhero.game.gui.skills;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.gui.ElementDetails;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;

/**
 * Created by guillaume on 1/14/15.
 */
public class UseSkill extends ElementDetails {

    public UseSkill(Context context, Skill skill, final View.OnClickListener onMainButtonClicked) {
        super(context, skill);

        // actions
        if (skill instanceof ActiveSkill && !((ActiveSkill) skill).isUsed()) {
            TextView actionButton = (TextView) findViewById(R.id.main_action_btn);
            actionButton.setText(R.string.use);
            actionButton.setTag(R.string.use_skill, skill);
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    onMainButtonClicked.onClick(v);
                }
            });
        }
    }

}
