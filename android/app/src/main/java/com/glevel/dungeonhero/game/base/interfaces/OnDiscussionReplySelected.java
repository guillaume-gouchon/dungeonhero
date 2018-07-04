package com.glevel.dungeonhero.game.base.interfaces;

import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Pnj;

public interface OnDiscussionReplySelected {

    void onReplySelected(Pnj pnj, int next, Reward instantReward);

}
