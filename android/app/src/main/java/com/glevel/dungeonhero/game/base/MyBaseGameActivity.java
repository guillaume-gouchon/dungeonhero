package com.glevel.dungeonhero.game.base;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.GUIManager;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.game.andengine.custom.CenteredSprite;
import com.glevel.dungeonhero.game.andengine.custom.CustomLayoutGameActivity;
import com.glevel.dungeonhero.game.andengine.custom.CustomZoomCamera;
import com.glevel.dungeonhero.game.base.interfaces.OnActionExecuted;
import com.glevel.dungeonhero.game.base.interfaces.UserActionListener;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.providers.MyContentProvider;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.util.color.Color;

public abstract class MyBaseGameActivity extends CustomLayoutGameActivity implements UserActionListener, View.OnClickListener {

    protected SharedPreferences mSharedPrefs;

    protected boolean mDoSaveGame = true;
    protected Game mGame;

    protected Scene mScene;
    protected ZoomCamera mCamera;
    protected GraphicsManager mGraphicsManager;
    protected InputManager mInputManager;
    protected GUIManager mGUIManager;
    protected SoundEffectManager mSoundEffectManager;
    protected Font mDefaultFont;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isLandscape = mSharedPrefs.getBoolean(GameConstants.GAME_PREFS_LANDSCAPE, false);

        mCamera = new CustomZoomCamera(0, 0, isLandscape ? GameConstants.CAMERA_WIDTH : GameConstants.CAMERA_HEIGHT,
                isLandscape ? GameConstants.CAMERA_HEIGHT : GameConstants.CAMERA_WIDTH, GameConstants.CAMERA_ZOOM_MIN, GameConstants.CAMERA_ZOOM_MAX);
        EngineOptions engineOptions = new EngineOptions(true, isLandscape ? ScreenOrientation.LANDSCAPE_SENSOR : ScreenOrientation.PORTRAIT_SENSOR,
                new FillResolutionPolicy(), mCamera);
        engineOptions.getAudioOptions().setNeedsSound(true);
        return engineOptions;
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // init GUI
        mGUIManager = new GUIManager(this);
        mGUIManager.initGUI();
        mGUIManager.showLoadingScreen();

