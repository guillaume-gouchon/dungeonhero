package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.game.graphics.LightSprite;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume on 10/10/14.
 */
public class Light extends Decoration {

    public Light(String identifier) {
        super(identifier, 21, 20, 3, 1);
    }

    @Override
    public void createSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        sprite = new LightSprite(this, vertexBufferObjectManager);
    }

}
