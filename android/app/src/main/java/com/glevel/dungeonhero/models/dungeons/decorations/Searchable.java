package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.game.graphics.GameElementSprite;
import com.glevel.dungeonhero.models.Reward;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.io.Serializable;

/**
 * Created by guillaume on 10/10/14.
 */
public class Searchable extends Decoration implements Serializable {

    private static final long serialVersionUID = -705814371353878655L;

    private boolean isEmpty = false;
    private Reward reward;

    public Searchable(int name, String spriteName, Reward reward, int spriteWidth, int spriteHeight, int nbSpritesX, int nbSpritesY) {
        super(name, spriteName, spriteWidth, spriteHeight, nbSpritesX, nbSpritesY);
        this.reward = reward;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Reward search() {
        if (isEmpty) return null;

        isEmpty = true;
        return reward;
    }

    @Override
    public void createSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        sprite = new GameElementSprite(this, vertexBufferObjectManager);
        sprite.setZIndex(11);
    }

}
