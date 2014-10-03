package com.glevel.dungeonhero.activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.AI;
import com.glevel.dungeonhero.game.GameGUI;
import com.glevel.dungeonhero.game.GameUtils;
import com.glevel.dungeonhero.game.GraphicsFactory;
import com.glevel.dungeonhero.game.InputManager;
import com.glevel.dungeonhero.game.SoundEffectManager;
import com.glevel.dungeonhero.game.andengine.custom.CustomLayoutGameActivity;
import com.glevel.dungeonhero.game.andengine.custom.CustomZoomCamera;
import com.glevel.dungeonhero.game.graphics.CannonSprite;
import com.glevel.dungeonhero.game.graphics.CenteredSprite;
import com.glevel.dungeonhero.game.graphics.Crosshair;
import com.glevel.dungeonhero.game.graphics.DeploymentZone;
import com.glevel.dungeonhero.game.graphics.ObjectiveSprite;
import com.glevel.dungeonhero.game.graphics.Protection;
import com.glevel.dungeonhero.game.graphics.SelectionCircle;
import com.glevel.dungeonhero.game.graphics.SoldierSprite;
import com.glevel.dungeonhero.game.graphics.TankSprite;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.game.interfaces.OnNewSoundToPlay;
import com.glevel.dungeonhero.game.interfaces.OnNewSpriteToDraw;
import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.game.models.Battle;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.game.models.ObjectivePoint;
import com.glevel.dungeonhero.game.models.Player;
import com.glevel.dungeonhero.game.models.map.Tile;
import com.glevel.dungeonhero.game.models.orders.FireOrder;
import com.glevel.dungeonhero.game.models.orders.MoveOrder;
import com.glevel.dungeonhero.game.models.orders.Order;
import com.glevel.dungeonhero.game.models.units.Cannon;
import com.glevel.dungeonhero.game.models.units.Soldier;
import com.glevel.dungeonhero.game.models.units.Tank;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.utils.database.DatabaseHelper;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import java.util.List;

public class GameActivity extends CustomLayoutGameActivity implements OnNewSpriteToDraw, OnNewSoundToPlay {

    public static final String EXTRA_GAME_ID = "game_id";

    private DatabaseHelper mDbHelper;
    protected SharedPreferences mSharedPrefs;
    private GraphicsFactory mGameElementFactory;
    private InputManager mInputManager;
    protected GameGUI mGameGUI;
    private SoundEffectManager mSoundEffectManager;

    public Battle battle = null;
    protected boolean mMustSaveGame = true;

    public Scene mScene;
    private ZoomCamera mCamera;
    public TMXLayer tmxLayer;
    public TMXTiledMap mTMXTiledMap;

    private Font mFont;
    public Sprite selectionCircle;
    public Line orderLine;
    public Crosshair crosshair, crossHairLine;
    public Protection protection;
    private DeploymentZone deploymentZone;

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new CustomZoomCamera(0, 0, GameUtils.CAMERA_WIDTH, GameUtils.CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy(), mCamera);
        engineOptions.getAudioOptions().setNeedsSound(true);
        return engineOptions;
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        initGameActivity();
    }

    protected void initGameActivity() {
//        mDbHelper = new DatabaseHelper(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // load game or new game
            long gameId = extras.getLong("game_id", 0);
//            battle = mDbHelper.getGamDao().getById(gameId);
        }

        if (battle == null) {
            // load last saved battle
//            battle = GameConverterHelper.getUnfinishedBattles(mDbHelper).get(0);
        }

//        battle.setDifficultyLevel(DifficultyLevel.values()[mSharedPrefs.getInt(GameUtils.GAME_PREFS_KEY_DIFFICULTY,
//                DifficultyLevel.medium.ordinal())]);
        battle.setOnNewSprite(this);
        battle.setOnNewSoundToPlay(this);

        // init GUI
        mGameGUI = new GameGUI(this);
        mGameGUI.showLoadingScreen();
        mGameGUI.initGUI();

        // used to keep only one saved battle for each game mode / campaign
        battle.setId(0L);
