package com.glevel.dungeonhero.models.discussions;

import java.io.Serializable;

/**
 * Created by guillaume on 10/13/14.
 */
public class Reaction implements Serializable {

    private static final long serialVersionUID = 3217490571298178133L;

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
