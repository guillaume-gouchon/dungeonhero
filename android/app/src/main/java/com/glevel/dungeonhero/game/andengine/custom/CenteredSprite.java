package com.glevel.dungeonhero.game.andengine.custom;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class CenteredSprite extends AnimatedSprite {

	public CenteredSprite(float pX, float pY, float pWidth, float pHeight, ITiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);
	}

	public CenteredSprite(float pX, float pY, ITiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}

	@Override
	public float getX() {
		return super.getX() + this.getWidth() / 2.0f;
	}

	@Override
	public float getY() {
		return super.getY() + this.getHeight() / 2.0f;
	}

	@Override
	public void setPosition(float pX, float pY) {
		super.setPosition(pX - this.getWidth() / 2.0f, pY - this.getHeight() / 2.0f);
	}

}
