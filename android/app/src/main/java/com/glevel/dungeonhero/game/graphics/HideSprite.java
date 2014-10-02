package com.glevel.dungeonhero.game.graphics;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class HideSprite extends CenteredSprite {

	private static final float ALPHA_ANIMATION_SPEED = 0.005f;
	private static final float ALPHA_ANIMATION_LIMIT = 0.2f;
	private static final float INITIAL_ALPHA = 0.5f;
	private static final float INITIAL_SCALE = 0.3f;
	private boolean mIsGrowing = true;

	public HideSprite(final ITiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(-50, -50, pTextureRegion, pVertexBufferObjectManager);
		this.setColor(0.0f, 1.0f, 0.0f);
		this.setScale(INITIAL_SCALE);
		this.setAlpha(INITIAL_ALPHA);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		// alpha animation
		this.setAlpha(getAlpha() + ALPHA_ANIMATION_SPEED * (mIsGrowing ? 1 : -1));
		if (Math.abs(getAlpha() - INITIAL_ALPHA) > ALPHA_ANIMATION_LIMIT) {
			mIsGrowing = !mIsGrowing;
		}
		super.onManagedUpdate(pSecondsElapsed);
	}

}
