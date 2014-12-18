package com.glevel.dungeonhero.game.base.interfaces;

import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Pnj;

/**
 * Created by guillaume on 10/9/14.
 */
public interface OnDiscussionReplySelected {

    public void onReplySelected(Pnj pnj, int next, Reward instantReward);

}
