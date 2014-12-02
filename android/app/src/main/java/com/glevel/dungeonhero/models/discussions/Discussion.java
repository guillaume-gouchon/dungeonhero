package com.glevel.dungeonhero.models.discussions;

import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.riddles.Riddle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guillaume on 10/13/14.
 */
public class Discussion implements Serializable {

    private static final long serialVersionUID = 4434121243275987291L;

    private final int image;
    private final int name;
    private int message;
    private List<Reaction> reactions;
    private boolean isPermanent;
    private Reward reward;
    private Riddle riddle;

    public Discussion(int image, int name, int message, List<Reaction> reactions, boolean isPermanent, Reward reward) {
        this.image = image;
        this.name = name;
        this.message = message;
        this.reactions = reactions;
        this.isPermanent = isPermanent;
        this.reward = reward;
    }

    public Discussion(int image, int name, Riddle riddle) {
        this.image = image;
        this.name = name;
        this.riddle = riddle;
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

    public Riddle getRiddle() {
        return riddle;
    }

}
