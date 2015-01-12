package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.DiscussionCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Pnj extends Hero {

    private final List<Discussion> discussions = new ArrayList<>();
    private DiscussionCallback onDiscussionOver = null;
    private final boolean isAutoTalk;

    public Pnj(String identifier, Ranks ranks, int hp, int currentHP, int strength, int dexterity, int spirit, int movement, int xp, int level, HeroTypes heroType, boolean isAutoTalk) {
        super(identifier, ranks, hp, currentHP, strength, dexterity, spirit, movement, null, xp, level, heroType);
        this.isAutoTalk = isAutoTalk;
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

    public DiscussionCallback getDiscussionCallback() {
        return onDiscussionOver;
    }

    public void setDiscussionCallback(DiscussionCallback onDiscussionOver) {
        this.onDiscussionOver = onDiscussionOver;
    }

    public Discussion getNextDiscussion() {
        // update end of discussion callback action
        if (discussions.get(0).getOnDiscussionOver() != null) {
            this.onDiscussionOver = discussions.get(0).getOnDiscussionOver();
        }
        return discussions.get(0);
    }

    public boolean isAutoTalk() {
        return isAutoTalk;
    }

}
