package com.glevel.dungeonhero.models.discussions.callbacks;

import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;

public class EnemyDiscussionCallback extends DiscussionCallback {

    private static final long serialVersionUID = 5869054321871679506L;

    public EnemyDiscussionCallback(Pnj pnj) {
        super(pnj);
    }

    @Override
    public void onDiscussionOver() {
        pnj.setRank(Ranks.ENEMY);
    }

}
