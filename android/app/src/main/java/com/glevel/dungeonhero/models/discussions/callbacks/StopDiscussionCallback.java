package com.glevel.dungeonhero.models.discussions.callbacks;

import com.glevel.dungeonhero.models.characters.Pnj;

public class StopDiscussionCallback extends DiscussionCallback {

    private static final long serialVersionUID = -3763582792183993581L;

    public StopDiscussionCallback(Pnj pnj) {
        super(pnj);
    }

    @Override
    public void onDiscussionOver() {
    }

}