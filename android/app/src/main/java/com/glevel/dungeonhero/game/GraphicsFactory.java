package com.glevel.dungeonhero.game;

import java.util.HashMap;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;

import com.glevel.dungeonhero.game.models.Battle;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.game.models.Player;
import com.glevel.dungeonhero.game.models.units.Cannon;
import com.glevel.dungeonhero.game.models.units.Soldier;
import com.glevel.dungeonhero.game.models.units.Tank;

public class GraphicsFactory {

	private Context mContext;
	private TextureManager mTextureManager;

	public static HashMap<String, TiledTextureRegion> mGfxMap = new HashMap<String, TiledTextureRegion>();

	public GraphicsFactory(Context context, VertexBufferObjectManager vertexBufferObjectManager,
			TextureManager textureManager) {
		mContext = context;
		mTextureManager = textureManager;
	}

	public void initGraphics(Battle battle) {
		mGfxMap = new HashMap<String, TiledTextureRegion>();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// load all game elements graphics
		for (Player player : battle.getPlayers()) {
			for (GameElement gameElement : player.getUnits()) {
				if (gameElement instanceof Tank) {
					loadGfxFromAssets(164, 164, gameElement.getSpriteName());
					loadGfxFromAssets(164, 164, gameElement.getSpriteName().replace(".png", "") + "_turret.png");
				} else if (gameElement instanceof Soldier) {
					loadTiledGfxFromAssets(288, 192, 3, 2, gameElement.getSpriteName());
				} else if (gameElement instanceof Cannon) {
					loadTiledGfxFromAssets(164, 164, 1, 1, gameElement.getSpriteName());
				}
			}
		}

		// stuff to load
		loadGfxFromAssets(128, 128, "selection.png");
		loadGfxFromAssets(128, 128, "crosshair.png");
		loadGfxFromAssets(64, 64, "muzzle_flash.png");
		loadGfxFromAssets(128, 128, "protection.png");
		loadGfxFromAssets(128, 128, "hide.png");
		loadGfxFromAssets(256, 256, "objective.png");
		loadTiledGfxFromAssets(256, 256, 4, 4, "explosion.png");
		loadTiledGfxFromAssets(512, 256, 4, 2, "smoke.png");
		loadTiledGfxFromAssets(512, 256, 4, 2, "tank_move_smoke.png");
		loadTiledGfxFromAssets(312, 50, 6, 1, "blood.png");
	}

	private void loadGfxFromAssets(int textureWidth, int textureHeight, String spriteName) {
		loadTiledGfxFromAssets(textureWidth, textureHeight, 1, 1, spriteName);
	}

	private void loadTiledGfxFromAssets(int textureWidth, int textureHeight, int x, int y, String spriteName) {
		if (mGfxMap.get(spriteName) == null) {
			BitmapTextureAtlas mTexture = new BitmapTextureAtlas(mTextureManager, textureWidth, textureHeight,
					TextureOptions.DEFAULT);
			TiledTextureRegion tiledTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mTexture,
					mContext.getAssets(), spriteName, 0, 0, x, y);
			mTexture.load();
			mGfxMap.put(spriteName, tiledTexture);
		}
	}

	public void onPause() {
		mGfxMap = new HashMap<String, TiledTextureRegion>();
	}

}
