package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.models.dungeons.Directions;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume ON 10/8/14.
 */
public class UnitSprite extends GameElementSprite {

    public UnitSprite(GameElement gameElement, VertexBufferObjectManager vertexBufferObjectManager) {
        super(gameElement, vertexBufferObjectManager);
        setScale(0.25f);
        stand();
    }

    public void walk(Directions direction) {
        animate(new long[]{300, 300, 300}, direction.ordinal() * 3, direction.ordinal() * 3 + 2, true);
    }

    public void stand() {
        animate(new long[]{300, 300, 300}, Directions.SOUTH.ordinal() * 3, Directions.SOUTH.ordinal() * 3 + 2, true);
    }

}
