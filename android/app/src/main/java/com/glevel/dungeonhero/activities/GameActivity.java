package com.glevel.dungeonhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.fragments.StoryFragment;
import com.glevel.dungeonhero.game.ActionsDispatcher;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.MyBaseGameActivity;
import com.glevel.dungeonhero.game.base.interfaces.OnActionExecuted;
import com.glevel.dungeonhero.game.graphics.SelectionCircle;
import com.glevel.dungeonhero.models.Chapter;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Room;
import com.glevel.dungeonhero.models.dungeons.Tile;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.utils.ApplicationUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.texture.TextureOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends MyBaseGameActivity {

    public SelectionCircle mSelectionCircle;
    public Entity mGroundLayer;
    public TMXTiledMap mTmxTiledMap;
    private Dungeon mDungeon;
    private Hero mHero;
    private Room mRoom;
    private Unit mActiveCharacter;
    private ActionsDispatcher mActionDispatcher;

    @Override
    protected void initGameActivity() {
        Bundle extras = getIntent().getExtras();
        mGame = (Game) extras.getSerializable(Game.class.getName());
//        mGame = new Game(HeroFactory.buildBerserker(), BookFactory.buildInitiationBook(1));
        mGame.setOnNewSprite(this);
        mGame.setOnNewSoundToPlay(this);

        if (mGame.getDungeon() == null) {
            // start new dungeon
            Chapter chapter = mGame.getChapter();

            // create dungeon
            chapter.createDungeon();
            mGame.setDungeon(chapter.getDungeon());

            mHero = mGame.getHero().clone();
            mDungeon = mGame.getDungeon();


            // show intro story if needed
            if (mDungeon.getIntroText() > 0) {
                Bundle args = new Bundle();
                args.putInt(StoryFragment.ARGUMENT_STORY, mDungeon.getIntroText());
                ApplicationUtils.openDialogFragment(this, new StoryFragment(), args);
            }
        } else {
            mDungeon = mGame.getDungeon();
        }
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
        final TMXLoader tmxLoader = new TMXLoader(getAssets(), mEngine.getTextureManager(),
                TextureOptions.BILINEAR_PREMULTIPLYALPHA, getVertexBufferObjectManager(), null);
        mTmxTiledMap = tmxLoader.loadFromAsset("tmx/" + mRoom.getTmxName() + ".tmx");

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

        List<Unit> heroes = new ArrayList<Unit>();
        if (mHero != null) {
            heroes.add(mHero);
        }
        mDungeon.moveIn(mTmxTiledMap, heroes);

        // add elements to scene
        GameElement gameElement;
        for (Tile[] hTiles : mRoom.getTiles()) {
            for (Tile tile : hTiles) {
                gameElement = tile.getContent();
                if (gameElement != null) {
                    gameElement.setTilePosition(tile);
                    addElementToScene(gameElement);
                    if (mHero == null && gameElement.getRank() == Ranks.ME) {
                        mHero = (Hero) gameElement;
                    }
                }

                List<GameElement> copy = new ArrayList<GameElement>(tile.getSubContent());
                for (GameElement subContent : copy) {
                    subContent.setTilePosition(tile);
                    addElementToScene(subContent);
                }
            }
        }

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
    protected void onPause() {
        if (mActiveCharacter != null && mActiveCharacter.isEnemy(mHero)) {
            mRoom.getQueue().remove(mActiveCharacter);
            mRoom.getQueue().add(0, mActiveCharacter);
        }
        mGame.setDungeon(mDungeon);
        super.onPause();
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
        if (mInputManager.ismIsEnabled()) {
            switch (view.getId()) {
                case R.id.hero:
                    mGUIManager.showHeroInfo(mHero);
                    break;
                case R.id.bag:
                    mGUIManager.showBag(mHero);
                    break;
            }

            if (view.getTag(R.string.item) != null) {
                final Item item = (Item) view.getTag(R.string.item);
                mGUIManager.showItemInfo(mHero, item, new OnActionExecuted() {
                    @Override
                    public void onActionDone(boolean success) {
                        mActionDispatcher.dropItem(item);
                    }
                });
            }
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
                    Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                    intent.putExtra(Game.class.getName(), mGame);
                    startActivity(intent);
                    finish();
                    return;
                }
                mActiveCharacter = mRoom.getQueue().get(0);
                mActiveCharacter.initNewTurn();
                mRoom.getQueue().add(mRoom.getQueue().get(0));
                mRoom.getQueue().remove(0);
                mGUIManager.updateQueue(mActiveCharacter, mRoom.getQueue(), mRoom.isSafe());

                updateActionTiles();

                mActionDispatcher.hideElementInfo();

                mGUIManager.updateActiveHeroLayout(mHero);

                mInputManager.setEnabled(!mActiveCharacter.isEnemy(mHero));

                if (mActiveCharacter instanceof Monster) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
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

    public void switchRoom(final Tile doorTile) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDungeon.switchRoom(doorTile);
                mRoom = mDungeon.getCurrentRoom();
                try {
                    mGroundLayer.detachChildren();
                    mScene.reset();
                    mScene.clearTouchAreas();
                    mScene.detachChildren();
                    onCreateScene(new OnCreateSceneCallback() {
                        @Override
                        public void onCreateSceneFinished(Scene pScene) {
                            try {
                                onPopulateScene(mScene, new OnPopulateSceneCallback() {
                                    @Override
                                    public void onPopulateSceneFinished() {
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
