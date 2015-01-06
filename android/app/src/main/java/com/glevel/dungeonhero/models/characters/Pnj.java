package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.game.base.interfaces.OnActionExecuted;
import com.glevel.dungeonhero.models.discussions.Discussion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Pnj extends Hero {

    private static final long serialVersionUID = 4682214659658928572L;

    private final List<Discussion> discussions = new ArrayList<>();
    private OnActionExecuted onDiscussionOver = null;

    public Pnj(String identifier, Ranks ranks, int hp, int currentHP, int strength, int dexterity, int spirit, int movement, int xp, int level, HeroTypes heroType) {
        super(identifier, ranks, hp, currentHP, strength, dexterity, spirit, movement, null, xp, level, heroType);
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

    public OnActionExecuted getOnDiscussionOver() {
        return onDiscussionOver;
    }

    public void setOnDiscussionOver(OnActionExecuted onDiscussionOver) {
        this.onDiscussionOver = onDiscussionOver;
    }

    public Discussion getNextDiscussion() {
        // update end of discussion callback action
        if (discussions.get(0).getOnActionExecuted() != null) {
            this.onDiscussionOver = discussions.get(0).getOnActionExecuted();
        }
        return discussions.get(0);
    }

}
