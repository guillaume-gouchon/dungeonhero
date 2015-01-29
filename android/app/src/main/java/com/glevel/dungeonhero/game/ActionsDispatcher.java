package com.glevel.dungeonhero.game;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.games.GameActivity;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.InputManager;
import com.glevel.dungeonhero.game.base.interfaces.OnActionExecuted;
import com.glevel.dungeonhero.game.base.interfaces.OnDiscussionReplySelected;
import com.glevel.dungeonhero.game.base.interfaces.UserActionListener;
import com.glevel.dungeonhero.game.graphics.ActionTile;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.models.Actions;
import com.glevel.dungeonhero.models.FightResult;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.callbacks.StopDiscussionCallback;
import com.glevel.dungeonhero.models.dungeons.Directions;
import com.glevel.dungeonhero.models.dungeons.GroundTypes;
import com.glevel.dungeonhero.models.dungeons.Tile;
import com.glevel.dungeonhero.models.dungeons.decorations.ItemOnGround;
import com.glevel.dungeonhero.models.dungeons.decorations.Light;
import com.glevel.dungeonhero.models.dungeons.decorations.Searchable;
import com.glevel.dungeonhero.models.dungeons.decorations.Stairs;
import com.glevel.dungeonhero.models.effects.BuffEffect;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.PoisonEffect;
import com.glevel.dungeonhero.models.effects.RecoveryEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.pathfinding.AStar;
import com.glevel.dungeonhero.utils.pathfinding.MathUtils;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guillaume on 10/14/14.
 */
public class ActionsDispatcher implements UserActionListener {

    private static final String TAG = "ActionDispatcher";

    private final GameActivity mGameActivity;
    private final GUIManager mGUIManager;
    private final InputManager mInputManager;
    private Scene mScene;
    private GameElement mSelectedElement;
    private Tile mSelectedTile = null;
    private boolean isMoving = false;
    private Tile mNextTile = null;
    private boolean inputDisabled = false;
    private TimerHandler animationHandler;
    private ActiveSkill activatedSkill = null;

    public ActionsDispatcher(GameActivity gameActivity, Scene scene) {
        mGameActivity = gameActivity;
        mInputManager = mGameActivity.getInputManager();
        mGUIManager = mGameActivity.getGUIManager();
        mScene = scene;
    }

    @Override
    public void onMove(float x, float y) {
        Tile tile = getTileAtCoordinates(x, y);
        if (tile != null && tile.getAction() != null && mGameActivity.getActiveCharacter().getRank() == Ranks.ME) {
            selectTile(tile);
        }
    }

