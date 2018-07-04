package com.glevel.dungeonhero.models;

import android.content.res.Resources;

import com.glevel.dungeonhero.BaseApplication;

import java.io.Serializable;

public abstract class StorableResource implements Serializable {

    private static final long serialVersionUID = 484509051608106005L;

    protected final String identifier;

    protected StorableResource(String identifier) {
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
        return resources.getIdentifier(identifier, isImage ? "drawable" : "string", BaseApplication.sPackageName);
    }

    public String getIdentifier() {
        return identifier;
    }

}