        initGameActivity();
    }

    protected abstract void initGameActivity();

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.surfaceView;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        // init game element factory
        mGraphicsManager = new GraphicsManager(this, getTextureManager());
        mGraphicsManager.initGraphics(mGame);

        // init sound manager
        mSoundEffectManager = new SoundEffectManager(this, mSharedPrefs.getInt(GameConstants.GAME_PREFS_KEY_MUSIC_VOLUME,
                GameConstants.MusicStates.ON.ordinal()));
        mSoundEffectManager.init(mGame, mEngine);

        // load font
        mDefaultFont = FontFactory.create(getFontManager(), getTextureManager(), 256, 256,
                Typeface.create(Typeface.createFromAsset(getAssets(), "fonts/font_text.otf"), Typeface.BOLD), 32, Color.WHITE.hashCode());
        mDefaultFont.load();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        mInputManager = new InputManager(mCamera, this);
        prepareNewScene();
    }

    protected void prepareNewScene() {
        mScene = new Scene();
        mScene.setOnAreaTouchTraversalFrontToBack();
        mScene.setBackground(new Background(0, 0, 0));
        mScene.setOnSceneTouchListener(mInputManager);
        mScene.setTouchAreaBindingOnActionDownEnabled(true);
        mEngine.setScene(mScene);
    }

    @Override
    protected synchronized void onResume() {
        if (GraphicsManager.sGfxMap.size() == 0 && mGraphicsManager != null) {
            mEngine.stop();
            mGraphicsManager.initGraphics(mGame);
            mSoundEffectManager.init(mGame, mEngine);
            mEngine.start();
        }

        super.onResume();

    }

    @Override
    public void onBackPressed() {
        pauseGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGUIManager != null) {
            mGUIManager.onPause();
        }
        if (mGraphicsManager != null) {
            mGraphicsManager.onPause();
        }
        if (mSoundEffectManager != null) {
            mSoundEffectManager.onPause();
        }
        if (mDoSaveGame) {
            getContentResolver().insert(MyContentProvider.URI_GAMES, mGame.toContentValues());
        }
    }

    private void pauseGame() {
        mGUIManager.openGameMenu();
        mEngine.stop();
    }

    public void resumeGame() {
        mEngine.start();
    }

    public void addElementToScene(Shape shape, boolean isClickable) {
        if (isClickable) {
            mScene.registerTouchArea(shape);
        }
        mScene.attachChild(shape);
    }

    public void removeElement(final Shape shape, boolean isClickable) {
        if (isClickable) {
            mScene.unregisterTouchArea(shape);
        }
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                mScene.detachChild(shape);
            }
        });
    }

    public void endGame() {
        // stop engine
        mEngine.stop();
    }

    public void drawSprite(float x, float y, String spriteName, final int duration, final int size) {
        final Sprite sprite = new CenteredSprite(x, y, GraphicsManager.sGfxMap.get(spriteName), getVertexBufferObjectManager());
        sprite.setScale(size);
        mScene.attachChild(sprite);
        if (duration > 0) {
            sprite.registerUpdateHandler(new IUpdateHandler() {

                private int timeLeft = duration;

                public void onUpdate(float pSecondsElapsed) {
                    if (--timeLeft <= 0) {
                        // remove sprite
                        runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                mScene.detachChild(sprite);
                            }
                        });
                    }
                }

                @Override
                public void reset() {
                }

            });
        }
    }

    public void drawAnimatedText(float x, float y, String text, Color color, float scale, final int duration, final float offset) {
        final Text animatedText = new Text(x, y, mDefaultFont, text, getVertexBufferObjectManager());
        animatedText.setScaleCenter(0.5f, 0.5f);
        animatedText.setColor(color);
        animatedText.setScale(scale);
        animatedText.setAlpha(0.75f);
        mScene.attachChild(animatedText);
        if (duration > 0) {
            animatedText.registerUpdateHandler(new IUpdateHandler() {

                private int timeLeft = duration;

                public void onUpdate(float pSecondsElapsed) {
                    if (offset != 0) {
                        animatedText.setPosition(animatedText.getX(), animatedText.getY() + offset);
                    }

                    if (--timeLeft <= 0) {
                        // remove sprite
                        runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                mScene.detachChild(animatedText);
                            }
                        });
                    }
                }

                @Override
                public void reset() {
                }

            });
        }
    }

    public void drawAnimatedSprite(float x, float y, String spriteName, int frameDuration, float scale, float alpha, int loopCount, final boolean removeAfter, int zIndex, final OnActionExecuted callback) {
        if (GraphicsManager.sGfxMap.get(spriteName) == null) return;

        final AnimatedSprite sprite = new AnimatedSprite(0, 0, GraphicsManager.sGfxMap.get(spriteName), getVertexBufferObjectManager());
        sprite.setPosition(x - sprite.getWidth() / 2.0f, y - sprite.getWidth() / 2.0f);
        sprite.setZIndex(zIndex);
        sprite.setScale(scale);
        sprite.setAlpha(alpha);
        if (loopCount == -1) {
            sprite.animate(frameDuration, true);
        } else {
            sprite.animate(frameDuration, loopCount, new AnimatedSprite.IAnimationListener() {
                @Override
                public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
                }

                @Override
                public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount,
                                                    int pInitialLoopCount) {
                }

                @Override
                public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex,
                                                    int pNewFrameIndex) {
                }

                @Override
                public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
                    if (removeAfter) {
                        // remove sprite
                        runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                mScene.detachChild(sprite);
                            }
                        });
                    } else {
                        pAnimatedSprite.setCurrentTileIndex(1);
                    }

                    if (callback != null) {
                        callback.onActionDone(true);
                    }
                }
            });
        }
        mScene.attachChild(sprite);
    }

    public void playSound(String soundName, boolean isLooped) {
        mSoundEffectManager.playSound(soundName, isLooped);
    }

    public void playGeoSound(String soundName, float x, float y) {
        mSoundEffectManager.playGeoSound(soundName, x, y);
    }

    public InputManager getInputManager() {
        return mInputManager;
    }

    public GUIManager getGUIManager() {
        return mGUIManager;
    }

    public Game getGame() {
        return mGame;
    }

    public ZoomCamera getCamera() {
        return mCamera;
    }

}
