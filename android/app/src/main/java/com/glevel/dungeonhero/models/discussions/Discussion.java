package com.glevel.dungeonhero.models.discussions;

import android.content.res.Resources;

import com.glevel.dungeonhero.MyApplication;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.StorableResource;
import com.glevel.dungeonhero.models.discussions.riddles.Riddle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guillaume on 10/13/14.
 */
public class Discussion implements Serializable {

    private static final long serialVersionUID = 4434121243275987291L;

    private String message;
    private List<Reaction> reactions;
    private boolean isPermanent;
    private Reward reward;
    private Riddle riddle;

    public Discussion(String message, List<Reaction> reactions, boolean isPermanent, Reward reward) {
        this.message = message;
        this.message = message;
        this.reactions = reactions;
        this.isPermanent = isPermanent;
        this.reward = reward;
    }

    public Discussion(Riddle riddle) {
        this.riddle = riddle;
    }

    public int getMessage(Resources resources) {
        return StorableResource.getResource(resources, message, false);
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
