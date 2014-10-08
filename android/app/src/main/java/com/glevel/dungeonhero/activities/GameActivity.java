package com.glevel.dungeonhero.activities;

import android.os.Bundle;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.data.HeroFactory;
import com.glevel.dungeonhero.game.base.CustomGameActivity;
import com.glevel.dungeonhero.game.graphics.GameElementSprite;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.dungeons.Directions;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Room;
import com.glevel.dungeonhero.models.dungeons.Tile;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.texture.TextureOptions;

public class GameActivity extends CustomGameActivity {

    public static final String EXTRA_GAME_ID = "game_id";

    private Dungeon mDungeon;
    private Room mRoom;

    @Override
    protected void initGameActivity() {
        Bundle extras = getIntent().getExtras();
        // TODO test
//        mGame = (Game) extras.getSerializable(Game.class.getName());
        mGame = new Game(HeroFactory.buildWarrior(), BookFactory.buildInitiationBook(1));
        mGame.setOnNewSprite(this);
        mGame.setOnNewSoundToPlay(this);

        mDungeon = mGame.getDungeon();
        mRoom = mDungeon.getCurrentRoom();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_game;
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        super.onCreateScene(pOnCreateSceneCallback);

        // load tiled map for current room
        final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), mEngine.getTextureManager(),
                TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), null);
        TMXTiledMap tmXTiledMap = tmxLoader.loadFromAsset("tmx/" + mRoom.getTmxName() + ".tmx");
        mRoom.initRoom(tmXTiledMap);

        // TODO : place hero
        mRoom.getDoors().get(Directions.NORTH).setContent(mGame.getHero());

        for (TMXLayer tmxLayer : tmXTiledMap.getTMXLayers()) {
            mScene.attachChild(tmxLayer);
        }

        // make the camera not exceed the bounds of the TMXEntity
        mCamera.setBounds(0, 0, tmXTiledMap.getTileHeight() * tmXTiledMap.getTileRows(), tmXTiledMap.getTileWidth() * tmXTiledMap.getTileColumns());
        mCamera.setBoundsEnabled(true);

        pOnCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        // add elements to scene
        GameElement gameElement;
        for (Tile[] hTiles : mRoom.getTiles()) {
            for (Tile tile : hTiles) {
                gameElement = tile.getContent();
                if (gameElement != null) {
                    gameElement.setTilePosition(tile);
                    addElementToScene(gameElement);
                }
            }
        }

        mScene.sortChildren(true);

        pOnPopulateSceneCallback.onPopulateSceneFinished();

        startGame();
    }

    public void startGame() {
        // init camera position
        Tile tile = mGame.getHero().getTilePosition();
        this.mCamera.setCenter(tile.getX(), tile.getY());

        // hide loading screen
        mGUIManager.hideLoadingScreen();

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

        startRenderLoop();
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
//                mGUIManager.updateSelectedElementLayout(mInputManager.selectedElement);
            }
        });
    }

    public void addElementToScene(GameElement gameElement) {
        // create sprite
        GameElementSprite sprite = null;
        if (gameElement instanceof Unit) {
            sprite = new UnitSprite(gameElement, getVertexBufferObjectManager());
        }

        addElementToScene(sprite, true);
    }

    public void endGame() {
        super.endGame();
    }

}
