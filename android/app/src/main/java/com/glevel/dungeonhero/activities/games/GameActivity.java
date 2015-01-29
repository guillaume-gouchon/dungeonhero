package com.glevel.dungeonhero.activities.games;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.GameOverActivity;
import com.glevel.dungeonhero.activities.fragments.StoryFragment;
import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.data.characters.HeroFactory;
import com.glevel.dungeonhero.game.ActionsDispatcher;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.MyBaseGameActivity;
import com.glevel.dungeonhero.game.base.interfaces.OnActionExecuted;
import com.glevel.dungeonhero.game.graphics.SelectionCircle;
import com.glevel.dungeonhero.game.gui.items.ItemInfoInGame;
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
import com.glevel.dungeonhero.models.effects.CamouflageEffect;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.HeroicEffect;
import com.glevel.dungeonhero.models.effects.PoisonEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.consumables.Potion;
import com.glevel.dungeonhero.models.items.equipments.Equipment;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.pathfinding.MathUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends MyBaseGameActivity {

    private static final String TAG = "GameActivity";

    public SelectionCircle mSelectionCircle;
    public Entity mGroundLayer;
    public TMXTiledMap mTmxTiledMap;
    protected Dungeon mDungeon;
    protected Hero mHero;
    protected Room mRoom;
    protected Unit mActiveCharacter;
    protected ActionsDispatcher mActionDispatcher;
    private Tile mTurnStartTile;

    @Override
    protected void initGameActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(Game.class.getName()) != null) {
            mGame = (Game) extras.getSerializable(Game.class.getName());
        } else {
            // used fot testing only
            mGame = new Game();
            mGame.setHero(HeroFactory.buildDwarfWarrior());
            mGame.setBook(BookFactory.buildVanarkBook());
        }

        if (mGame.getDungeon() == null) {
            // new dungeon
            Chapter chapter = mGame.getBook().getActiveChapter();

            // create dungeon
            chapter.createDungeon();
            mGame.setDungeon(chapter.getDungeon());

            // copy hero object for reset dungeon after game-over
            mHero = (Hero) ApplicationUtils.deepCopy(mGame.getHero());

            mDungeon = mGame.getDungeon();

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
            addSpecialGameElements();
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

        mHero.updateSprite();
        mGUIManager.setData(mHero);

        showBookIntro();

        Log.d(TAG, "populate scene is finished");
        pOnPopulateSceneCallback.onPopulateSceneFinished();

        mActionDispatcher = new ActionsDispatcher(this, mScene);

        Log.d(TAG, "sort children by z-index");
        mScene.sortChildren();

        startGame();
    }

    protected void addSpecialGameElements() {
    }

    public void addElementToScene(GameElement gameElement) {
        Log.d(TAG, "add element to scene = " + gameElement.getIdentifier());
        gameElement.createSprite(getVertexBufferObjectManager());
        super.addElementToScene(gameElement.getSprite(), false);
    }

    public void removeElement(GameElement gameElement) {
        Log.d(TAG, "removing element");
        gameElement.destroy();
        mRoom.removeElement(gameElement);
        mGUIManager.updateQueue(mActiveCharacter, mRoom);
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
    protected int[] getMusicResource() {
        if (mSharedPrefs.getBoolean(GameConstants.GAME_PREFS_METAL_MUSIC, false)) {
            return new int[]{R.raw.adventure1_metal, R.raw.adventure2_metal, R.raw.adventure3_metal, R.raw.adventure4};
        } else {
            return new int[]{R.raw.adventure1, R.raw.adventure2, R.raw.adventure3, R.raw.adventure4};
        }
    }

    @Override
    public void endGame() {
        super.endGame();
    }

    @Override
    public void onMove(float x, float y) {
        mActionDispatcher.onMove(x, y);
    }

    @Override
    public void onCancel(float x, float y) {
        mActionDispatcher.onCancel(x, y);
    }

    @Override
    public void onPinchZoom(float zoomFactor) {
        mActionDispatcher.onPinchZoom(zoomFactor);
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
                Log.d(TAG, "Show item info");
                final Item item = (Item) view.getTag(R.string.item);
                mGUIManager.showItemInfo(item, new ItemInfoInGame.OnItemActionSelected() {
                    @Override
                    public void onActionExecuted(ItemInfoInGame.ItemActionsInGame action) {
                        switch (action) {
                            case UNEQUIP:
                                boolean success = mHero.removeEquipment((Equipment) item);
                                if (!success) {
                                    mActionDispatcher.getItemOrDropIt(item);
                                }
                                mGUIManager.updateBag(mHero);
                                break;

                            case EQUIP:
                                Item dropItem = mHero.equip((Equipment) item);
                                if (dropItem != null) {
                                    mActionDispatcher.getItemOrDropIt(item);
                                }
                                mGUIManager.updateBag(mHero);
                                // update reachable targets
                                updateActionTiles();
                                break;

                            case DROP:
                                mHero.drop(item);
                                mGUIManager.updateBag(mHero);
                                mActionDispatcher.dropItem(item);
                                break;

                            case DRINK:
                                mGUIManager.hideBag();
                                drinkPotion((Potion) item);
                                updateActionTiles();
                                break;
                        }
                    }
                });
            } else if (view.getTag(R.string.show_skill) != null) {
                if (mActionDispatcher.getActivatedSkill() == view.getTag(R.string.show_skill)) {
                    Log.d(TAG, "Cancel skill");
                    mActionDispatcher.setActivatedSkill((ActiveSkill) view.getTag(R.string.show_skill));
                } else {
                    Log.d(TAG, "Show skill");
                    mGUIManager.showUseSkillInfo((Skill) view.getTag(R.string.show_skill));
                }
            } else if (view.getTag(R.string.use_skill) != null) {
                Log.d(TAG, "Use skill");
                mActionDispatcher.setActivatedSkill((ActiveSkill) view.getTag(R.string.use_skill));
            }
        }

        if (view.getTag(R.string.skill) != null) {
            Log.d(TAG, "Show improve skill dialog");
            mGUIManager.showImproveSkillDialog((com.glevel.dungeonhero.models.skills.Skill) view.getTag(R.string.skill));
        }

        if (view.getId() == R.id.map) {
            mGUIManager.showDungeonMap();
        }
    }

    public void drinkPotion(Potion potion) {
        Log.d(TAG, "Drink potion");
        mHero.use(potion);
        playSound("magic", false);
        mActionDispatcher.applyEffect(potion.getEffect(), mHero.getTilePosition(), false);
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

        // auto-talk
        for (GameElement element : mRoom.getObjects()) {
            if (element instanceof Pnj && ((Pnj) element).isAutoTalk() && ((Pnj) element).getDiscussions().size() > 0 && !((Pnj) element).getDiscussions().get(0).isPermanent()) {
                mActionDispatcher.talkTo((Pnj) element);
                return;
            }
        }

        nextTurn();
    }

    public void showBookIntro() {
        // show book introduction if needed
        if (mGame.getBook().getIntroText(getResources()) > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.rootLayout).setVisibility(View.INVISIBLE);
                }
            });

            Bundle args = new Bundle();
            args.putInt(StoryFragment.ARGUMENT_STORY, mGame.getBook().getIntroText(getResources()));
            ApplicationUtils.openDialogFragment(this, new StoryFragment(), args);
            mGame.getBook().read();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showChapterIntro();
                }
            });
        }
    }

    public void showChapterIntro() {
        Log.d(TAG, "show chapter intro");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.rootLayout).setVisibility(View.VISIBLE);
            }
        });
        OnActionExecuted callback = new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                if (mGame.getBook().getActiveChapter().getIntroText(getResources()) > 0) {
                    mGUIManager.showChapterIntro(null);
                    mGame.getBook().getActiveChapter().read();
                }
            }
        };

        // show first new level dialog if needed
        if (mHero.getSkillPoints() > 0) {
            mGUIManager.showNewLevelDialog(callback);
        } else {
            callback.onActionDone(true);
        }
    }

    protected void gameover() {
        Log.d(TAG, "hero is dead, game over !");
        mGame.getBook().updateScore(-1);
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra(Game.class.getName(), mGame);
        startActivity(intent);
        finish();
    }

    public void nextTurn() {
        Log.d(TAG, "NEXT TURN");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mHero.isDead()) {
                    gameover();
                    return;
                }

                // add new enemies / remove characters
                for (GameElement element : mRoom.getObjects()) {
                    if (element instanceof Unit && (element.getRank() == Ranks.ENEMY || element.getRank() == Ranks.ALLY)
                            && !mRoom.getQueue().contains(element)) {
                        mRoom.getQueue().add(0, (Unit) element);
                        mRoom.checkSafe();
                    } else if (element instanceof Unit && element.getTilePosition() == null) {
                        removeElement(element);
                        break;
                    }
                }

                boolean isHeroic = false, isHidden = false;
                if (mActiveCharacter != null) {
                    List<Effect> copy = new ArrayList<>(mActiveCharacter.getBuffs());
                    for (Effect effect : copy) {
                        if (effect instanceof HeroicEffect) {
                            Log.d(TAG, "character is heroic");
                            isHeroic = true;
                            // animate heroic
                            if (effect.getSpriteName() != null) {
                                drawAnimatedSprite(mActiveCharacter.getTilePosition().getTileX(), mActiveCharacter.getTilePosition().getTileY(), effect.getSpriteName(), 50, GameConstants.ANIMATED_SPRITE_ALPHA, 1.0f, 0, true, 100, null);
                            }
                        } else if (effect instanceof CamouflageEffect) {
                            Log.d(TAG, "character is invisible");
                            isHidden = true;
                            // animate invisible
                            if (effect.getSpriteName() != null) {
                                drawAnimatedSprite(mActiveCharacter.getTilePosition().getTileX(), mActiveCharacter.getTilePosition().getTileY(), effect.getSpriteName(), 50, GameConstants.ANIMATED_SPRITE_ALPHA, 1.0f, 0, true, 100, null);
                            }
                            // test if character is still invisible
                            for (GameElement element : mRoom.getObjects()) {
                                if (element instanceof Monster) {
                                    // test if the monster sees invisible character depending on the distance
                                    double distance = MathUtils.calcManhattanDistance(element.getTilePosition(), mActiveCharacter.getTilePosition());
                                    Log.d(TAG, "distance to invisible character = " + distance);
                                    if (distance == 1 || ((Unit) element).testCharacteristic(Characteristics.SPIRIT, (int) (effect.getValue() + distance))) {
                                        // character is not hidden anymore
                                        isHidden = false;
                                        mActiveCharacter.getBuffs().remove(effect);
                                        // animate end of invisibility
                                        if (effect.getSpriteName() != null) {
                                            drawAnimatedSprite(element.getTilePosition().getTileX(), element.getTilePosition().getTileY(), effect.getSpriteName(), 50, GameConstants.ANIMATED_SPRITE_ALPHA, 1.0f, 0, true, 100, null);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if (!isHeroic && !isHidden) {
                    // next character
                    mActiveCharacter = mRoom.getQueue().get(0);
                    Log.d(TAG, "updating queue to next character = " + mActiveCharacter.getRank().name() + ", " + mActiveCharacter.getHp() + "hp");

                    mRoom.getQueue().add(mRoom.getQueue().get(0));
                    mRoom.getQueue().remove(0);
                    mGUIManager.updateQueue(mActiveCharacter, mRoom);
                }

                // handle current buffs
                boolean skipTurn = false;
                List<Effect> copy = new ArrayList<>(mActiveCharacter.getBuffs());
                for (Effect effect : copy) {
                    if (mActiveCharacter.isDead()) break;

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
                            drawAnimatedText(mActiveCharacter.getSprite().getX() + GameConstants.PIXEL_BY_TILE / 3, mActiveCharacter.getSprite().getY() - 2 * GameConstants.PIXEL_BY_TILE / 3, getString(R.string.sleep_effect), new Color(0.0f, 1.0f, 0.0f), 0.4f, 50, -0.15f);
                        }
                    }
                }

                if (mActiveCharacter.isDead()) {
                    if (mActiveCharacter == mHero) {
                        gameover();
                    } else {
                        nextTurn();
                    }
                    return;
                }

                mTurnStartTile = mActiveCharacter.getTilePosition();
                updateActionTiles();
                mActionDispatcher.hideElementInfo();
                mGUIManager.updateActiveHeroLayout();

                mActiveCharacter.initNewTurn();

                if (skipTurn) {
                    nextTurn();
                } else if (mActiveCharacter.isEnemy(mHero) || mActiveCharacter.getRank() == Ranks.ALLY) {
                    Log.d(TAG, "AI turn");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // find target
                                    Unit target = null;

                                    // prioritize adjacent tiles
                                    Set<Tile> adjacentTiles = MathUtils.getAdjacentNodes(mRoom.getTiles(), mActiveCharacter.getTilePosition(), 1, false, null);
                                    for (Tile tile : adjacentTiles) {
                                        if (tile.getContent() instanceof Unit && tile.getContent().isEnemy(mActiveCharacter)) {
                                            target = (Unit) tile.getContent();
                                            break;
                                        }
                                    }

                                    // then, search in whole room
                                    if (target == null) {
                                        Collections.shuffle(mRoom.getObjects());
                                        for (GameElement gameElement : mRoom.getObjects()) {
                                            if (gameElement instanceof Unit && gameElement.isEnemy(mActiveCharacter)) {
                                                target = (Unit) gameElement;
                                                break;
                                            }
                                        }
                                    }

                                    if (target == null) {
                                        nextTurn();
                                    } else {
                                        ActiveSkill availableSkill = mActiveCharacter.getAvailableSkill();
                                        if (availableSkill != null && Math.random() < 0.7) {
                                            mActionDispatcher.setActivatedSkill(availableSkill);
                                            if (!availableSkill.isPersonal()) {
                                                mActionDispatcher.useSkill(target.getTilePosition());
                                            }
                                        } else {
                                            mActionDispatcher.attack(target.getTilePosition());
                                        }
                                    }
                                }
                            });
                        }
                    }, 500);
                } else if (mActiveCharacter.getRank() == Ranks.ME) {
                    mInputManager.setEnabled(true);
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
                    mActionDispatcher.showSpecialActions();
                } else {
                    mActionDispatcher.showAllActions(mTurnStartTile);
                }
                Log.d(TAG, "updating action tiles is done");
                mActionDispatcher.setInputEnabled(!mActiveCharacter.isEnemy(mHero));
            }
        });
    }

    public void switchRoom(final Tile doorTile) {
        Log.d(TAG, "switching room");
        final Directions doorDirection = mRoom.getDoorDirection(doorTile);

        List<Unit> heroes = new ArrayList<>();
        heroes.add(mHero);
        mDungeon.switchRoom(doorTile, heroes);

        mRoom = mDungeon.getCurrentRoom();

        try {
            Log.d(TAG, "resetting scene");
            mEngine.clearDrawHandlers();
            mEngine.clearUpdateHandlers();
            mScene.clearUpdateHandlers();
            mEngine.stop();
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
                                updateLightAtmoshphere();
                                animateRoomSwitch(doorDirection);
                                mScene.sortChildren(true);
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

    public void updateLightAtmoshphere() {
        setForeground(mRoom.hasLight() ? android.R.color.transparent : R.color.red);
    }

    public void setForeground(final int color) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.foreground_view).setBackgroundColor(color);
            }
        });
    }

}
