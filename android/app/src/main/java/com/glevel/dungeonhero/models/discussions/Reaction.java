package com.glevel.dungeonhero.models.discussions;

/**
 * Created by guillaume on 10/13/14.
 */
public class Reaction {

    private final int message;
    private final int skipNextSteps;

    public Reaction(int message, int nextStep) {
        this.message = message;
        this.skipNextSteps = nextStep;
    }

    public int getMessage() {
        return message;
    }

    public int getSkipNextSteps() {
        return skipNextSteps;
    }

}
