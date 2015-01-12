package com.glevel.dungeonhero.models.dungeons.decorations;

import com.glevel.dungeonhero.game.graphics.TreasureSprite;
import com.glevel.dungeonhero.models.Reward;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by guillaume on 10/10/14.
 */
public class TreasureChest extends Searchable {

    private static final long serialVersionUID = -8965387914362690452L;

    public TreasureChest(Reward reward) {
        super("treasure_chest", reward, 34, 20, 2, 1);
    }

    @Override
    public void createSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        sprite = new TreasureSprite(this, vertexBufferObjectManager);
    }

    @Override
    public Reward search() {
        ((TreasureSprite) sprite).open();
        return super.search();
    }

}
