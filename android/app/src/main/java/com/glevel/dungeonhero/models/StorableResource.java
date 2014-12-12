package com.glevel.dungeonhero.models;

import android.content.res.Resources;

import com.glevel.dungeonhero.MyApplication;

import java.io.Serializable;

/**
 * Created by guillaume on 12/12/14.
 */
public abstract class StorableResource implements Serializable {

    private static final long serialVersionUID = 484509051608106005L;
    
    protected final String identifier;

    public StorableResource(String identifier) {
        this.identifier = identifier;
    }

    public int getName(Resources resources) {
        return getResource(resources, identifier, false);
    }

    public int getImage(Resources resources) {
        return getResource(resources, identifier, true);
    }

    public int getDescription(Resources resources) {
        return getResource(resources, identifier + "_description", false);
    }

    public static int getResource(Resources resources, String identifier, boolean isImage) {
        return resources.getIdentifier(identifier, isImage ? "drawable" : "string", MyApplication.sPackageName);
    }

    public String getIdentifier() {
        return identifier;
    }

}
