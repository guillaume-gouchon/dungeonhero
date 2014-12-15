package com.glevel.dungeonhero.models.items.equipments;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Ring extends Equipment implements Serializable {

    private static final long serialVersionUID = 4191323307341633727L;

    public Ring(String identifier, int level) {
        super(identifier, level);
    }

}
