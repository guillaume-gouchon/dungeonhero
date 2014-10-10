package com.glevel.dungeonhero.activities;

import android.os.Bundle;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.data.HeroFactory;
import com.glevel.dungeonhero.game.base.CustomGameActivity;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.graphics.ActionTile;
import com.glevel.dungeonhero.game.graphics.SelectionCircle;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.models.Actions;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.dungeons.Directions;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Room;
import com.glevel.dungeonhero.models.dungeons.Tile;
import com.glevel.dungeonhero.models.dungeons.decorations.Searchable;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.utils.pathfinding.AStar;
import com.glevel.dungeonhero.utils.pathfinding.MathUtils;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.texture.TextureOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameActivity extends CustomGameActivity {

    public static final String EXTRA_GAME_ID = "game_id";

    private Dungeon mDungeon;
    private Room mRoom;

    private Unit mActiveCharacter;


    private SelectionCircle mSelectionCircle;
    private Entity mGroundLayer;
    private TMXTiledMap mTmxTiledMap;
    private GameElement mSelectedElement;

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
        mTmxTiledMap = tmxLoader.loadFromAsset("tmx/" + mRoom.getTmxName() + ".tmx");

        mRoom.initRoom(mTmxTiledMap, mGame.getHero(), mDungeon);

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

        nextTurn();
    }

    private void nextTurn() {
        hideActionTiles();
        hideElementInfo();
        mActiveCharacter = mRoom.getQueue().get(0);
        mRoom.getQueue().add(mRoom.getQueue().get(0));
        mRoom.getQueue().remove(0);
        mGUIManager.updateQueue(mActiveCharacter, mRoom.getQueue());
        showMovement();

        if (mActiveCharacter instanceof Monster) {
            mInputManager.setEnabled(false);
            attack(mGame.getHero().getTilePosition());
        }
    }

    public void addElementToScene(GameElement gameElement) {
        gameElement.createSprite(getVertexBufferObjectManager());
        super.addElementToScene(gameElement.getSprite(), false);
    }

    public void removeElement(GameElement gameElement) {
        gameElement.setTilePosition(null);
        mRoom.getObjects().remove(gameElement);
        // TODO
        if (gameElement instanceof Unit) {
            mRoom.getQueue().remove(gameElement);
        }
        super.removeElement(gameElement.getSprite(), false);
    }

    @Override
    public void endGame() {
        super.endGame();
    }

    @Override
    public void onTouch(float x, float y) {
        Tile tile = getTileAtCoordinates(x, y);
        if (tile != null && tile.getAction() != null && tile.getAction() != Actions.MOVE) {
            selectTile(tile);
        }
    }

    @Override
    public void onCancel(float x, float y) {
        selectTile(null);
    }

    @Override
    public void onTap(float x, float y) {
        Tile tile = getTileAtCoordinates(x, y);
        if (tile != null) {
            if (tile.getContent() != null && tile.getContent() != mSelectedElement && tile.getContent().getRank() != Ranks.ME) {
                showElementInfo(tile.getContent());
            } else if (tile.getAction() != null) {
                executeAction(tile);
            } else {
                hideElementInfo();
            }
        }
    }

    private void executeAction(Tile tile) {
        hideElementInfo();
        mInputManager.setEnabled(false);
        switch (tile.getAction()) {
            case MOVE:
                if (tile == mSelectedTile) {
                    move(tile);
                } else {
                    selectTile(tile);
                    mInputManager.setEnabled(true);
                }
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
        List<Tile> path = new AStar<Tile>().search(mRoom.getTiles(), mActiveCharacter.getTilePosition(), tile, false, mActiveCharacter);
        walkTo(path, new Runnable() {
            @Override
            public void run() {
                mInputManager.setEnabled(true);
                nextTurn();
            }
        });
    }

    private void attack(final Tile tile) {
        goCloserTo(tile, new Runnable() {
            @Override
            public void run() {
                // TODO
                Unit unit = (Unit) tile.getContent();
                unit.setCurrentHP(unit.getCurrentHP() - 5);
                if (unit != mGame.getHero() && unit.getCurrentHP() <= 0) {
                    removeElement(unit);
                    mGUIManager.updateQueue(mActiveCharacter, mRoom.getQueue());
                }
                mInputManager.setEnabled(true);
                nextTurn();
            }
        });
    }

    private void search(final Tile tile) {
        goCloserTo(tile, new Runnable() {
            @Override
            public void run() {
                // TODO
                Searchable searchable = (Searchable) tile.getContent();
                Item foundItem = searchable.search();
                mInputManager.setEnabled(true);
                nextTurn();
            }
        });
    }

    private void talk(Tile tile) {
        goCloserTo(tile, new Runnable() {
            @Override
            public void run() {
                // TODO
                mInputManager.setEnabled(true);
                nextTurn();
            }
        });
    }

    private void goCloserTo(Tile tile, Runnable runnable) {
        if (MathUtils.calcManhattanDistance(mActiveCharacter.getTilePosition(), tile) == 1) {
            runnable.run();
            return;
        }

        Tile closestTile = null;
        Set<Tile> adjacentTiles = MathUtils.getAdjacentNodes(mRoom.getTiles(), tile, 1, false, mActiveCharacter);
        for (Tile adjacent : adjacentTiles) {
            if (adjacent.getAction() == Actions.MOVE) {
                closestTile = adjacent;
                break;
            }
        }

        if (closestTile != null) {
            List<Tile> path = new AStar<Tile>().search(mRoom.getTiles(), mActiveCharacter.getTilePosition(), closestTile, false, mActiveCharacter);
            walkTo(path, runnable);
        } else {
            runnable.run();
        }
    }

    private Tile mSelectedTile = null;

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
        TMXTile tmxTile = mTmxTiledMap.getTMXLayers().get(0).getTMXTileAt(x, y);
        if (tmxTile != null) {
            return mRoom.getTiles()[tmxTile.getTileRow()][tmxTile.getTileColumn()];
        } else {
            return null;
        }
    }

    private void addAvailableAction(GameElement gameElement) {
        boolean isActionPossible = MathUtils.calcManhattanDistance(mActiveCharacter.getTilePosition(), gameElement.getTilePosition()) == 1;
        if (!isActionPossible) {
            Set<Tile> adjacentTiles = MathUtils.getAdjacentNodes(mRoom.getTiles(), gameElement.getTilePosition(), 1, false, mActiveCharacter);
            for (Tile adjacent : adjacentTiles) {
                if (adjacent.getAction() == Actions.MOVE) {
                    isActionPossible = true;
                    break;
                }
            }
        }

        if (isActionPossible) {
            if (mActiveCharacter.isEnemy(gameElement)) {
                addActionToTile(Actions.ATTACK, gameElement.getTilePosition());
            } else if (gameElement instanceof Pnj) {
                addActionToTile(Actions.TALK, gameElement.getTilePosition());
            } else if (gameElement instanceof Searchable) {
                addActionToTile(Actions.SEARCH, gameElement.getTilePosition());
            }
        }
    }

    private void addActionToTile(Actions action, Tile tile) {
        ActionTile actionTile = new ActionTile(action, tile, getVertexBufferObjectManager());
        tile.setAction(action);
        mGroundLayer.attachChild(actionTile);
    }


    private void showElementInfo(GameElement gameElement) {
        mSelectedElement = gameElement;
        mSelectionCircle.attachToGameElement(gameElement);
        mGUIManager.showGameElementInfo(gameElement);
    }

    private void hideElementInfo() {
        mSelectedElement = null;
        mSelectionCircle.unAttach();
        mGUIManager.hideGameElementInfo();
    }


    private void showMovement() {
        // get reachable tiles
        Set<Tile> reachableTiles = new HashSet<Tile>();
        Tile t;
        for (Tile[] hTile : mRoom.getTiles()) {
            for (Tile tile : hTile) {
                if (!reachableTiles.contains(tile) && MathUtils.calcManhattanDistance(tile, mActiveCharacter.getTilePosition()) <= mActiveCharacter.getMovement()
                        && mActiveCharacter.canMoveIn(tile)) {
                    List<Tile> path = new AStar<Tile>().search(mRoom.getTiles(), mActiveCharacter.getTilePosition(), tile, false, mActiveCharacter);
                    if (path != null) {
                        for (int n = 0; n < path.size(); n++) {
                            t = path.get(n);
                            if (n <= mActiveCharacter.getMovement()) {
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
            c = new ActionTile(Actions.MOVE, tile, getVertexBufferObjectManager());
            mGroundLayer.attachChild(c);
        }

        for (GameElement gameElement : mRoom.getObjects()) {
            if (mActiveCharacter != gameElement && MathUtils.calcManhattanDistance(gameElement.getTilePosition(), mActiveCharacter.getTilePosition()) <= mActiveCharacter.getMovement() + 1) {
                addAvailableAction(gameElement);
            }
        }
    }

    private void hideActionTiles() {
        mSelectedTile = null;
        ActionTile actionTile;
        for (int n = 0; n < mGroundLayer.getChildCount(); n++) {
            actionTile = (ActionTile) mGroundLayer.getChildByIndex(n);
            actionTile.getTile().setAction(null);
            actionTile.getTile().setSelected(false);
        }
        mGroundLayer.detachChildren();
    }

    TimerHandler moveHandler;

    private void walkTo(List<Tile> path, final Runnable callback) {
        if (path.size() > 1) {
            path.remove(0);
            final UnitSprite sprite = (UnitSprite) mActiveCharacter.getSprite();
            final Tile nextTile = path.get(0);
            final Directions direction = Directions.from(nextTile.getX() - mActiveCharacter.getTilePosition().getX(), mActiveCharacter.getTilePosition().getY() - nextTile.getY());
            sprite.walk(direction);
            final List<Tile> p = new ArrayList<Tile>(path);
            moveHandler = new TimerHandler(1.0f / 40, true, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    sprite.setPosition(sprite.getX() + direction.getDx(), sprite.getY() - direction.getDy());
                    if (direction == Directions.EAST && sprite.getX() >= nextTile.getTileX()
                            || direction == Directions.WEST && sprite.getX() <= nextTile.getTileX()
                            || direction == Directions.SOUTH && sprite.getY() >= nextTile.getTileY()
                            || direction == Directions.NORTH && sprite.getY() <= nextTile.getTileY()) {
                        mScene.unregisterUpdateHandler(moveHandler);
                        mActiveCharacter.setTilePosition(nextTile);
                        sprite.setPosition(nextTile.getTileX(), nextTile.getTileY());

                        mScene.sortChildren(true);
                        walkTo(p, callback);
                    }
                }
            });
            mScene.registerUpdateHandler(moveHandler);
        } else {
            UnitSprite sprite = (UnitSprite) mActiveCharacter.getSprite();
            sprite.stand();
            callback.run();
        }
    }

}
