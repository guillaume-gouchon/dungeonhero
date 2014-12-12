package com.glevel.dungeonhero.models.discussions;

import android.content.res.Resources;

import com.glevel.dungeonhero.models.StorableResource;

import java.io.Serializable;

/**
 * Created by guillaume on 10/13/14.
 */
public class Reaction implements Serializable {

    private static final long serialVersionUID = 3217490571298178133L;

    private final String message;
    private final int skipNextSteps;

    public Reaction(String message, int nextStep) {
        this.message = message;
        this.skipNextSteps = nextStep;
    }

    public int getMessage(Resources resources) {
        return StorableResource.getResource(resources, message, false);
    }

    public int getSkipNextSteps() {
        return skipNextSteps;
    }

}
