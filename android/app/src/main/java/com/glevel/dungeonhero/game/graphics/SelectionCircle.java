package com.glevel.dungeonhero.game.graphics;

import com.glevel.dungeonhero.game.andengine.custom.CenteredSprite;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.GraphicsManager;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SelectionCircle extends CenteredSprite {

    public SelectionCircle(VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, 0, GraphicsManager.sGfxMap.get("selection.png"), pVertexBufferObjectManager);
        setScale(0.5f);
        setZIndex(5);
        setVisible(false);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        this.setRotation(getRotation() + 0.8f);
        super.onManagedUpdate(pSecondsElapsed);
    }

    public void attachToGameElement(GameElement gameElement) {
        GameElementSprite sprite = gameElement.getSprite();
        setColor(gameElement.getSelectionColor());
        setPosition(sprite.getX(), sprite.getY() - GameElementSprite.Y_OFFSET + getHeightScaled() / 2);
        setVisible(true);
    }

    public void unAttach() {
        setVisible(false);
    }

}