//        GameConverterHelper.deleteSavedBattles(mDbHelper, battle.getCampaignId());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_game;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.surfaceView;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        // init game element factory
        mGameElementFactory = new GraphicsFactory(this, getVertexBufferObjectManager(), getTextureManager());
        mGameElementFactory.initGraphics(battle);

        // init sound manager
        mSoundEffectManager = new SoundEffectManager(this, mSharedPrefs.getInt(GameUtils.GAME_PREFS_KEY_MUSIC_VOLUME,
                GameUtils.MusicState.on.ordinal()));
        mSoundEffectManager.setCamera(mCamera);
        mSoundEffectManager.init(battle, mEngine);

        // load font
        mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, Color.WHITE.hashCode());
        mFont.load();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    protected synchronized void onResume() {
        if (GraphicsFactory.mGfxMap.size() == 0 && mGameElementFactory != null) {
            mEngine.stop();
            mGameElementFactory.initGraphics(battle);
            mSoundEffectManager.init(battle, mEngine);
            mEngine.start();
        }

        super.onResume();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        // prepare scene
        mScene = new Scene();
        mScene.setOnAreaTouchTraversalFrontToBack();
        mScene.setBackground(new Background(0, 0, 0));
        mInputManager = new InputManager(this, mCamera);
        this.mScene.setOnSceneTouchListener(mInputManager);
        this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

        // prepare tile map
        try {
            final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(),
                    TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), null);
            this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/" + battle.getTileMapName());
        } catch (final TMXLoadException e) {
            Debug.e(e);
        }
        tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
        mScene.attachChild(tmxLayer);

        // make the camera not exceed the bounds of the TMXEntity
        this.mCamera.setBounds(0, 0, tmxLayer.getHeight(), tmxLayer.getWidth());
        this.mCamera.setBoundsEnabled(true);
        this.mCamera.setZoomFactor(0.5f);

        pOnCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        // add selection circle
        selectionCircle = new SelectionCircle(GraphicsFactory.mGfxMap.get("selection.png"),
                getVertexBufferObjectManager());
        selectionCircle.setZIndex(5);

        // add crosshairs, icons and order line
        crosshair = new Crosshair(GraphicsFactory.mGfxMap.get("crosshair.png"), getVertexBufferObjectManager());
        crosshair.setVisible(false);
        crosshair.setZIndex(101);
        crosshair.setAlpha(0.7f);
        mScene.attachChild(crosshair);

        Text distanceText = new Text(0, 0, mFont, "", 5, getVertexBufferObjectManager());
        mScene.attachChild(distanceText);

        crossHairLine = new Crosshair(GraphicsFactory.mGfxMap.get("crosshair.png"), getVertexBufferObjectManager(),
                distanceText);
        crossHairLine.setVisible(false);
        crossHairLine.setZIndex(101);
        crossHairLine.setAlpha(0.7f);
        mScene.attachChild(crossHairLine);

        protection = new Protection(GraphicsFactory.mGfxMap.get("protection.png"), getVertexBufferObjectManager());
        protection.setVisible(false);
        protection.setZIndex(101);
        mScene.attachChild(protection);

        orderLine = new Line(0, 0, 0, 0, getVertexBufferObjectManager());
        orderLine.setColor(0.5f, 1f, 0.3f);
        orderLine.setAlpha(0.7f);
        orderLine.setLineWidth(4.0f);
        orderLine.setZIndex(100);
        mScene.attachChild(orderLine);

        // init battle's map tiles
        Tile[][] lstTiles = new Tile[tmxLayer.getTileRows()][tmxLayer.getTileColumns()];
        for (TMXTile[] tt : tmxLayer.getTMXTiles()) {
            for (TMXTile t : tt) {
                lstTiles[t.getTileRow()][t.getTileColumn()] = new Tile(t, mTMXTiledMap);
            }
        }
        battle.getMap().setTiles(lstTiles);
        battle.getMap().setTmxLayer(tmxLayer);

        // add armies to scene
        for (Player player : battle.getPlayers()) {

            int[] deploymentBoundaries = battle.getDeploymentBoundaries(player);

            if (!battle.isStarted()) {
                // deploy troops for the first time
                AI.deployTroops(battle, player);

                // add objectives for the first time
                for (int n = 0; n < GameUtils.NUMBER_OBJECTIVES_PER_PLAYER; n++) {
                    Tile tile = battle.getMap().getTiles()[(int) ((1.0 * n + 0.2 + Math.random() * 0.6)
                            * (battle.getMap().getHeight() - 2) / GameUtils.NUMBER_OBJECTIVES_PER_PLAYER)][(int) (1 + deploymentBoundaries[0] + Math
                            .random() * (deploymentBoundaries[1] - deploymentBoundaries[0] - 2))];
                    ObjectivePoint objective = new ObjectivePoint(tile.getTileColumn() * GameUtils.PIXEL_BY_TILE,
                            tile.getTileRow() * GameUtils.PIXEL_BY_TILE, player.getArmy(),
                            getVertexBufferObjectManager());
                    battle.getObjectives().add(objective);
                    mScene.attachChild(objective.getSprite());
                }
            } else {
                // setup objectives sprites
                for (ObjectivePoint objective : battle.getObjectives()) {
                    objective.setSprite(new ObjectiveSprite(objective.getX(), objective.getY(), objective.getOwner(),
                            getVertexBufferObjectManager()));
                    mScene.attachChild(objective.getSprite());
                }
            }
            // setup objective tiles
            for (ObjectivePoint objective : battle.getObjectives()) {
                Tile centerTile = MapLogic.getTileAtCoordinates(battle.getMap(), objective.getX(), objective.getY());
                List<Tile> adjacentTiles = MapLogic.getAdjacentTiles(battle.getMap(), centerTile,
                        GameUtils.OBJECTIVE_RADIUS_IN_TILE, true);
                for (Tile tile : adjacentTiles) {
                    tile.setObjective(objective);
                }
                centerTile.setObjective(objective);
            }

            for (Unit unit : player.getUnits()) {
                // add element to scene / create sprite
//                addElementToScene(unit, player.getArmyIndex() == 0);
                unit.setOrder(unit.getOrder());

                if (battle.isStarted()) {
                    // load position and rotation
                    float currentX = unit.getCurrentX();
                    float currentY = unit.getCurrentY();
                    float currentRotation = unit.getCurrentRotation();
                    Tile t = MapLogic.getTileAtCoordinates(battle.getMap(), currentX, currentY);
                    unit.setTilePosition(battle, t);
                    unit.getSprite().setX(currentX);
                    unit.getSprite().setY(currentY);
                    unit.getSprite().setRotation(currentRotation);
                    if (unit.isDead()) {
                        unit.died(battle);
                    }
                } else {
                    // init units rotation
                    unit.getSprite().setRotation(deploymentBoundaries[0] == 0 ? 90 : -90);
                }
            }

        }

        mScene.sortChildren(true);

        pOnPopulateSceneCallback.onPopulateSceneFinished();

        // init camera position
        this.mCamera.setCenter(battle.getDeploymentBoundaries(battle.getMe())[0] * GameUtils.PIXEL_BY_TILE,
                tmxLayer.getHeight() / 2);

        // hide loading screen
        mGameGUI.hideLoadingScreen();

        if (battle.getPhase() == Battle.Phase.deployment) {
            startDeploymentPhase();
        } else {
            mGameGUI.hideDeploymentButton();
            startGame();
            startRenderLoop();
        }

        battle.setHasStarted(true);
    }

    private void startDeploymentPhase() {
        // add deployment fogs of war
        int[] deploymentBoundaries = battle.getDeploymentBoundaries(battle.getMe());
        if (deploymentBoundaries[0] == 0) {
            deploymentZone = new DeploymentZone(deploymentBoundaries[1] * GameUtils.PIXEL_BY_TILE, 0.0f, battle
                    .getMap().getWidth() * GameUtils.PIXEL_BY_TILE, battle.getMap().getHeight()
                    * GameUtils.PIXEL_BY_TILE, getVertexBufferObjectManager());
        } else {
            deploymentZone = new DeploymentZone(0.0f, 0.0f, deploymentBoundaries[0] * GameUtils.PIXEL_BY_TILE, battle
                    .getMap().getHeight() * GameUtils.PIXEL_BY_TILE, getVertexBufferObjectManager());
        }
        mScene.attachChild(deploymentZone);

        mGameGUI.displayBigLabel(getString(R.string.deployment_phase), R.color.white);

        startRenderLoop();
    }

    public void startGame() {
        // register game logic loop
        TimerHandler spriteTimerHandler = new TimerHandler(1.0f / GameUtils.GAME_LOOP_FREQUENCY, true,
                new ITimerCallback() {
                    Player winner;

                    @Override
                    public void onTimePassed(TimerHandler pTimerHandler) {
                        winner = battle.update();
                        if (winner != null) {
                            endGame(winner, false);
                        }

                        // update selected element order's crosshair
                        if (mInputManager.selectedElement == null) {
                            crosshair.setVisible(false);
                        } else if (mInputManager.selectedElement.getGameElement() instanceof Unit) {
                            Unit unit = (Unit) mInputManager.selectedElement.getGameElement();
                            Order o = unit.getOrder();
                            if (unit.getRank() == GameElement.Rank.ally && o != null) {
                                if (o instanceof FireOrder) {
                                    FireOrder f = (FireOrder) o;
                                    crosshair.setColor(Color.RED);
                                    crosshair.setPosition(f.getXDestination(), f.getYDestination());
                                    crosshair.setVisible(true);
                                } else if (o instanceof MoveOrder) {
                                    MoveOrder f = (MoveOrder) o;
                                    crosshair.setColor(Color.GREEN);
                                    crosshair.setPosition(f.getXDestination(), f.getYDestination());
                                    crosshair.setVisible(true);
                                } else {
                                    crosshair.setVisible(false);
                                }
                            } else {
                                crosshair.setVisible(false);
                            }
                        }

                        mScene.sortChildren(true);
                    }
                });
        mEngine.registerUpdateHandler(spriteTimerHandler);

        // show go label
        mGameGUI.displayBigLabel(getString(R.string.go), R.color.white);

        // hide deployment
        if (deploymentZone != null) {
            deploymentZone.setVisible(false);
        }
        battle.setPhase(Battle.Phase.combat);

        String atmoSound = GameUtils.ATMO_SOUNDS[(int) Math.round(Math.random() * (GameUtils.ATMO_SOUNDS.length - 1))];
        playSound(atmoSound, (float) (Math.random() * battle.getMap().getWidth() * GameUtils.PIXEL_BY_TILE),
                (float) (Math.random() * battle.getMap().getHeight() * GameUtils.PIXEL_BY_TILE));
    }

    private void startRenderLoop() {
        // register render loop
        mScene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(final float pSecondsElapsed) {
                battle.updateMoves();

                // update selected element info
                mGameGUI.updateSelectedElementLayout(mInputManager.selectedElement);
            }
        });
    }

    @Override
    public void onBackPressed() {
        pauseGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGameGUI != null) {
            mGameGUI.onPause();
        }
        if (mGameElementFactory != null) {
            mGameElementFactory.onPause();
        }
        if (mSoundEffectManager != null) {
            mSoundEffectManager.onPause();
        }
        if (mMustSaveGame) {
//            GameConverterHelper.saveGame(mDbHelper, battle);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    private void pauseGame() {
        mGameGUI.openGameMenu();
        mEngine.stop();
    }

    public void resumeGame() {
        mEngine.start();
    }

    public void addElementToScene(GameElement gameElement, boolean isMySquad) {
        // create sprite
        UnitSprite sprite = null;
        if (gameElement instanceof Soldier) {
            sprite = new SoldierSprite(gameElement, mInputManager, 0, 0, GraphicsFactory.mGfxMap.get(gameElement
                    .getSpriteName()), getVertexBufferObjectManager());
        } else if (gameElement instanceof Tank) {
            sprite = new TankSprite(gameElement, mInputManager, 0, 0, GraphicsFactory.mGfxMap.get(gameElement
                    .getSpriteName()), getVertexBufferObjectManager());
        } else if (gameElement instanceof Cannon) {
            sprite = new CannonSprite(gameElement, mInputManager, 0, 0, GraphicsFactory.mGfxMap.get(gameElement
                    .getSpriteName()), getVertexBufferObjectManager());
        }

        if (gameElement.getTilePosition() != null) {
            sprite.setPosition(gameElement.getTilePosition().getTileX(), gameElement.getTilePosition().getTileY());
        }
        gameElement.setSprite(sprite);
        gameElement.setRank(isMySquad ? GameElement.Rank.ally : GameElement.Rank.enemy);
        mScene.registerTouchArea(sprite);
        mScene.attachChild(sprite);
    }

    public void endGame(final Player winningPlayer, boolean instantly) {
        // stop engine
        mEngine.stop();

        if (instantly) {
            // show battle report without big label animation
            goToReport(winningPlayer == battle.getMe());
        } else {
            mGameGUI.displayVictoryLabel(winningPlayer == battle.getMe());
        }
    }

    public void goToReport(boolean victory) {
////        long battleId = GameConverterHelper.saveGame(mDbHelper, battle);
//        Intent i = new Intent(GameActivity.this, BattleReportActivity.class);
//        Bundle extras = new Bundle();
////        extras.putLong("game_id", battleId);
//        extras.putBoolean("victory", victory);
//        i.putExtras(extras);
//        startActivity(i);
//        finish();
    }

    @Override
    public void drawSprite(float x, float y, String spriteName, final int duration, final int size) {
        final Sprite sprite = new CenteredSprite(x, y, GraphicsFactory.mGfxMap.get(spriteName),
                getVertexBufferObjectManager());
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

    @Override
    public void drawAnimatedSprite(float x, float y, String spriteName, int frameDuration, float scale, int loopCount,
                                   final boolean removeAfter, int zIndex) {
        if (GraphicsFactory.mGfxMap.get(spriteName) == null) {
            return;
        }

        final AnimatedSprite sprite = new AnimatedSprite(0, 0, GraphicsFactory.mGfxMap.get(spriteName),
                getVertexBufferObjectManager());
        sprite.setPosition(x - sprite.getWidth() / 2.0f, y - sprite.getWidth() / 2.0f);
        sprite.setZIndex(zIndex);
        sprite.setScale(scale);
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
                }
            });
        }
        mScene.attachChild(sprite);
    }

    @Override
    public void playSound(String soundName, float x, float y) {
        mSoundEffectManager.playSound(soundName, x, y);
    }

    @Override
    public void onSignInFailed() {
    }

    @Override
    public void onSignInSucceeded() {
    }

}
