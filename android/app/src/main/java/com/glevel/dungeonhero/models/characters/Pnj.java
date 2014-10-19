package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.models.discussions.Discussion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/8/14.
 */
public class Pnj extends Hero {

    private static final long serialVersionUID = 4682214659658928572L;

    private final List<Discussion> discussions = new ArrayList<Discussion>();
    private boolean isActive;

    public Pnj(boolean isActive, Ranks ranks, int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int block, int movement, int name, int description, int coins, int xp, int level) {
        super(ranks, image, spriteName, hp, currentHP, strength, dexterity, spirit, attack, block, movement, name, description, coins, null, xp, level);
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
