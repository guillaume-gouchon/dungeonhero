package com.glevel.dungeonhero.models.discussions.riddles;

import android.content.res.Resources;

import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.StorableResource;

import java.io.Serializable;

/**
 * Created by guillaume on 12/2/14.
 */
public abstract class Riddle implements Serializable {

    private static final long serialVersionUID = -8538469758021996078L;
    
    private final int timer;
    private final String question;
    private final Reward reward;

    public Riddle(int timer, String question, Reward reward) {
        this.timer = timer;
        this.question = question;
        this.reward = reward;
    }

    public int getTimer() {
        return timer;
    }

    public int getQuestion(Resources resources) {
        return StorableResource.getResource(resources, question, false);
    }

    public Reward getReward() {
        return reward;
    }

}
