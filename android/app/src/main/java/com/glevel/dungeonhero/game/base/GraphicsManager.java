package com.glevel.dungeonhero.game.base;

import android.content.Context;

import com.glevel.dungeonhero.game.graphics.GraphicHolder;
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

        for (GraphicHolder graphicHolder : game.getGraphicsToLoad()) {
            loadGfx(graphicHolder);
        }
    }

    private void loadGfx(GraphicHolder graphicHolder) {
        String spriteName = graphicHolder.getSpriteName();
        if (sGfxMap.get(spriteName) == null) {
            BitmapTextureAtlas mTexture = new BitmapTextureAtlas(mTextureManager, graphicHolder.getSpriteWidth(), graphicHolder.getSpriteHeight(),
                    TextureOptions.DEFAULT);
            TiledTextureRegion tiledTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mTexture,
                    mContext.getAssets(), spriteName, 0, 0, graphicHolder.getNbSpritesX(), graphicHolder.getNbSpritesY());
            mTexture.load();
            sGfxMap.put(spriteName, tiledTexture);
        }
    }

    public void onPause() {
        sGfxMap = new HashMap<String, TiledTextureRegion>();
    }

}
