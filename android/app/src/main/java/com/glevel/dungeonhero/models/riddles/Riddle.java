package com.glevel.dungeonhero.models.riddles;

import com.glevel.dungeonhero.models.Reward;

import java.io.Serializable;

/**
 * Created by guillaume on 12/2/14.
 */
public abstract class Riddle implements Serializable {

    private static final long serialVersionUID = -8538469758021996078L;
    private final int timer;
    private final int question;
    private final Reward reward;

    public Riddle(int timer, int question, Reward reward) {
        this.timer = timer;
        this.question = question;
        this.reward = reward;
    }

    public int getTimer() {
        return timer;
    }

    public int getQuestion() {
        return question;
    }

    public Reward getReward() {
        return reward;
    }

}
