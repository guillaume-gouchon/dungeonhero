package com.glevel.dungeonhero.game;

import android.content.DialogInterface;
import android.widget.Toast;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.GameActivity;
import com.glevel.dungeonhero.data.dungeon.GroundTypes;
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
import com.glevel.dungeonhero.models.dungeons.Directions;
import com.glevel.dungeonhero.models.dungeons.Tile;
import com.glevel.dungeonhero.models.dungeons.decorations.ItemOnGround;
import com.glevel.dungeonhero.models.dungeons.decorations.Searchable;
import com.glevel.dungeonhero.models.items.Item;
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
import java.util.List;
import java.util.Set;

/**
 * Created by guillaume on 10/14/14.
 */
public class ActionsDispatcher implements UserActionListener {

    private final GameActivity mGameActivity;
    private final GUIManager mGUIManager;
    private final InputManager mInputManager;
    private Scene mScene;
    private GameElement mSelectedElement;
    private Tile mSelectedTile = null;
    private boolean isMoving = false;
    private boolean interrupt = false;
    private TimerHandler animationHandler;

    // TODO needs refactor
    public ActionsDispatcher(GameActivity gameActivity, Scene scene) {
        mGameActivity = gameActivity;
        mInputManager = mGameActivity.getInputManager();
        mGUIManager = mGameActivity.getGUIManager();
        mScene = scene;
    }

    @Override
    public void onTouch(float x, float y) {
        Tile tile = getTileAtCoordinates(x, y);
        if (tile != null && tile.getAction() != null && mGameActivity.getActiveCharacter().getRank() == Ranks.ME) {
            selectTile(tile);
        }
    }

