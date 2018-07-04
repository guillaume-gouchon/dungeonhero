package com.glevel.dungeonhero.models.discussions.callbacks;

import com.glevel.dungeonhero.models.characters.Pnj;

import java.io.Serializable;

public abstract class DiscussionCallback implements Serializable {

    private static final long serialVersionUID = 1025748464560723997L;

    final Pnj pnj;

    DiscussionCallback(Pnj pnj) {
        this.pnj = pnj;
    }

    public abstract void onDiscussionOver();

}
