package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;

import java.util.List;

/**
 * Created by guillaume on 10/2/14.
 */
public abstract class Unit {

    // Images
    private final int image;
    private final String spriteName;

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
    private final int name;
    private final int description;

    // Ownings
    private int coins;
    private List<Item> items;

    // SKills
    private List<PassiveSkill> passive;
    private List<ActiveSkill> active;

    public Unit(int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int currentAttack, int block, int currentBlock, int name, int description, int coins, List<Item> items, List<PassiveSkill> passive, List<ActiveSkill> active) {
        this.image = image;
        this.spriteName = spriteName;
        this.hp = hp;
        this.currentHP = currentHP;
        this.strength = strength;
        this.dexterity = dexterity;
        this.spirit = spirit;
        this.attack = attack;
        this.currentAttack = currentAttack;
        this.block = block;
        this.currentBlock = currentBlock;
        this.name = name;
        this.description = description;
        this.coins = coins;
        this.items = items;
        this.passive = passive;
        this.active = active;
    }

    public int getImage() {
        return image;
    }

    public String getSpriteName() {
        return spriteName;
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

    public int getName() {
        return name;
    }

    public int getDescription() {
        return description;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<PassiveSkill> getPassive() {
        return passive;
    }

    public void setPassive(List<PassiveSkill> passive) {
        this.passive = passive;
    }

    public List<ActiveSkill> getActive() {
        return active;
    }

    public void setActive(List<ActiveSkill> active) {
        this.active = active;
    }
}
