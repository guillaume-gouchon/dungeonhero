package com.glevel.dungeonhero.game.models;

import java.io.Serializable;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.glevel.dungeonhero.game.data.ArmiesData;
import com.glevel.dungeonhero.game.graphics.ObjectiveSprite;

public class ObjectivePoint implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7724645757541499751L;

    private transient ObjectiveSprite sprite;

    private ArmiesData owner;
    private final float x;
    private final float y;

    public ObjectivePoint(float x, float y, ArmiesData owner, VertexBufferObjectManager vertexBufferObjectManager) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.sprite = new ObjectiveSprite(x, y, owner, vertexBufferObjectManager);
    }

    public ArmiesData getOwner() {
        return owner;
    }

    public void setOwner(ArmiesData owner) {
        this.owner = owner;
        sprite.updateColor(owner.getColor());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public ObjectiveSprite getSprite() {
        return sprite;
    }

    public void setSprite(ObjectiveSprite sprite) {
        this.sprite = sprite;
    }

}
