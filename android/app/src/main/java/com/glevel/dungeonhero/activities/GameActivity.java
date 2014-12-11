package com.glevel.dungeonhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.fragments.StoryFragment;
import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.data.HeroFactory;
import com.glevel.dungeonhero.game.ActionsDispatcher;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.MyBaseGameActivity;
import com.glevel.dungeonhero.game.base.interfaces.OnActionExecuted;
import com.glevel.dungeonhero.game.base.interfaces.OnDiscussionReplySelected;
import com.glevel.dungeonhero.game.graphics.SelectionCircle;
import com.glevel.dungeonhero.models.Chapter;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.dungeons.Directions;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Room;
import com.glevel.dungeonhero.models.dungeons.Tile;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.HeroicEffect;
import com.glevel.dungeonhero.models.effects.PoisonEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
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

    private static final String TAG = "GameActivity";

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
        if (extras != null && extras.getSerializable(Game.class.getName()) != null) {
            mGame = (Game) extras.getSerializable(Game.class.getName());
        } else {
            // TODO : used fot testing only
            mGame = new Game();
            mGame.setHero(HeroFactory.buildWizard());
            mGame.setBook(BookFactory.buildInitiationBook(1));
        }

        if (mGame.getDungeon() == null) {
            // start new dungeon
            Chapter chapter = mGame.getBook().getActiveChapter();

            // create dungeon
            chapter.createDungeon();
            mGame.setDungeon(chapter.getDungeon());

            mHero = mGame.getHero().clone();
            mDungeon = mGame.getDungeon();

            // show book intro story if needed
            if (mGame.getBook().getIntroText() > 0) {
                Bundle args = new Bundle();
                args.putInt(StoryFragment.ARGUMENT_STORY, mGame.getBook().getIntroText());
                ApplicationUtils.openDialogFragment(this, new StoryFragment(), args);
            }

            mHero.reset();
        } else {
            mDungeon = mGame.getDungeon();
        }
        mRoom = mDungeon.getCurrentRoom();
        Log.d(TAG, "current room " + mRoom);
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
        mCamera.setBounds(0, 0, mTmxTiledMap.getTileWidth() * mTmxTiledMap.getTileColumns(), mTmxTiledMap.getTileHeight() * mTmxTiledMap.getTileRows());
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

        if (mHero != null) {
            List<Unit> heroes = new ArrayList<>();
            heroes.add(mHero);
            mDungeon.moveIn(mTmxTiledMap, heroes);
        } else {
            mRoom.initRoom(mTmxTiledMap, null, 0);
        }

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

                List<GameElement> copy = new ArrayList<>(tile.getSubContent());
                for (GameElement subContent : copy) {
                    subContent.setTilePosition(tile);
                    addElementToScene(subContent);
                }
            }
        }

        pOnPopulateSceneCallback.onPopulateSceneFinished();

        mActionDispatcher = new ActionsDispatcher(this, mScene);

        mScene.sortChildren();

        startGame();
    }

    public void addElementToScene(GameElement gameElement) {
        gameElement.createSprite(getVertexBufferObjectManager());
        super.addElementToScene(gameElement.getSprite(), false);
    }

    public void removeElement(GameElement gameElement) {
        Log.d(TAG, "removing element");
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
            } else if (view.getTag(R.string.skill) != null) {
                mGUIManager.showSkillInfo((com.glevel.dungeonhero.models.skills.Skill) view.getTag(R.string.skill));
            } else if (view.getTag(R.string.use_skill) != null) {
                mActionDispatcher.setActivatedSkill((ActiveSkill) view.getTag(R.string.use_skill));
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
        mCamera.setCenter(mHero.getSprite().getX(), mHero.getSprite().getY());

        mGUIManager.hideLoadingScreen();
        mGUIManager.updateSkillButtons();

        nextTurn();
    }

    public void showChapterIntro() {
        if (mGame.getBook().getIntroText() > 0) {
            OnDiscussionReplySelected callback = null;
            if (mHero.getSkillPoints() > 0) {
                // if hero has some skill points left
                callback = new OnDiscussionReplySelected() {
                    @Override
                    public void onReplySelected(Pnj pnj, int next) {
                        mGUIManager.showNewLevelDialog();
                    }
                };
            }
            mGUIManager.showChapterIntro(callback);
        } else if (mHero.getSkillPoints() > 0) {
            mGUIManager.showNewLevelDialog();
        }
    }

    public void nextTurn() {
        Log.d(TAG, "NEXT TURN");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mHero.isDead()) {
                    Log.d(TAG, "hero is dead, game over !");
                    Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                    intent.putExtra(Game.class.getName(), mGame);
                    startActivity(intent);
                    finish();
                    return;
                }


                boolean isHeroic = false;
                if (mActiveCharacter != null) {
                    for (Effect effect : mActiveCharacter.getBuffs()) {
                        if (effect instanceof HeroicEffect) {
                            Log.d(TAG, "character is heroic");
                            isHeroic = true;
                        }
                    }
                }

                // TODO : camouflage

                if (!isHeroic) {
                    // next character
                    mActiveCharacter = mRoom.getQueue().get(0);
                    Log.d(TAG, "updating queue to next character = " + mActiveCharacter.getRank().name() + ", " + mActiveCharacter.getHp() + "hp");

                    mRoom.getQueue().add(mRoom.getQueue().get(0));
                    mRoom.getQueue().remove(0);
                    mGUIManager.updateQueue(mActiveCharacter, mRoom.getQueue(), mRoom.isSafe());
                }

                updateActionTiles();
                mActionDispatcher.hideElementInfo();
                mGUIManager.updateActiveHeroLayout(mHero);

                // handle current buffs
                boolean skipTurn = false;
                List<Effect> copy = new ArrayList<>(mActiveCharacter.getBuffs());
                for (Effect effect : copy) {
                    if (effect instanceof PoisonEffect) {
                        Log.d(TAG, "got poison effect");
                        mActionDispatcher.applyEffect(effect, mActiveCharacter.getTilePosition(), false);
                    } else if (effect instanceof StunEffect) {
                        Log.d(TAG, "got stun effect");
                        if (mActiveCharacter.testCharacteristic(effect.getTarget(), effect.getValue())) {
                            Log.d(TAG, "stun test was a success");
                            mActiveCharacter.getBuffs().remove(effect);
                        } else {
                            Log.d(TAG, "skip turn");
                            skipTurn = true;
                        }
                    }
                }

                mActiveCharacter.initNewTurn();

                if (skipTurn) {
                    nextTurn();
                } else if (mActiveCharacter instanceof Monster) {
                    Log.d(TAG, "AI turn");
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
                Log.d(TAG, "updating action tiles");
                mActionDispatcher.hideActionTiles();

                if (mRoom.isSafe()) {
                    mActionDispatcher.showActions();
                } else {
                    mActionDispatcher.showMovement();
                }
                Log.d(TAG, "updating action tiles is done");
                mActionDispatcher.setInputEnabled(!mActiveCharacter.isEnemy(mHero));
            }
        });
    }

    public void switchRoom(final Tile doorTile) {
        Log.d(TAG, "switching room");
        final Directions doorDirection = mRoom.getDoorDirection(doorTile);
        mEngine.stop();
        mDungeon.switchRoom(doorTile);
        mRoom = mDungeon.getCurrentRoom();
        try {
            Log.d(TAG, "resetting scene");
            mEngine.clearDrawHandlers();
            mEngine.clearUpdateHandlers();
            mScene.clearUpdateHandlers();
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
                                mEngine.start();
                                mInputManager.setEnabled(true);
                                animateRoomSwitch(doorDirection);
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

    private void animateRoomSwitch(final Directions doorDirection) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "start animation to direction " + doorDirection.name());
                int n = 30;
                mScene.setX(mScene.getX() + 30 * doorDirection.getDx());
                mScene.setY(mScene.getY() - 30 * doorDirection.getDy());
                while (n > 0) {
                    mScene.setX(mScene.getX() - doorDirection.getDx());
                    mScene.setY(mScene.getY() + doorDirection.getDy());
                    n--;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "animation is over");
                mScene.setX(0);
                mScene.setY(0);
            }
        });
    }

}
