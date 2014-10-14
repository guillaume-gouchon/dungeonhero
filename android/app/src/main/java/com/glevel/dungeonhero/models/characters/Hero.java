package com.glevel.dungeonhero.models.characters;


import com.glevel.dungeonhero.models.FightResult;
import com.glevel.dungeonhero.utils.billing.InAppProduct;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Hero extends Unit implements InAppProduct {

    private static final long serialVersionUID = -2887616275513777101L;

    protected boolean mHasBeenBought = false;

    private final String productId;
    private int xp;
    private int level;
    protected int frags = 0;

    public Hero(Ranks ranks, int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int currentAttack, int block, int currentBlock, int name, int description, int coins, String productId, int xp, int level) {
        super(ranks, image, spriteName, hp, currentHP, strength, dexterity, spirit, attack, currentAttack, block, currentBlock, name, description, coins);
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

    public void setFrags(int frags) {
        this.frags = frags;
    }

    public int getFrags() {
        return frags;
    }

    public void addGold(int goldAmount) {
        gold += goldAmount;
    }

    public void addXP(int xpAmount) {
        xp += xpAmount;
        checkLevelChange();
    }

    private void checkLevelChange() {
        // TODO
        if (xp > 1000) {
            xp -= 1000;
            level++;
        }
    }

    @Override
    public FightResult attack(Unit target) {
        FightResult fightResult =  super.attack(target);
        if (target.isDead()) {
            frags++;
        }
        return fightResult;
    }
}
