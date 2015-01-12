package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.characters.Ranks;

/**
 * Created by guillaume on 10/10/14.
 */
public abstract class Decoration extends GameElement {

    private static final long serialVersionUID = -5804554337397517745L;

    public Decoration(String identifier, int spriteWidth, int spriteHeight, int nbSpritesX, int nbSpritesY) {
        super(identifier, Ranks.NEUTRAL, spriteWidth, spriteHeight, nbSpritesX, nbSpritesY);
    }

}
