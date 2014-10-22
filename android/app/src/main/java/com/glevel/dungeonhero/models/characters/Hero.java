package com.glevel.dungeonhero.models.characters;


import com.glevel.dungeonhero.models.FightResult;
import com.glevel.dungeonhero.models.items.Equipment;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.utils.billing.InAppProduct;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Hero extends Unit implements InAppProduct, Cloneable {

    private static final long serialVersionUID = -2887616275513777101L;

    private final String productId;
    protected boolean mHasBeenBought = false;
    protected int frags = 0;
    private int xp;
    private int level;
    private String heroName;

    public Hero(Ranks ranks, int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int block, int movement, int name, int description, int coins, String productId, int xp, int level) {
        super(ranks, image, spriteName, hp, currentHP, strength, dexterity, spirit, attack, block, movement, name, description, coins);
        this.productId = productId;
        this.xp = xp;
        this.level = level;
    }

    @Override
    public String getProductId() {
        return productId;
    }

    @Override
    public void setHasBeenBought(boolean hasBeenBought) {
        mHasBeenBought = hasBeenBought;
    }

    @Override
    public boolean isAvailable() {
        return isFree() || mHasBeenBought;
    }

    @Override
    public boolean isFree() {
        return productId == null;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getFrags() {
        return frags;
    }

    public void setFrags(int frags) {
        this.frags = frags;
    }

    public void addGold(int goldAmount) {
        gold += goldAmount;
    }

    public Equipment[] getEquipments() {
        return equipments;
    }

    public boolean addXP(int xpAmount) {
        xp += xpAmount;
        if (xp >= getNextLevelXPAmount()) {
            increaseLevel();
            return true;
        }
        return false;
    }

    private void increaseLevel() {
        level++;
        // TODO : increase hp, add skills, increase attack or defense
    }

    public int getNextLevelXPAmount() {
        return (int) (100 * (Math.pow(2, level + 1) - 1));
    }

    @Override
    public FightResult attack(Unit target) {
        FightResult fightResult = super.attack(target);
        if (target.isDead()) {
            frags++;
        }
        return fightResult;
    }

    public void drop(Item item) {
        items.remove(item);
    }

    public Hero clone() {
        try {
            return (Hero) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

}
