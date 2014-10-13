package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.models.dungeons.Tile;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;
import com.glevel.dungeonhero.utils.pathfinding.MovingElement;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/2/14.
 */
public abstract class Unit extends GameElement implements MovingElement<Tile>, Serializable {

    private static final long serialVersionUID = 1683650465351973413L;

    // Images
    private final int image;

    // Characteristics
    private int hp;
    private int currentHP;

    private int strength;
    private int dexterity;
    private int spirit;

    private int attack;
    private int currentAttack;

    private int block;
    private int currentBlock;

    // RP
    private final int description;

    // Possessions
    protected int gold;
    protected final List<Item> items = new ArrayList<Item>();

    // SKills
    private final List<PassiveSkill> passive = new ArrayList<PassiveSkill>();
    private final List<ActiveSkill> active = new ArrayList<ActiveSkill>();

    public Unit(Ranks rank, int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int currentAttack, int block, int currentBlock, int name, int description, int coins) {
        super(name, spriteName, rank);
        this.image = image;
        this.hp = hp;
        this.currentHP = currentHP;
        this.strength = strength;
        this.dexterity = dexterity;
        this.spirit = spirit;
        this.attack = attack;
        this.currentAttack = currentAttack;
        this.block = block;
        this.currentBlock = currentBlock;
        this.description = description;
        this.gold = coins;
    }

    public int getImage() {
        return image;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getSpirit() {
        return spirit;
    }

    public void setSpirit(int spirit) {
        this.spirit = spirit;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public void setCurrentAttack(int currentAttack) {
        this.currentAttack = currentAttack;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(int currentBlock) {
        this.currentBlock = currentBlock;
    }

    public int getDescription() {
        return description;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<PassiveSkill> getPassive() {
        return passive;
    }

    public List<ActiveSkill> getActive() {
        return active;
    }

    @Override
    public boolean canMoveIn(Tile tile) {
        return tile.getGround() != null && tile.getContent() == null && (tile.getTerrain() == null || !tile.getTerrain().isBlocking());
    }

    public int getMovement() {
        return 4;
    }

    @Override
    public void createSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        sprite = new UnitSprite(this, vertexBufferObjectManager);
    }

    public float getLifeRatio() {
        return (float) currentHP / (float) hp;
    }

}
