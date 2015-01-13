package com.glevel.dungeonhero.models.discussions.callbacks;

import android.util.Log;

import com.glevel.dungeonhero.data.items.ArmorFactory;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.discussions.Discussion;

public class WhiteWizardDiscussionCallback extends StopDiscussionCallback {

    private static final long serialVersionUID = -5747234029703099616L;
    private static final String TAG = "WhiteWizardDiscussionCallback";

    private final boolean correctAnswer;

    public WhiteWizardDiscussionCallback(Pnj pnj, boolean correctAnswer) {
        super(pnj);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public void onDiscussionOver() {
        if (correctAnswer) {
            pnj.setReward(new Reward(null, pnj.getReward().getGold() + 1, 0));
            Log.d(TAG, "number good answers = " + pnj.getReward().getGold());
            if (pnj.getReward().getGold() == 3) {
                Log.d(TAG, "set riddles reward");
                pnj.getDiscussions().add(new Discussion("white_wizard_right_reward", false, new Reward(ArmorFactory.buildMithrilArmor(), 0, 0), new DeathDiscussionCallback(pnj)));
                pnj.setDiscussionCallback(null);
                return;
            }
        } else {
            pnj.setReward(new Reward(null, 0, 0));
        }
        pnj.setDiscussionCallback(null);
        pnj.setTilePosition(null);
    }

}