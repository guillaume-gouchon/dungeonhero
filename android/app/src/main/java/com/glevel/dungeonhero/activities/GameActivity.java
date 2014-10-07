package com.glevel.dungeonhero.activities;

import android.os.Bundle;

import com.glevel.dungeonhero.MyDatabase;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.game.GraphicsManager;
import com.glevel.dungeonhero.game.base.CustomGameActivity;
import com.glevel.dungeonhero.game.graphics.CannonSprite;
import com.glevel.dungeonhero.game.graphics.SoldierSprite;
import com.glevel.dungeonhero.game.graphics.TankSprite;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.game.models.Player;
import com.glevel.dungeonhero.game.models.map.Tile;
import com.glevel.dungeonhero.game.models.units.Cannon;
import com.glevel.dungeonhero.game.models.units.Soldier;
import com.glevel.dungeonhero.game.models.units.Tank;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.models.Game;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.debug.Debug;

public class GameActivity extends CustomGameActivity {

    public static final String EXTRA_GAME_ID = "game_id";

    private TMXLayer tmxLayer;
    private TMXTiledMap mTMXTiledMap;

    @Override
    protected void initGameActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // load game or new game
            long gameId = extras.getLong("game_id", 0);
            mGame = (Game) mDbHelper.getRepository(MyDatabase.Repositories.GAME.name()).getById(gameId);
        }

        mGame.setOnNewSprite(this);
        mGame.setOnNewSoundToPlay(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_game;
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        super.onCreateScene(pOnCreateSceneCallback);

        // prepare tile map
        try {
            final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), mEngine.getTextureManager(),
                    TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), null);
            mTMXTiledMap = tmxLoader.loadFromAsset("tmx/" + mGame.getTileMapName());
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
        // init mBattle's map tiles
        Tile[][] lstTiles = new Tile[tmxLayer.getTileRows()][tmxLayer.getTileColumns()];
        for (TMXTile[] tt : tmxLayer.getTMXTiles()) {
            for (TMXTile t : tt) {
                lstTiles[t.getTileRow()][t.getTileColumn()] = new Tile(t, mTMXTiledMap);
            }
        }
        mGame.getMap().setTiles(lstTiles);
        mGame.getMap().setTmxLayer(tmxLayer);

        // add armies to scene
        for (Player player : mBattle.getPlayers()) {
            for (Unit unit : player.getUnits()) {
                // load position and rotation
                float currentX = unit.getCurrentX();
                float currentY = unit.getCurrentY();
                float currentRotation = unit.getCurrentRotation();
                Tile t = MapLogic.getTileAtCoordinates(mBattle.getMap(), currentX, currentY);
                unit.setTilePosition(mBattle, t);
                unit.getSprite().setX(currentX);
                unit.getSprite().setY(currentY);
                unit.getSprite().setRotation(currentRotation);
                if (unit.isDead()) {
                    unit.died(mBattle);
                }
            }
        }

        mScene.sortChildren(true);

        pOnPopulateSceneCallback.onPopulateSceneFinished();

        // init camera position
        this.mCamera.setCenter(mBattle.getDeploymentBoundaries(mBattle.getMe())[0] * GameConstants.PIXEL_BY_TILE,
                tmxLayer.getHeight() / 2);

        // hide loading screen
        mGUIManager.hideLoadingScreen();

        startGame();
        startRenderLoop();
    }

    public void startGame() {
//        // register game logic loop
//        TimerHandler spriteTimerHandler = new TimerHandler(1.0f / GameConstants.GAME_LOOP_FREQUENCY, true,
//                new ITimerCallback() {
//                    Player winner;
//
//                    @Override
//                    public void onTimePassed(TimerHandler pTimerHandler) {
//                        winner = mBattle.update();
//                        if (winner != null) {
//                            endGame(winner, false);
//                        }
//
//                        // update selected element order's crosshair
//                        if (mInputManager.selectedElement == null) {
//                            crosshair.setVisible(false);
//                        } else if (mInputManager.selectedElement.getGameElement() instanceof Unit) {
//                            Unit unit = (Unit) mInputManager.selectedElement.getGameElement();
//                            Order o = unit.getOrder();
//                            if (unit.getRank() == GameElement.Rank.ally && o != null) {
//                                if (o instanceof FireOrder) {
//                                    FireOrder f = (FireOrder) o;
//                                    crosshair.setColor(Color.RED);
//                                    crosshair.setPosition(f.getXDestination(), f.getYDestination());
//                                    crosshair.setVisible(true);
//                                } else if (o instanceof MoveOrder) {
//                                    MoveOrder f = (MoveOrder) o;
//                                    crosshair.setColor(Color.GREEN);
//                                    crosshair.setPosition(f.getXDestination(), f.getYDestination());
//                                    crosshair.setVisible(true);
//                                } else {
//                                    crosshair.setVisible(false);
//                                }
//                            } else {
//                                crosshair.setVisible(false);
//                            }
//                        }
//
//                        mScene.sortChildren(true);
//                    }
//                });
//        mEngine.registerUpdateHandler(spriteTimerHandler);
//
//        // show go label
//        mGameGUI.displayBigLabel(getString(R.string.go), R.color.white);
//
//        String atmoSound = GameConstants.ATMO_SOUNDS[(int) Math.round(Math.random() * (GameConstants.ATMO_SOUNDS.length - 1))];
//        playGeolocalizedSound(atmoSound, (float) (Math.random() * mBattle.getMap().getWidth() * GameConstants.PIXEL_BY_TILE),
//                (float) (Math.random() * mBattle.getMap().getHeight() * GameConstants.PIXEL_BY_TILE));
    }

    private void startRenderLoop() {
        // register render loop
        mScene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(final float pSecondsElapsed) {
                // update selected element info
                mGUIManager.updateSelectedElementLayout(mInputManager.selectedElement);
            }
        });
    }

    public void addElementToScene(GameElement gameElement, boolean isMySquad) {
        // create sprite
        UnitSprite sprite = null;
        if (gameElement instanceof Soldier) {
            sprite = new SoldierSprite(gameElement, mInputManager, 0, 0, GraphicsManager.sGfxMap.get(gameElement
                    .getSpriteName()), getVertexBufferObjectManager());
        } else if (gameElement instanceof Tank) {
            sprite = new TankSprite(gameElement, mInputManager, 0, 0, GraphicsManager.sGfxMap.get(gameElement
                    .getSpriteName()), getVertexBufferObjectManager());
        } else if (gameElement instanceof Cannon) {
            sprite = new CannonSprite(gameElement, mInputManager, 0, 0, GraphicsManager.sGfxMap.get(gameElement
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

    public void endGame() {
        super.endGame();
    }

}