    @Override
    public void onTap(float x, float y) {
        Log.d(TAG, "onTap received, disabled ? " + inputDisabled);
        if (inputDisabled) return;

        Tile tile = getTileAtCoordinates(x, y);
        if (tile != null && mGameActivity.getActiveCharacter().getRank() == Ranks.ME) {
            if (activatedSkill != null) {
                useSkill(tile);
            } else if (tile.getSubContent().size() > 0 && tile.getSubContent().get(0) != mSelectedElement && (tile.getContent() == null || tile.getContent().getRank() != Ranks.ENEMY)) {
                showElementInfo(tile.getSubContent().get(0));
            } else if (tile.getContent() != null && tile.getContent() != mSelectedElement && tile.getContent().getRank() != Ranks.ME) {
                showElementInfo(tile.getContent());
            } else if (tile.getContent() != null && tile.getContent().getRank() == Ranks.ME && mSelectedTile == tile && tile.getSubContent().size() == 0) {
                // end movement
                mGameActivity.nextTurn();
            } else if (!isMoving && tile.getAction() != null && tile.getAction() != Actions.NONE) {
                executeAction(tile);
            } else {
                hideElementInfo();
                if (mGameActivity.getRoom().isSafe() && mGameActivity.getActiveCharacter().getRank() == Ranks.ME && mGameActivity.getActiveCharacter().getTilePosition() == tile && tile.getSubContent().size() > 0
                        && tile.getSubContent().get(0) instanceof Stairs) {
                    enterStairs();
                } else if (mGameActivity.getRoom().isSafe()) {
                    if (mGameActivity.getActiveCharacter().canMoveIn(tile)) {
                        if (isMoving) {
                            mNextTile = tile;
                            mGameActivity.runOnUpdateThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideActionTiles();
                                    showSpecialActions();
                                    selectTile(mNextTile);
                                    addActionToTile(Actions.NONE, mNextTile);
                                }
                            });
                        } else {
                            selectTile(tile);
                            addActionToTile(Actions.NONE, tile);
                            move(tile);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCancel(float x, float y) {
        selectTile(null);
    }

    @Override
    public void onPinchZoom(float zoomFactor) {
    }

    private void executeAction(Tile tile) {
        hideElementInfo();
        switch (tile.getAction()) {
            case MOVE:
                move(tile);
                break;
            case ATTACK:
                attack(tile);
                break;
            case TALK:
                talk(tile);
                break;
            case SEARCH:
                search(tile);
                break;
            case LIGHT:
                switchLight(tile);
                break;
        }
    }

    private void switchLight(final Tile tile) {
        goCloserTo(tile, new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                if (success) {
                    Light light = (Light) tile.getContent();
                    light.switchLight();
                    mGameActivity.updateLightAtmoshphere();
                }
                isMoving = false;
                mGameActivity.nextTurn();
            }
        });
    }

    private void move(Tile tile) {
        Log.d(TAG, "move to " + tile.getX() + "," + tile.getY());
        if (!mGameActivity.getRoom().isSafe()) {
            setInputEnabled(false);
        }
        List<Tile> path = new AStar<Tile>().search(mGameActivity.getRoom().getTiles(), mGameActivity.getActiveCharacter().getTilePosition(), tile, false, mGameActivity.getActiveCharacter());
        if (path != null) {
            isMoving = true;
            animateMove(path, new OnActionExecuted() {
                private boolean done = false;

                @Override
                public void onActionDone(boolean success) {
                    setInputEnabled(true);
                    isMoving = false;
                    selectTile(null);
                    if (!done) {
                        done = true;
                        if (mGameActivity.getActiveCharacter().getRank() == Ranks.ME && mGameActivity.getActiveCharacter().getTilePosition().getGround() == GroundTypes.DOOR) {
                            // enters a door tile
                            mGameActivity.switchRoom(mGameActivity.getActiveCharacter().getTilePosition());
                        } else if (mGameActivity.getRoom().isSafe() && mGameActivity.getActiveCharacter().getRank() == Ranks.ME && mGameActivity.getActiveCharacter().getTilePosition().getSubContent().size() > 0
                                && mGameActivity.getActiveCharacter().getTilePosition().getSubContent().get(0) instanceof Stairs) {
                            enterStairs();
                        } else if (mGameActivity.getRoom().isSafe()) {
                            hideActionTiles();
                            showSpecialActions();
                        }
                    }
                }
            });
        } else {
            Log.d(TAG, "path is null");
            hideActionTiles();
            if (mGameActivity.getActiveCharacter().getRank() == Ranks.ME) {
                setInputEnabled(true);
            }
        }
    }

    private void enterStairs() {
        hideActionTiles();
        showSpecialActions();

        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (((Stairs) mGameActivity.getActiveCharacter().getTilePosition().getSubContent().get(0)).isDownStairs()) {
                    mGUIManager.showFinishQuestConfirmDialog();
                } else {
                    mGUIManager.showLeaveQuestConfirmDialog();
                }
            }
        });
    }

    public void attack(final Tile tile) {
        Log.d(TAG, "attack");
        setInputEnabled(false);

        OnActionExecuted callback = new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                isMoving = false;
                if (success && (mGameActivity.getActiveCharacter().isRangeAttack() || mGameActivity.getActiveCharacter().isNextTo(tile))) {
                    final Unit target = (Unit) tile.getContent();
                    FightResult fightResult = mGameActivity.getActiveCharacter().attack(target);
                    animateFight(mGameActivity.getActiveCharacter(), target, fightResult, new OnActionExecuted() {
                        @Override
                        public void onActionDone(boolean success) {
                            if (target.isDead()) {
                                animateDeath(target, new OnActionExecuted() {
                                    @Override
                                    public void onActionDone(boolean success) {
                                        if (target.getRank() != Ranks.ME) {
                                            mGameActivity.removeElement(target);
                                        }
                                        mGameActivity.nextTurn();
                                    }
                                });
                            } else {
                                mGameActivity.nextTurn();
                            }
                        }
                    });
                } else {
                    mGameActivity.nextTurn();
                }
            }
        };

        if (mGameActivity.getActiveCharacter().isRangeAttack()) {
            callback.onActionDone(true);
        } else {
            goCloserTo(tile, callback);
        }
    }

    private void search(final Tile tile) {
        Log.d(TAG, "search");
        if (!mGameActivity.getRoom().isSafe()) {
            setInputEnabled(false);
        }

        goCloserTo(tile, new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                if (success && mGameActivity.getActiveCharacter().isNextTo(tile)) {
                    mGameActivity.playSound("search", false);

                    Searchable searchable;
                    if (tile.getContent() != null && tile.getContent() instanceof Searchable) {
                        searchable = (Searchable) tile.getContent();
                    } else {
                        if (tile.getSubContent().get(0) instanceof Stairs) {
                            searchable = (Searchable) tile.getSubContent().get(1);
                        } else {
                            searchable = (Searchable) tile.getSubContent().get(0);
                        }
                    }
                    final Reward reward = searchable.search();
                    if (searchable instanceof ItemOnGround) {
                        mGameActivity.removeElement(searchable);
                    }

                    // show reward popup
                    mGUIManager.showReward(reward, new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (reward != null) {
                                animateReward(reward);
                            }
                            mGameActivity.nextTurn();
                        }
                    });

                    if (reward != null) {
                        if (reward.getItem() != null) {
                            getItemOrDropIt(reward.getItem());
                        }
                        mGameActivity.getHero().addGold(reward.getGold());
                    }
                } else {
                    mGameActivity.nextTurn();
                }
                isMoving = false;
            }
        });
    }

    public void getItemOrDropIt(final Item item) {
        boolean success = mGameActivity.getHero().addItem(item);
        if (!success) {
            mGameActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ApplicationUtils.showToast(mGameActivity, mGameActivity.getString(R.string.bag_full, item.getName(mGameActivity.getResources())), Toast.LENGTH_LONG);
                }
            });
            dropItem(item);
        }
    }

    public void dropItem(Item item) {
        Tile tile = mGameActivity.getHero().getTilePosition();
        ItemOnGround itemOnGround = new ItemOnGround(item.getIdentifier(), new Reward(item, 0, 0));
        itemOnGround.setTilePosition(tile);
        mGameActivity.addElementToScene(itemOnGround);
        mGameActivity.getRoom().getObjects().add(itemOnGround);
        mScene.sortChildren(true);
    }

    public void talk(final Tile tile) {
        Log.d(TAG, "talk");
        if (!mGameActivity.getRoom().isSafe()) {
            setInputEnabled(false);
        }
        goCloserTo(tile, new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                isMoving = false;
                if (success && mGameActivity.getActiveCharacter().isNextTo(tile)) {
                    talkTo((Pnj) tile.getContent());
                } else {
                    mGameActivity.nextTurn();
                }
            }
        });
    }

    public void talkTo(Pnj pnj) {
        OnDiscussionReplySelected onDiscussionSelected = new OnDiscussionReplySelected() {
            @Override
            public void onReplySelected(Pnj pnj, int next, Reward instantReward) {
                if (instantReward != null) {
                    Log.d(TAG, "got reward from discussion = " + instantReward.getItem() + "," + instantReward.getGold() + "," + instantReward.getXp());
                    if (instantReward.getItem() != null) {
                        getItemOrDropIt(instantReward.getItem());
                        mGUIManager.showReward(instantReward, null);
                    }

                    mGameActivity.getHero().addGold(instantReward.getGold());
                    boolean newLevel = mGameActivity.getHero().addXP(instantReward.getXp());
                    animateReward(instantReward);

                    if (newLevel) {
                        mGUIManager.showNewLevelDialog(null);
                    }
                }

                if (pnj.getDiscussions().size() > 0) {
                    for (int n = 0; n < next; n++) {
                        pnj.getDiscussions().remove(0);
                    }
                    if (next >= 0 && pnj.getDiscussions().size() > 0 && !(pnj.getDiscussionCallback() instanceof StopDiscussionCallback)) {
                        // go to next discussion
                        talkTo(pnj);
                    } else {
                        finishDiscussion(pnj);
                    }
                } else {
                    finishDiscussion(pnj);
                }
            }
        };

        if (pnj.getDiscussions().size() > 0) {
            Discussion discussion = pnj.getNextDiscussion();
            mGUIManager.showDiscussion(pnj, discussion, onDiscussionSelected);
            if (!discussion.isPermanent()) {
                pnj.getDiscussions().remove(0);
            }
        } else {
            mGUIManager.showDiscussion(pnj, new Discussion("discussion_is_over", true, null), onDiscussionSelected);
        }
    }

    private void finishDiscussion(Pnj pnj) {
        Log.d(TAG, "finish discussion");
        if (pnj.getDiscussionCallback() != null) {
            pnj.getDiscussionCallback().onDiscussionOver();

            // check dead units
            for (final GameElement gameElement : mGameActivity.getRoom().getObjects()) {
                if (gameElement instanceof Unit && ((Unit) gameElement).isDead()) {
                    Log.d(TAG, "one unit is dead");
                    animateDeath((Unit) gameElement, new OnActionExecuted() {
                        @Override
                        public void onActionDone(boolean success) {
                            mGameActivity.removeElement(gameElement);
                            mGameActivity.nextTurn();

                        }
                    });
                    return;
                }
            }
        }
        mGameActivity.nextTurn();
    }

    private void goCloserTo(Tile tile, OnActionExecuted callback) {
        if (mGameActivity.getActiveCharacter().isNextTo(tile)) {
            callback.onActionDone(true);
            return;
        }

        List<Tile> shortestPath = null;
        if (mGameActivity.getActiveCharacter().canMoveIn(tile)) {
            shortestPath = new AStar<Tile>().search(mGameActivity.getRoom().getTiles(), mGameActivity.getActiveCharacter().getTilePosition(), tile, false, mGameActivity.getActiveCharacter());
        } else {
            Set<Tile> adjacentTiles = MathUtils.getAdjacentNodes(mGameActivity.getRoom().getTiles(), tile, 1, false, mGameActivity.getActiveCharacter());
            for (Tile adjacent : adjacentTiles) {
                if (mGameActivity.getRoom().isSafe() || adjacent.getAction() == Actions.MOVE) {
                    List<Tile> path = new AStar<Tile>().search(mGameActivity.getRoom().getTiles(), mGameActivity.getActiveCharacter().getTilePosition(), adjacent, false, mGameActivity.getActiveCharacter());
                    if (path != null && (shortestPath == null || path.size() < shortestPath.size())) {
                        shortestPath = path;
                    }
                }
            }
            if (shortestPath == null) {
                for (Tile adjacent : adjacentTiles) {
                    List<Tile> path = new AStar<Tile>().search(mGameActivity.getRoom().getTiles(), mGameActivity.getActiveCharacter().getTilePosition(), adjacent, false, mGameActivity.getActiveCharacter());
                    if (path != null && (shortestPath == null || path.size() < shortestPath.size())) {
                        shortestPath = path;
                    }
                }
                if (shortestPath != null) {
                    shortestPath = shortestPath.subList(0, Math.min(mGameActivity.getActiveCharacter().calculateMovement() + 1, shortestPath.size()));
                }
            }
        }

        if (shortestPath != null) {
            isMoving = true;
            animateMove(shortestPath, callback);
        } else {
            callback.onActionDone(false);
        }
    }

    private void selectTile(Tile tile) {
        // unselect previous selected tile
        if (mSelectedTile != null) {
            mSelectedTile.setSelected(false);
        }

        mSelectedTile = tile;
        if (mSelectedTile != null) {
            mSelectedTile.setSelected(true);
        }
    }

    private Tile getTileAtCoordinates(float x, float y) {
        TMXTile tmxTile = mGameActivity.mTmxTiledMap.getTMXLayers().get(0).getTMXTileAt(x, y);
        if (tmxTile != null) {
            return mGameActivity.getRoom().getTiles()[tmxTile.getTileRow()][tmxTile.getTileColumn()];
        } else {
            return null;
        }
    }

    private void showElementInfo(GameElement gameElement) {
        mSelectedElement = gameElement;
        mGameActivity.mSelectionCircle.attachToGameElement(gameElement);
        mGUIManager.showGameElementInfo(gameElement);
    }

    public void hideElementInfo() {
        mSelectedElement = null;
        mGameActivity.mSelectionCircle.unAttach();
        mGUIManager.hideGameElementInfo(mGameActivity.getRoom().isSafe());
        selectTile(null);
    }

    public void showAllActions(Tile fromTile) {
        Unit activeCharacter = mGameActivity.getActiveCharacter();

        // get reachable tiles
        Set<Tile> reachableTiles = new HashSet<>();
        Tile t;
        for (Tile[] hTile : mGameActivity.getRoom().getTiles()) {
            for (Tile tile : hTile) {
                if (tile != null && !reachableTiles.contains(tile) && MathUtils.calcManhattanDistance(tile, fromTile) <= activeCharacter.calculateMovement()
                        && activeCharacter.canMoveIn(tile)) {
                    List<Tile> path = new AStar<Tile>().search(mGameActivity.getRoom().getTiles(), fromTile, tile, false, activeCharacter);
                    if (path != null) {
                        for (int n = 0; n < path.size(); n++) {
                            t = path.get(n);
                            if (n <= activeCharacter.calculateMovement()) {
                                reachableTiles.add(t);
                            }
                        }
                    }
                }
            }
        }

        // add movement tiles
        ActionTile c;
        for (Tile tile : reachableTiles) {
            tile.setAction(Actions.MOVE);
            c = new ActionTile(Actions.MOVE, tile, mGameActivity.getVertexBufferObjectManager(), activeCharacter.getRank() != Ranks.ME);
            mGameActivity.mGroundLayer.attachChild(c);
        }

        // add special actions
        if (activeCharacter.getRank() == Ranks.ME) {
            showSpecialActions();
        }
    }

    public void showSpecialActions() {
        for (GameElement gameElement : mGameActivity.getRoom().getObjects()) {
            if (mGameActivity.getActiveCharacter() != gameElement
                    && ((mGameActivity.getRoom().isSafe() || MathUtils.calcManhattanDistance(gameElement.getTilePosition(), mGameActivity.getActiveCharacter().getTilePosition()) <= mGameActivity.getActiveCharacter().calculateMovement() + 1)
                    || mGameActivity.getActiveCharacter().isRangeAttack() && gameElement.isEnemy(mGameActivity.getActiveCharacter()))) {
                addAvailableAction(gameElement);
            }
        }
    }

    public void hideActionTiles() {
        mSelectedTile = null;
        ActionTile actionTile;
        for (int n = 0; n < mGameActivity.mGroundLayer.getChildCount(); n++) {
            actionTile = (ActionTile) mGameActivity.mGroundLayer.getChildByIndex(n);
            actionTile.getTile().setAction(null);
            actionTile.getTile().setSelected(false);
        }
        mGameActivity.mGroundLayer.detachChildren();
    }

    private void addAvailableAction(GameElement gameElement) {
        boolean isActionPossible = mGameActivity.getRoom().isSafe() || mGameActivity.getActiveCharacter().isNextTo(gameElement.getTilePosition())
                || mGameActivity.getActiveCharacter().isRangeAttack() && gameElement.isEnemy(mGameActivity.getActiveCharacter());
        if (!isActionPossible) {
            Set<Tile> adjacentTiles = MathUtils.getAdjacentNodes(mGameActivity.getRoom().getTiles(), gameElement.getTilePosition(), 1, false, mGameActivity.getActiveCharacter());
            for (Tile adjacent : adjacentTiles) {
                if (adjacent.getAction() == Actions.MOVE) {
                    isActionPossible = true;
                    break;
                }
            }
        }

        if (isActionPossible) {
            if (mGameActivity.getActiveCharacter().isEnemy(gameElement)) {
                addActionToTile(Actions.ATTACK, gameElement.getTilePosition());
            } else if (gameElement instanceof Pnj) {
                addActionToTile(Actions.TALK, gameElement.getTilePosition());
            } else if (gameElement instanceof Searchable) {
                addActionToTile(Actions.SEARCH, gameElement.getTilePosition());
            } else if (gameElement instanceof Light) {
                addActionToTile(Actions.LIGHT, gameElement.getTilePosition());
            }
        }
    }

    private void addActionToTile(Actions action, Tile tile) {
        ActionTile actionTile = new ActionTile(action, tile, mGameActivity.getVertexBufferObjectManager(), false);
        tile.setAction(action);
        mGameActivity.mGroundLayer.attachChild(actionTile);
    }

    private void animateMove(List<Tile> path, final OnActionExecuted callback) {
        Log.d(TAG, "animate movement");
        if (path.size() > 1) {
            path.remove(0);
            Log.d(TAG, "path size = " + path.size());
            final UnitSprite sprite = (UnitSprite) mGameActivity.getActiveCharacter().getSprite();
            final Tile nextTile = path.get(0);
            final Directions direction = Directions.from(nextTile.getX() - mGameActivity.getActiveCharacter().getTilePosition().getX(), mGameActivity.getActiveCharacter().getTilePosition().getY() - nextTile.getY());
            sprite.walk(direction);
            final List<Tile> p = new ArrayList<>(path);
            animationHandler = new TimerHandler(1.0f / 60, true, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    if (direction == Directions.EAST && sprite.getX() >= nextTile.getTileX()
                            || direction == Directions.WEST && sprite.getX() <= nextTile.getTileX()
                            || direction == Directions.SOUTH && sprite.getY() >= nextTile.getTileY()
                            || direction == Directions.NORTH && sprite.getY() <= nextTile.getTileY()) {

                        Log.d(TAG, "unregister movement animation handler");
                        animationHandler.reset();
                        mScene.unregisterUpdateHandler(animationHandler);

                        mGameActivity.getActiveCharacter().setTilePosition(nextTile);
                        sprite.setPosition(nextTile.getTileX(), nextTile.getTileY());
                        Log.d(TAG, "character is z-index = " + sprite.getZIndex());

                        if (mNextTile == null) {
                            mScene.sortChildren();
                            animateMove(p, callback);
                        } else {
                            Log.d(TAG, "go to next tile");
                            mScene.sortChildren();
                            sprite.stand();
                            move(mNextTile);
                            mNextTile = null;
                        }
                    } else {
                        sprite.setPosition(sprite.getX() + 2 * direction.getDx(), sprite.getY() - 2 * direction.getDy());
                        mInputManager.checkAutoScrolling(sprite.getX(), sprite.getY());
                    }
                }
            });
            mScene.registerUpdateHandler(animationHandler);
        } else {
            Log.d(TAG, "movement animation is over");
            UnitSprite sprite = (UnitSprite) mGameActivity.getActiveCharacter().getSprite();
            sprite.stand();
            mScene.sortChildren();
            mScene.unregisterUpdateHandler(animationHandler);
            callback.onActionDone(true);
        }
    }

    private void animateFight(final Unit attacker, final Unit target, final FightResult fightResult, final OnActionExecuted callback) {
        final UnitSprite attackerSprite = (UnitSprite) attacker.getSprite();
        final Sprite targetSprite = target.getSprite();

        if (attacker.isNextTo(target.getTilePosition())) {
            mGameActivity.playSound("close_combat_attack", false);
        } else if (attacker.isRangeAttack()) {
            mGameActivity.playSound("range_attack", false);
        }

        // play sound
        if (fightResult.getState() == FightResult.States.BLOCK) {
            mGameActivity.playSound("block", false);
        } else if (fightResult.getState() == FightResult.States.DAMAGE && fightResult.getDamage() > 0) {
            if (target.getRank() == Ranks.ME) {
                mGameActivity.playSound("damage_hero", false);
            } else {
                mGameActivity.playSound("damage_monster", false);
            }
        }

        // draw damage and fight result text
        if (fightResult.getState() == FightResult.States.DAMAGE || fightResult.getState() == FightResult.States.CRITICAL) {
            showAnimatedText(target, "-" + fightResult.getDamage());
        }
        if (fightResult.getState() != FightResult.States.DAMAGE) {
            mGameActivity.drawAnimatedText(targetSprite.getX() + GameConstants.PIXEL_BY_TILE / 3, targetSprite.getY() - GameConstants.PIXEL_BY_TILE, fightResult.getState().name().toLowerCase(), fightResult.getState().getColor(), 0.4f, 40, -0.15f);
        }

        // animate characters
        final Directions direction = Directions.from(targetSprite.getX() - attackerSprite.getX(), targetSprite.getY() - attackerSprite.getY());
        attackerSprite.changeOrientation(Directions.from(targetSprite.getX() - attackerSprite.getX(), attackerSprite.getY() - targetSprite.getY()));
        animationHandler = new TimerHandler(1.0f / 50, true, new ITimerCallback() {

            private static final int DURATION_IN_FRAMES = 17;
            private static final int OFFSET = 5;
            private static final float ATTACKER_SPEED = 3f;
            private static final float TARGET_SPEED = 1.2f;

            private int offset = OFFSET;

            private boolean done = false;

            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                offset--;
                if (offset >= -OFFSET) {
                    attackerSprite.setPosition(attackerSprite.getX() + (offset >= 0 ? ATTACKER_SPEED : -ATTACKER_SPEED) * direction.getDx(), attackerSprite.getY() + (offset >= 0 ? ATTACKER_SPEED : -ATTACKER_SPEED) * direction.getDy());
                    if (fightResult.getState() == FightResult.States.DAMAGE || fightResult.getState() == FightResult.States.CRITICAL) {
                        targetSprite.setPosition(targetSprite.getX(), targetSprite.getY() - (offset >= 0 ? TARGET_SPEED : -TARGET_SPEED) * Directions.NORTH.getDy());
                        targetSprite.setColor(1.0f, 0.0f, 0.0f);
                    } else if (fightResult.getState() == FightResult.States.DODGE) {
                        targetSprite.setPosition(targetSprite.getX() + (offset >= 0 ? ATTACKER_SPEED : -ATTACKER_SPEED) * direction.getDx(), targetSprite.getY() + (offset >= 0 ? ATTACKER_SPEED : -ATTACKER_SPEED) * direction.getDy());
                    }
                } else if (offset <= -DURATION_IN_FRAMES + OFFSET) {
                    targetSprite.setColor(1.0f, 1.0f, 1.0f);
                    mScene.unregisterUpdateHandler(animationHandler);
                    if (!done) {
                        done = true;
                        attackerSprite.stand();
                        target.updateSprite();
                        callback.onActionDone(true);
                    }
                }
            }
        });
        mScene.registerUpdateHandler(animationHandler);
    }

    private void showAnimatedText(Unit target, String message) {
        mGameActivity.drawAnimatedText(target.getSprite().getX() - 2 * GameConstants.PIXEL_BY_TILE / 3, target.getSprite().getY() - GameConstants.PIXEL_BY_TILE, message, message.startsWith("-") ? FightResult.States.DAMAGE.getColor() : FightResult.States.DODGE.getColor(), 0.4f, 40, -0.15f);
    }

    public void animateDeath(final Unit target, final OnActionExecuted onActionExecuted) {
        Log.d(TAG, "animating death");
        Sprite sprite = target.getSprite();
        mGameActivity.drawAnimatedSprite(sprite.getX(), sprite.getY(), "blood.png", 65, GameConstants.ANIMATED_SPRITE_ALPHA, 1.0f, 0, true, 10, new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                if (mGameActivity.getActiveCharacter().getRank() == Ranks.ME && (target instanceof Monster || target instanceof Pnj)) {
                    animateFightReward(target, onActionExecuted);
                } else {
                    onActionExecuted.onActionDone(true);
                }
            }
        });
        mGameActivity.playSound("death", false);
        sprite.setRotation(90);
        sprite.setPosition(sprite.getX() + 10, sprite.getY() + 10);
    }

    private void animateReward(Reward reward) {
        if (reward.getGold() > 0) {
            mGameActivity.playSound("coins", false);
            mGameActivity.drawAnimatedText(mGameActivity.getHero().getSprite().getX() - 4 * GameConstants.PIXEL_BY_TILE / 3, mGameActivity.getHero().getSprite().getY() - GameConstants.PIXEL_BY_TILE, "+" + reward.getGold() + " gold", Color.YELLOW, 0.4f, 50, -0.15f);
        }
        if (reward.getXp() > 0) {
            mGameActivity.drawAnimatedText(mGameActivity.getHero().getSprite().getX() + GameConstants.PIXEL_BY_TILE / 2, mGameActivity.getHero().getSprite().getY() - GameConstants.PIXEL_BY_TILE, "+" + reward.getXp() + "xp", new Color(1.0f, 1.0f, 1.0f), 0.4f, 50, -0.15f);
        }
    }

    private void animateFightReward(Unit target, final OnActionExecuted onActionExecuted) {
        Log.d(TAG, "animating fight reward");
        final Reward reward = target.getReward();
        if (reward == null) {
            onActionExecuted.onActionDone(true);
            return;
        }

        if (reward.getItem() != null) {
            getItemOrDropIt(reward.getItem());
        }
        mGameActivity.getHero().addGold(reward.getGold());
        mGameActivity.getHero().addFrag(target.getIdentifier());
        final boolean newLevel = mGameActivity.getHero().addXP(reward.getXp());

        animateReward(reward);

        if (reward.getItem() != null) {
            mGameActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGUIManager.showReward(reward, new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            Log.d(TAG, "animating fight reward is over");
                            if (newLevel) {
                                mGameActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mGUIManager.showNewLevelDialog(onActionExecuted);
                                    }
                                });
                            } else {
                                onActionExecuted.onActionDone(true);
                            }
                        }
                    });
                }
            });
        } else {
            Log.d(TAG, "animating fight reward is over");
            if (newLevel) {
                mGameActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mGUIManager.showNewLevelDialog(onActionExecuted);
                    }
                });
            } else {
                onActionExecuted.onActionDone(true);
            }
        }
    }

    public ActiveSkill getActivatedSkill() {
        return activatedSkill;
    }

    public void setActivatedSkill(ActiveSkill skill) {
        if (skill.isPersonal()) {
            activatedSkill = skill;
            mGUIManager.displayBigLabel(mGameActivity.getString(R.string.use_skill_personal, mGameActivity.getString(skill.getName(mGameActivity.getResources()))), R.color.green);
            useSkill(mGameActivity.getActiveCharacter().getTilePosition());
        } else if (activatedSkill == skill) {
            activatedSkill = null;
            mGUIManager.displayBigLabel(mGameActivity.getString(R.string.use_skill_off, mGameActivity.getString(skill.getName(mGameActivity.getResources()))), R.color.red);
        } else {
            activatedSkill = skill;
            mGUIManager.displayBigLabel(mGameActivity.getString(R.string.use_skill_on, mGameActivity.getString(skill.getName(mGameActivity.getResources()))), R.color.green);
        }
    }

    public void useSkill(final Tile tile) {
        setInputEnabled(false);
        mGUIManager.displayBigLabel(mGameActivity.getString(R.string.use_skill_personal, mGameActivity.getString(activatedSkill.getName(mGameActivity.getResources()))), R.color.green);
        mGameActivity.playSound("magic", false);

        animateSkill(new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                Effect effect = activatedSkill.getEffect();
                if (!activatedSkill.isPersonal() || activatedSkill.getRadius() == 0) {
                    applyEffect(effect, tile, true);
                }
                if (activatedSkill.getRadius() > 0) {
                    Set<Tile> targetTiles = MathUtils.getAdjacentNodes(mGameActivity.getRoom().getTiles(), tile, activatedSkill.getRadius(), true, null);
                    for (Tile targetTile : targetTiles) {
                        if (targetTile != mGameActivity.getActiveCharacter().getTilePosition()) {
                            applyEffect(effect, targetTile, true);
                        }
                    }
                }

                activatedSkill.use();
                activatedSkill = null;
                mGUIManager.updateSkillButtons();

                // go to next turn when everyone is dead
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mGameActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mGameActivity.nextTurn();
                            }
                        });
                    }
                }, 700);
            }
        });
    }

    public void applyEffect(Effect effect, final Tile tile, boolean addExtra) {
        Log.d(TAG, "Applying effect of " + effect.getValue() + " " + effect.getTarget() + " on tile " + tile.getX() + "," + tile.getY());
        // apply effect
        if (tile.getContent() != null && tile.getContent() instanceof Unit) {
            final Unit target = (Unit) tile.getContent();
            if (effect instanceof RecoveryEffect) {
                // recover all skills
                for (Skill skill : target.getSkills()) {
                    if (skill instanceof ActiveSkill) {
                        ((ActiveSkill) skill).reset();
                    }
                }
                mGUIManager.updateSkillButtons();
            } else if (effect.getTarget() == Characteristics.HP) {
                // damage or heal
                if (effect instanceof PoisonEffect && effect.getValue() > 0 && target.getHp() - target.getCurrentHP() == 0) {
                    // do not show useless regeneration
                } else {
                    showAnimatedText(target, effect.getValue() > 0 ? "+" + Math.min(target.getHp() - target.getCurrentHP(), effect.getValue()) : "" + effect.getValue());
                }
                target.setCurrentHP(Math.min(target.getHp(), target.getCurrentHP() + effect.getValue()));
            } else {
                // special effects
                target.getBuffs().add(effect);

                if (!(effect instanceof StunEffect) && effect.getTarget() != null) {
                    showAnimatedText(target, (effect.getValue() > 0 ? "+" + effect.getValue() : "" + effect.getValue()) + " " + effect.getTarget().name());
                }
            }

            if (addExtra && effect.getSpecial() != null) {
                target.getBuffs().add(effect.getSpecial());
            }

            if (target.isDead()) {
                // animate deaths and remove dead units
                animateDeath(target, new OnActionExecuted() {
                    @Override
                    public void onActionDone(boolean success) {
                    }
                });
                mGameActivity.removeElement(target);
            }

            target.updateSprite();
        }

        // animate
        if (effect.getSpriteName() != null) {
            mGameActivity.drawAnimatedSprite(tile.getTileX(), tile.getTileY(), effect.getSpriteName(), 50, GameConstants.ANIMATED_SPRITE_ALPHA, 1.0f, 0, true, 100, null);
        }

        mGUIManager.updateActiveHeroLayout();
        mGUIManager.updateQueue(mGameActivity.getActiveCharacter(), mGameActivity.getRoom());
    }

    public void setInputEnabled(boolean enabled) {
        inputDisabled = !enabled;
        mInputManager.setEnabled(enabled);
    }

    private void animateSkill(final OnActionExecuted callback) {
        final UnitSprite sprite = (UnitSprite) mGameActivity.getActiveCharacter().getSprite();
        if (activatedSkill.getIdentifier().equals("swirl_swords")) {
            Set<Tile> targetTiles = MathUtils.getAdjacentNodes(mGameActivity.getRoom().getTiles(), mGameActivity.getActiveCharacter().getTilePosition(), activatedSkill.getRadius(), true, null);
            final Iterator<Tile> tileIterator = targetTiles.iterator();
            new Timer().schedule(new TimerTask() {
                private int n = 7;
                private Tile tile;

                @Override
                public void run() {
                    sprite.walk(Directions.values()[n % 4]);
                    tile = tileIterator.next();
                    mGameActivity.drawAnimatedSprite(tile.getTileX(), tile.getTileY(), "slash.png", 70, GameConstants.ANIMATED_SPRITE_ALPHA, 0.7f, 0, true, 100, null);
                    n--;
                    if (n < 0) {
                        sprite.stand();
                        cancel();
                        callback.onActionDone(true);
                    }
                }
            }, 0, 80);
        } else if (activatedSkill.getIdentifier().equals("berserker_rage")) {
            sprite.stand();
            sprite.setColor(((BuffEffect) activatedSkill.getEffect()).getBuffColor());
            new Timer().schedule(new TimerTask() {
                private int n = 7;

                @Override
                public void run() {
                    sprite.setScale((float) (0.5 + 0.03 * (n % 3)));
                    n--;
                    if (n < 0) {
                        sprite.setScale(0.5f);
                        cancel();
                        callback.onActionDone(true);
                    }
                }
            }, 0, 80);
        } else if (activatedSkill.getIdentifier().equals("camouflage")) {
            sprite.stand();
            new Timer().schedule(new TimerTask() {
                private int n = 7;

                @Override
                public void run() {
                    sprite.setAlpha((float) (0.3 + 0.1 * n));
                    n--;
                    if (n < 0) {
                        cancel();
                        callback.onActionDone(true);
                    }
                }
            }, 0, 80);
        } else if (activatedSkill.getIdentifier().equals("ground_slam")) {
            sprite.stand();
            new Timer().schedule(new TimerTask() {
                private int n = 12;
                private final float initialY = sprite.getY();
                private final float initialCameraX = mGameActivity.getCamera().getCenterX();

                @Override
                public void run() {
                    if (n >= 5) {
                        sprite.setPosition(sprite.getX(), (float) (initialY - 10 * Math.sin((n - 5) * Math.PI / 7)));
                    } else {
                        mGameActivity.getCamera().offsetCenter((float) (-3 * Math.cos(n * 2 * Math.PI / 5)), mGameActivity.getCamera().getCenterY());
                    }

                    n--;
                    if (n < 0) {
                        sprite.setPosition(sprite.getX(), initialY);
                        mGameActivity.getCamera().setCenter(initialCameraX, mGameActivity.getCamera().getCenterY());
                        cancel();
                        callback.onActionDone(true);
                    }
                }
            }, 0, 50);
        } else if (activatedSkill.getIdentifier().equals("drunken_master")) {
            sprite.stand();
            new Timer().schedule(new TimerTask() {
                private int n = 7;
                private final float initialX = sprite.getX();

                @Override
                public void run() {
                    sprite.setColor(Color.GREEN);
                    sprite.setPosition((float) (initialX - 3 * Math.cos(n * 2 * Math.PI / 7)), sprite.getY());

                    n--;
                    if (n < 0) {
                        sprite.setColor(Color.WHITE);
                        sprite.setPosition(initialX, sprite.getY());
                        cancel();
                        callback.onActionDone(true);
                    }
                }
            }, 0, 60);
        } else if (activatedSkill.getIdentifier().equals("war_cry")) {
            sprite.stand();
            new Timer().schedule(new TimerTask() {
                private int n = 8;
                private final float initialCameraX = mGameActivity.getCamera().getCenterX();

                @Override
                public void run() {
                    mGameActivity.getCamera().offsetCenter((float) (3 * Math.cos(n * 2 * Math.PI / 8)), mGameActivity.getCamera().getCenterY());

                    n--;
                    if (n < 0) {
                        mGameActivity.getCamera().setCenter(initialCameraX, mGameActivity.getCamera().getCenterY());
                        cancel();
                        callback.onActionDone(true);
                    }
                }
            }, 0, 50);
        } else if (activatedSkill.getIdentifier().equals("healing_plants")) {
            sprite.stand();
            new Timer().schedule(new TimerTask() {
                private int n = 7;

                @Override
                public void run() {
                    sprite.setColor(n % 4 == 0 ? Color.WHITE : new Color(0.0f, 0.6f, 0.0f));

                    n--;
                    if (n < 0) {
                        sprite.setColor(Color.WHITE);
                        cancel();
                        callback.onActionDone(true);
                    }
                }
            }, 0, 60);
        } else if (activatedSkill.getIdentifier().equals("stone_skin")) {
            sprite.stand();
            sprite.setColor(((BuffEffect) activatedSkill.getEffect()).getBuffColor());

            new Timer().schedule(new TimerTask() {
                private int n = 7;

                @Override
                public void run() {
                    sprite.setScale((float) (0.5 + 0.07 * Math.sin(n * Math.PI / 7)));

                    n--;
                    if (n < 0) {
                        sprite.setScale(0.5f);
                        cancel();
                        callback.onActionDone(true);
                    }
                }
            }, 0, 60);
        } else {
            callback.onActionDone(true);
        }
    }

}
