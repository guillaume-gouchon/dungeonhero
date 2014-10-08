package com.glevel.dungeonhero.game;

import android.content.Context;

import com.glevel.dungeonhero.models.Game;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;

public class GraphicsManager {

    private static final String ASSETS_PATH = "gfx/";

    private Context mContext;
    private TextureManager mTextureManager;

    public static HashMap<String, TiledTextureRegion> sGfxMap = new HashMap<String, TiledTextureRegion>();

    public GraphicsManager(Context context, VertexBufferObjectManager vertexBufferObjectManager, TextureManager textureManager) {
        mContext = context;
        mTextureManager = textureManager;
    }

    public void initGraphics(Game game) {
        sGfxMap = new HashMap<String, TiledTextureRegion>();

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(ASSETS_PATH);

        // TODO load all game elements graphics
        loadTiledGfxFromAssets(227, 400, 3, 4, game.getHero().getSpriteName());
    }

    private void loadGfxFromAssets(int textureWidth, int textureHeight, String spriteName) {
        loadTiledGfxFromAssets(textureWidth, textureHeight, 1, 1, spriteName);
    }

    private void loadTiledGfxFromAssets(int textureWidth, int textureHeight, int x, int y, String spriteName) {
        if (sGfxMap.get(spriteName) == null) {
            BitmapTextureAtlas mTexture = new BitmapTextureAtlas(mTextureManager, textureWidth, textureHeight,
                    TextureOptions.DEFAULT);
            TiledTextureRegion tiledTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mTexture,
                    mContext.getAssets(), spriteName, 0, 0, x, y);
            mTexture.load();
            sGfxMap.put(spriteName, tiledTexture);
        }
    }

    public void onPause() {
        sGfxMap = new HashMap<String, TiledTextureRegion>();
    }

}
