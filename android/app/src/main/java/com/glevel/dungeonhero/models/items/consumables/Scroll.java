package com.glevel.dungeonhero.models.items.consumables;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.items.Consumable;

import java.io.Serializable;

/**
 * Created by guillaume ON 10/6/14.
 */
public class Scroll extends Consumable implements Serializable {

    private static final long serialVersionUID = 2296109851474248483L;

    public Scroll(int name, int description, int image) {
        super(name, description, image, R.color.bg_scroll);
    }
    
}
