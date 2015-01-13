package com.glevel.dungeonhero.models.discussions.callbacks;

import com.glevel.dungeonhero.models.characters.Pnj;

public class DeathDiscussionCallback extends DiscussionCallback {

    private static final long serialVersionUID = -162134829548329521L;

    public DeathDiscussionCallback(Pnj pnj) {
        super(pnj);
    }

    @Override
    public void onDiscussionOver() {
        pnj.setCurrentHP(0);
    }
    
}