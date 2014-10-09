package com.glevel.dungeonhero.activities;

import android.os.Bundle;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.data.HeroFactory;
import com.glevel.dungeonhero.game.base.CustomGameActivity;
import com.glevel.dungeonhero.game.graphics.ActionTile;
import com.glevel.dungeonhero.game.graphics.GameElementSprite;
import com.glevel.dungeonhero.game.graphics.SelectionCircle;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.dungeons.Directions;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Room;
import com.glevel.dungeonhero.models.dungeons.Tile;
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


    private SelectionCircle selectionCircle;
    private Entity mGroundLayer;
    private TMXTiledMap mTmxTiledMap;

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
        mRoom.initRoom(mTmxTiledMap);

        // TODO : place hero
        mRoom.getDoors().get(Directions.NORTH).setContent(mGame.getHero());

        mRoom.getTiles()[5][5].setContent(HeroFactory.buildWarrior());
        mRoom.getTiles()[5][6].setContent(HeroFactory.buildWarrior());
        mRoom.getTiles()[5][7].setContent(HeroFactory.buildWarrior());
        mRoom.getTiles()[5][8].setContent(HeroFactory.buildWarrior());
        mRoom.getTiles()[5][9].setContent(HeroFactory.buildWarrior());


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

        selectionCircle = new SelectionCircle(getVertexBufferObjectManager());
        selectionCircle.setZIndex(5);
        mScene.attachChild(selectionCircle);

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
        mActiveCharacter = mGame.getHero();
        showMovement();
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
                    for (int n = 1; n < path.size(); n++) {
                        t = path.get(n);
                        if (n <= mActiveCharacter.getMovement()) {
                            reachableTiles.add(t);
                        }
                    }
                }
            }
        }

        ActionTile c;
        for (Tile tile : reachableTiles) {
            tile.setAction(1);
            c = new ActionTile(tile, getVertexBufferObjectManager());
            mGroundLayer.attachChild(c);
        }
    }

    private void hideMovement() {
        ActionTile actionTile;
        for (int n = 0; n < mGroundLayer.getChildCount(); n++) {
            actionTile = (ActionTile) mGroundLayer.getChildByIndex(n);
            actionTile.getTile().setAction(0);
        }
        mGroundLayer.detachChildren();
    }

    public void addElementToScene(GameElement gameElement) {
        // create sprite
        GameElementSprite sprite = null;
        if (gameElement instanceof Unit) {
            sprite = new UnitSprite(gameElement, getVertexBufferObjectManager(), mInputManager);
            sprite.setZIndex(10);
        }

        super.addElementToScene(sprite, true);
    }

    @Override
    public void endGame() {
        super.endGame();
    }

    @Override
    public void onElementSelected(GameElement gameElement) {
        selectionCircle.attachToGameElement(gameElement);
        mGUIManager.showGameElementInfo(gameElement);
    }

    @Override
    public void onElementUnselected() {
        selectionCircle.unAttach();
        mGUIManager.hideGameElementInfo();
    }

    @Override
    public void onTouch(float x, float y) {
        Tile tile = getTileAtCoordinates(x, y);
        if (tile != null && tile.getAction() == 1) {
            mInputManager.setEnabled(false);
            onElementUnselected();
            hideMovement();

            List<Tile> path = new AStar<Tile>().search(mRoom.getTiles(), mActiveCharacter.getTilePosition(), tile, false, mActiveCharacter);
            walkTo(path, new Runnable() {
                @Override
                public void run() {
                    showMovement();
                    mInputManager.setEnabled(true);
                }
            });
        } else {
            onElementUnselected();
        }
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
                        mActiveCharacter.getTilePosition().setContent(null);
                        mActiveCharacter.setTilePosition(nextTile);
                        sprite.setPosition(nextTile.getTileX(), nextTile.getTileY());
                        nextTile.setContent(mActiveCharacter);
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

    public Tile getTileAtCoordinates(float x, float y) {
        TMXTile tmxTile = mTmxTiledMap.getTMXLayers().get(0).getTMXTileAt(x, y);
        if (tmxTile != null) {
            return mRoom.getTiles()[tmxTile.getTileRow()][tmxTile.getTileColumn()];
        } else {
            return null;
        }
    }

}
