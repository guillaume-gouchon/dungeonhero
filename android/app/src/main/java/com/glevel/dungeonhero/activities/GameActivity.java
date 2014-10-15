package com.glevel.dungeonhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.data.HeroFactory;
import com.glevel.dungeonhero.game.ActionsDispatcher;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.MyBaseGameActivity;
import com.glevel.dungeonhero.game.graphics.SelectionCircle;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Room;
import com.glevel.dungeonhero.models.dungeons.Tile;
import com.glevel.dungeonhero.models.items.Item;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.texture.TextureOptions;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends MyBaseGameActivity {

    public static final String EXTRA_GAME_ID = "game_id";
    private Dungeon mDungeon;
    private Hero mHero;
    private Room mRoom;
    private Unit mActiveCharacter;
    public SelectionCircle mSelectionCircle;
    public Entity mGroundLayer;
    public TMXTiledMap mTmxTiledMap;
    private ActionsDispatcher mActionDispatcher;

    @Override
    protected void initGameActivity() {
        Bundle extras = getIntent().getExtras();
        // TODO test
//        mGame = (Game) extras.getSerializable(Game.class.getName());
        mGame = new Game(HeroFactory.buildBerserker(), BookFactory.buildInitiationBook(1));
        mGame.setOnNewSprite(this);
        mGame.setOnNewSoundToPlay(this);

        mDungeon = mGame.getDungeon();
        mRoom = mDungeon.getCurrentRoom();
        mHero = mGame.getHero();

        // TODO
        mMustSaveGame = false;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_game;
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        super.onCreateScene(pOnCreateSceneCallback);

        // load tiled map for current room
        final TMXLoader tmxLoader = new TMXLoader(getAssets(), mEngine.getTextureManager(),
                TextureOptions.BILINEAR_PREMULTIPLYALPHA, getVertexBufferObjectManager(), null);
        mTmxTiledMap = tmxLoader.loadFromAsset("tmx/" + mRoom.getTmxName() + ".tmx");

        mRoom.initRoom(mTmxTiledMap, mHero, mDungeon);

        mTmxTiledMap.getTMXLayers().get(1).setZIndex(10);
        for (TMXLayer tmxLayer : mTmxTiledMap.getTMXLayers()) {
            mScene.attachChild(tmxLayer);
        }

        // make the camera not exceed the bounds of the TMXEntity
        mCamera.setBounds(0, 0, mTmxTiledMap.getTileHeight() * mTmxTiledMap.getTileRows(), mTmxTiledMap.getTileWidth() * mTmxTiledMap.getTileColumns());
        mCamera.setBoundsEnabled(true);

        pOnCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        mGroundLayer = new Entity();
        mGroundLayer.setZIndex(2);
        mScene.attachChild(mGroundLayer);

        mSelectionCircle = new SelectionCircle(getVertexBufferObjectManager());
        mScene.attachChild(mSelectionCircle);

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

        mActionDispatcher = new ActionsDispatcher(this, mScene);

        startGame();
    }

    public void addElementToScene(GameElement gameElement) {
        gameElement.createSprite(getVertexBufferObjectManager());
        super.addElementToScene(gameElement.getSprite(), false);
    }

    public void removeElement(GameElement gameElement) {
        gameElement.destroy();
        mRoom.removeElement(gameElement);
        mGUIManager.updateQueue(mActiveCharacter, mRoom.getQueue(), mRoom.isSafe());
        super.removeElement(gameElement.getSprite(), false);
    }

    @Override
    public void endGame() {
        super.endGame();
    }

    @Override
    public void onTouch(float x, float y) {
        mActionDispatcher.onTouch(x, y);
    }

    @Override
    public void onCancel(float x, float y) {
        mActionDispatcher.onCancel(x, y);
    }

    @Override
    public void onTap(float x, float y) {
        mActionDispatcher.onTap(x, y);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hero:
                mGUIManager.showHeroInfo(mHero);
                break;
            case R.id.bag:
                mGUIManager.showBag(mHero);
                break;
        }

        if (view.getTag(R.string.item) != null) {
            mGUIManager.showItemInfo(mHero, (Item) view.getTag(R.string.item));
        }
    }

    public Unit getActiveCharacter() {
        return mActiveCharacter;
    }

    public Room getRoom() {
        return mRoom;
    }

    public Hero getHero() {
        return mHero;
    }

    public void startGame() {
        // init camera position
        Tile tile = mHero.getTilePosition();
        mCamera.setCenter(mHero.getSprite().getX(), mHero.getSprite().getY());

        // hide loading screen
        mGUIManager.hideLoadingScreen();

        nextTurn();
    }

    public void nextTurn() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mHero.isDead()) {
                    startActivity(new Intent(GameActivity.this, GameOverActivity.class));
                    finish();
                    return;
                }
                mActiveCharacter = mRoom.getQueue().get(0);
                mRoom.getQueue().add(mRoom.getQueue().get(0));
                mRoom.getQueue().remove(0);
                mGUIManager.updateQueue(mActiveCharacter, mRoom.getQueue(), mRoom.isSafe());

                updateActionTiles();

                mActionDispatcher.hideElementInfo();

                mGUIManager.updateActiveHeroLayout(mHero);

                if (mActiveCharacter instanceof Monster) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mInputManager.setEnabled(false);
                            mActionDispatcher.attack(mHero.getTilePosition());
                        }
                    }, 500);

                }
            }
        });

    }

    private void updateActionTiles() {
        runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                mActionDispatcher.hideActionTiles();

                if (mRoom.isSafe()) {
                    mActionDispatcher.showActions();
                } else {
                    mActionDispatcher.showMovement();
                }
            }
        });
    }

}
