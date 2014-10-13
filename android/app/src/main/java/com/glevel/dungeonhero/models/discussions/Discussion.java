package com.glevel.dungeonhero.models.discussions;

import com.glevel.dungeonhero.models.Reward;

import java.util.List;

/**
 * Created by guillaume on 10/13/14.
 */
public class Discussion {

    private final int image;
    private final int name;
    private final int message;
    private final List<Reaction> reactions;
    private final boolean isPermanent;
    private final Reward reward;

    public Discussion(int image, int name, int message, List<Reaction> reactions, boolean isPermanent, Reward reward) {
        this.image = image;
        this.name = name;
        this.message = message;
        this.reactions = reactions;
        this.isPermanent = isPermanent;
        this.reward = reward;
    }

    public int getImage() {
        return image;
    }

    public int getName() {
        return name;
    }

    public int getMessage() {
        return message;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public Reward getReward() {
        return reward;
    }

}