    @Override
    public void onTap(float x, float y) {
        Tile tile = getTileAtCoordinates(x, y);
        if (tile != null && mGameActivity.getActiveCharacter().getRank() == Ranks.ME) {
            if (tile.getSubContent() != null && tile.getSubContent() != mSelectedElement) {
                showElementInfo(tile.getSubContent());
            } else if (tile.getContent() != null && tile.getContent() != mSelectedElement && tile.getContent().getRank() != Ranks.ME) {
                showElementInfo(tile.getContent());
            } else if (tile.getContent() != null && tile.getContent().getRank() == Ranks.ME && mSelectedTile == tile && tile.getSubContent() == null) {// end movement
                mGameActivity.nextTurn();
            } else if (!isMoving && tile.getAction() != null && tile.getAction() != Actions.NONE) {
                executeAction(tile);
            } else {
                hideElementInfo();
                if (mGameActivity.getRoom().isSafe()) {
                    if (isMoving) {
                        interrupt = true;
                        mGameActivity.runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                hideActionTiles();
                                showActions();
                                selectTile(null);
                            }
                        });
                    } else if (mGameActivity.getActiveCharacter().canMoveIn(tile)) {
                        selectTile(tile);
                        addActionToTile(Actions.NONE, tile);
                        move(tile);
                    }
                }
            }
        }
    }

    @Override
    public void onCancel(float x, float y) {
        selectTile(null);
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
        }
    }

    private void move(Tile tile) {
        if (!mGameActivity.getRoom().isSafe()) {
            mInputManager.setEnabled(false);
        }
        List<Tile> path = new AStar<Tile>().search(mGameActivity.getRoom().getTiles(), mGameActivity.getActiveCharacter().getTilePosition(), tile, false, mGameActivity.getActiveCharacter());
        if (path != null) {
            isMoving = true;
            animateMove(path, new OnActionExecuted() {
                private boolean done = false;

                @Override
                public void onActionDone(boolean success) {
                    if (mGameActivity.getRoom().isSafe()) {
                        mGameActivity.runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                hideActionTiles();
                                showActions();
                            }
                        });
                    }
                    mInputManager.setEnabled(true);
                    isMoving = false;
                    selectTile(null);
                    if (!done && mGameActivity.getActiveCharacter().getRank() == Ranks.ME && mGameActivity.getActiveCharacter().getTilePosition().getGround() == GroundTypes.DOOR) {
                        done = true;
                        mGameActivity.switchRoom(mGameActivity.getActiveCharacter().getTilePosition());
                    }
                }
            });
        } else {
            if (mGameActivity.getActiveCharacter().getRank() == Ranks.ME) {
                mInputManager.setEnabled(true);
            }
        }
    }

    public void attack(final Tile tile) {
        mInputManager.setEnabled(false);
        goCloserTo(tile, new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                if (success) {
                    final Unit target = (Unit) tile.getContent();
                    FightResult fightResult = mGameActivity.getActiveCharacter().attack(target);
                    animateFight(mGameActivity.getActiveCharacter(), target, fightResult, new OnActionExecuted() {
                        @Override
                        public void onActionDone(boolean success) {
                            if (target.isDead()) {
                                animateDeath(target, new OnActionExecuted() {
                                    @Override
                                    public void onActionDone(boolean success) {
                                        mGameActivity.removeElement(target);
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
                isMoving = false;
                if (mGameActivity.getActiveCharacter().getRank() == Ranks.ME) {
                    mInputManager.setEnabled(true);
                }
            }
        });
    }

    private void search(final Tile tile) {
        if (!mGameActivity.getRoom().isSafe()) {
            mInputManager.setEnabled(false);
        }
        goCloserTo(tile, new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                if (success) {
                    Searchable searchable;
                    if (tile.getContent() != null && tile.getContent() instanceof Searchable) {
                        searchable = (Searchable) tile.getContent();
                    } else {
                        searchable = (Searchable) tile.getSubContent();
                    }
                    Reward reward = searchable.search();
                    if (searchable instanceof ItemOnGround) {
                        mGameActivity.removeElement(searchable);
                    }
                    foundReward(reward, true);
                } else {
                    mGameActivity.nextTurn();
                }
                isMoving = false;
                mInputManager.setEnabled(true);
            }
        });
    }

    private void foundReward(Reward reward, final boolean nextTurn) {
        mGUIManager.showReward(reward, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (nextTurn) {
                    mGameActivity.nextTurn();
                }
            }
        });

        if (reward != null) {
            // update hero
            if (reward.getItem() != null) {
                getItemOrDropIt(reward.getItem());
            }
            mGameActivity.getHero().addGold(reward.getGold());
            mGameActivity.getHero().addXP(reward.getXp());
        }
    }

    private void getItemOrDropIt(Item item) {
        boolean success = mGameActivity.getHero().addItem(item);
        if (!success) {
            mGameActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ApplicationUtils.showToast(mGameActivity, R.string.bag_full, Toast.LENGTH_LONG);
                }
            });
            dropItem(item);
        }
    }

    public void dropItem(Item item) {
        Tile tile = mGameActivity.getHero().getTilePosition();
        if (tile.getSubContent() != null) {
            mGameActivity.removeElement(tile.getSubContent());
        }
        ItemOnGround itemOnGround = new ItemOnGround(item.getName(), new Reward(item, 0, 0));
        itemOnGround.setTilePosition(tile);
        mGameActivity.addElementToScene(itemOnGround);
        mGameActivity.getRoom().getObjects().add(itemOnGround);
    }

    private void talk(final Tile tile) {
        if (!mGameActivity.getRoom().isSafe()) {
            mInputManager.setEnabled(false);
        }
        goCloserTo(tile, new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                isMoving = false;
                if (success) {
                    talkTo((Pnj) tile.getContent());
                } else {
                    mGameActivity.nextTurn();
                }
                mInputManager.setEnabled(true);
            }
        });
    }

    private void talkTo(Pnj pnj) {
        OnDiscussionReplySelected onDiscussionSelected = new OnDiscussionReplySelected() {
            @Override
            public void onReplySelected(Pnj pnj, int next) {
                if (pnj.getDiscussions().size() > 0) {
                    Reward reward = pnj.getDiscussions().get(0).getReward();
                    if (reward != null) {
                        // get reward if any
                        foundReward(reward, false);
                    }
                    for (int n = 0; n < next; n++) {
                        pnj.getDiscussions().remove(0);
                    }
                    if (next >= 0 && pnj.getDiscussions().size() > 0) {
                        // go to next discussion
                        talkTo(pnj);
                    } else {
                        mGameActivity.nextTurn();
                    }
                } else {
                    mGameActivity.nextTurn();
                }
            }
        };
        if (pnj.getDiscussions().size() > 0) {
            Discussion discussion = pnj.getDiscussions().get(0);
            mGUIManager.showDiscussion(pnj, discussion, onDiscussionSelected);
            if (!discussion.isPermanent()) {
                pnj.getDiscussions().remove(0);
            }
        } else {
            mGUIManager.showDiscussion(pnj, new Discussion(pnj.getImage(), pnj.getName(), R.string.discussion_is_over, null, true, null), onDiscussionSelected);
        }
    }

    private void goCloserTo(Tile tile, OnActionExecuted callback) {
        if (MathUtils.calcManhattanDistance(mGameActivity.getActiveCharacter().getTilePosition(), tile) <= 1) {
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

    private void addAvailableAction(GameElement gameElement) {
        boolean isActionPossible = mGameActivity.getRoom().isSafe() || MathUtils.calcManhattanDistance(mGameActivity.getActiveCharacter().getTilePosition(), gameElement.getTilePosition()) <= 1;
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
            }
        }
    }

    private void addActionToTile(Actions action, Tile tile) {
        ActionTile actionTile = new ActionTile(action, tile, mGameActivity.getVertexBufferObjectManager());
        tile.setAction(action);
        mGameActivity.mGroundLayer.attachChild(actionTile);
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

    public void showMovement() {
        // get reachable tiles
        Set<Tile> reachableTiles = new HashSet<Tile>();
        Tile t;
        for (Tile[] hTile : mGameActivity.getRoom().getTiles()) {
            for (Tile tile : hTile) {
                if (!reachableTiles.contains(tile) && MathUtils.calcManhattanDistance(tile, mGameActivity.getActiveCharacter().getTilePosition()) <= mGameActivity.getActiveCharacter().getMovement()
                        && mGameActivity.getActiveCharacter().canMoveIn(tile)) {
                    List<Tile> path = new AStar<Tile>().search(mGameActivity.getRoom().getTiles(), mGameActivity.getActiveCharacter().getTilePosition(), tile, false, mGameActivity.getActiveCharacter());
                    if (path != null) {
                        for (int n = 0; n < path.size(); n++) {
                            t = path.get(n);
                            if (n <= mGameActivity.getActiveCharacter().getMovement()) {
                                reachableTiles.add(t);
                            }
                        }
                    }
                }
            }
        }

        ActionTile c;
        for (Tile tile : reachableTiles) {
            tile.setAction(Actions.MOVE);
            c = new ActionTile(Actions.MOVE, tile, mGameActivity.getVertexBufferObjectManager());
            if (mGameActivity.getActiveCharacter().getRank() != Ranks.ME) {
                c.setAlpha(0.15f);
            }
            mGameActivity.mGroundLayer.attachChild(c);
        }

        if (mGameActivity.getActiveCharacter().getRank() == Ranks.ME) {
            showActions();
        }
    }

    public void showActions() {
        for (GameElement gameElement : mGameActivity.getRoom().getObjects()) {
            if (mGameActivity.getActiveCharacter() != gameElement && (mGameActivity.getRoom().isSafe() || MathUtils.calcManhattanDistance(gameElement.getTilePosition(), mGameActivity.getActiveCharacter().getTilePosition()) <= mGameActivity.getActiveCharacter().getMovement() + 1)) {
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

    private void animateMove(List<Tile> path, final OnActionExecuted callback) {
        if (path.size() > 1) {
            path.remove(0);
            final UnitSprite sprite = (UnitSprite) mGameActivity.getActiveCharacter().getSprite();
            final Tile nextTile = path.get(0);
            final Directions direction = Directions.from(nextTile.getX() - mGameActivity.getActiveCharacter().getTilePosition().getX(), mGameActivity.getActiveCharacter().getTilePosition().getY() - nextTile.getY());
            sprite.walk(direction);
            final List<Tile> p = new ArrayList<Tile>(path);
            animationHandler = new TimerHandler(1.0f / 40, true, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    sprite.setPosition(sprite.getX() + direction.getDx(), sprite.getY() - direction.getDy());
                    mInputManager.checkAutoScrolling(sprite.getX(), sprite.getY());
                    if (direction == Directions.EAST && sprite.getX() >= nextTile.getTileX()
                            || direction == Directions.WEST && sprite.getX() <= nextTile.getTileX()
                            || direction == Directions.SOUTH && sprite.getY() >= nextTile.getTileY()
                            || direction == Directions.NORTH && sprite.getY() <= nextTile.getTileY()) {
                        mScene.unregisterUpdateHandler(animationHandler);
                        mGameActivity.getActiveCharacter().setTilePosition(nextTile);
                        sprite.setPosition(nextTile.getTileX(), nextTile.getTileY());

                        mScene.sortChildren(true);
                        if (!interrupt) {
                            animateMove(p, callback);
                        } else {
                            callback.onActionDone(false);
                            interrupt = false;
                        }
                    }
                }
            });
            mScene.registerUpdateHandler(animationHandler);
        } else {
            UnitSprite sprite = (UnitSprite) mGameActivity.getActiveCharacter().getSprite();
            sprite.stand();
            callback.onActionDone(true);
        }
    }

    private void animateFight(final Unit attacker, Unit target, final FightResult fightResult, final OnActionExecuted callback) {
        final Sprite attackerSprite = attacker.getSprite(), targetSprite = target.getSprite();

        // draw text
        if (fightResult.getState() == FightResult.States.DAMAGE || fightResult.getState() == FightResult.States.CRITICAL) {
            mGameActivity.drawAnimatedText(targetSprite.getX() - 2 * GameConstants.PIXEL_BY_TILE / 3, targetSprite.getY() - GameConstants.PIXEL_BY_TILE / 2, "-" + fightResult.getDamage(), fightResult.getState().getColor(), 0.2f, 40, -0.15f);
        }
        if (fightResult.getState() != FightResult.States.DAMAGE) {
            mGameActivity.drawAnimatedText(targetSprite.getX() + GameConstants.PIXEL_BY_TILE / 2, targetSprite.getY() - GameConstants.PIXEL_BY_TILE / 2, fightResult.getState().name(), fightResult.getState().getColor(), 0.2f, 40, -0.15f);
        }

        // animate characters
        final Directions direction = Directions.from(targetSprite.getX() - attackerSprite.getX(), targetSprite.getY() - attackerSprite.getY());
        animationHandler = new TimerHandler(1.0f / 60, true, new ITimerCallback() {

            private static final int DURATION_IN_FRAMES = 30;
            private static final int OFFSET = 5;
            private static final float ATTACKER_SPEED = 1.5f;
            private static final float TARGET_SPEED = 0.6f;

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
                        callback.onActionDone(true);
                    }
                }
            }
        });
        mScene.registerUpdateHandler(animationHandler);
    }

    private void animateDeath(final Unit target, final OnActionExecuted onActionExecuted) {
        mGameActivity.drawAnimatedSprite(target.getSprite().getX(), target.getSprite().getY(), "blood.png", 65, 0.3f, 0, true, 10, new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                if (target instanceof Monster) {
                    animateGetReward((Monster) target, onActionExecuted);
                } else {
                    onActionExecuted.onActionDone(true);
                }
            }
        });
    }

    private void animateGetReward(Monster target, final OnActionExecuted onActionExecuted) {
        Reward reward = target.getReward();
        if (reward.getItem() != null) {
            getItemOrDropIt(reward.getItem());
        }
        mGameActivity.getHero().addGold(reward.getGold());
        mGameActivity.getHero().addXP(reward.getXp());

        if (reward != null) {
            if (reward.getGold() > 0) {
                mGameActivity.drawAnimatedText(target.getSprite().getX() - GameConstants.PIXEL_BY_TILE, target.getSprite().getY() - GameConstants.PIXEL_BY_TILE / 2, "+" + reward.getGold() + " gold", new Color(1, 1, 0), 0.2f, 50, -0.15f);
            }
            if (reward.getXp() > 0) {
                mGameActivity.drawAnimatedText(target.getSprite().getX() + 2 * GameConstants.PIXEL_BY_TILE / 3, target.getSprite().getY() - GameConstants.PIXEL_BY_TILE / 2, "+" + reward.getXp() + "xp", new Color(0, 0, 0.9f), 0.2f, 50, -0.15f);
            }
            if (reward.getItem() != null) {
                mGUIManager.showReward(reward, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        onActionExecuted.onActionDone(true);
                    }
                });
            } else {
                onActionExecuted.onActionDone(true);
            }
        } else {
            onActionExecuted.onActionDone(true);
        }

    }

}
