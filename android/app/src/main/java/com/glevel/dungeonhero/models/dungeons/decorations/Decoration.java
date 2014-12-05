package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.characters.Ranks;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.io.Serializable;

/**
 * Created by guillaume on 10/10/14.
 */
public abstract class Decoration extends GameElement implements Serializable {

    private static final long serialVersionUID = -705814371353878655L;

    public Decoration(int name, String spriteName, int spriteWidth, int spriteHeight, int nbSpritesX, int nbSpritesY) {
        super(name, spriteName, Ranks.NEUTRAL, spriteWidth, spriteHeight, nbSpritesX, nbSpritesY);
    }

}
