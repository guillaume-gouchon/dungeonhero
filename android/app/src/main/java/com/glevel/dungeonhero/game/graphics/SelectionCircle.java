package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.GraphicsManager;
import com.glevel.dungeonhero.game.andengine.custom.CenteredSprite;
import com.glevel.dungeonhero.game.models.GameElement;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SelectionCircle extends CenteredSprite {

    public SelectionCircle(VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, 0, GraphicsManager.sGfxMap.get("selection.png"), pVertexBufferObjectManager);
        setScale(0.25f);
        setVisible(false);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        this.setRotation(getRotation() + 0.8f);
        super.onManagedUpdate(pSecondsElapsed);
    }

    public void attachToGameElement(GameElement gameElement) {
        Sprite sprite = gameElement.getSprite();
        setColor(gameElement.getSelectionColor());
        setPosition(sprite.getX(), sprite.getY() + getHeightScaled() / 2);
        setVisible(true);
    }

    public void unAttach() {
        setVisible(false);
    }

}
