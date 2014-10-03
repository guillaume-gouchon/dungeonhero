package com.glevel.dungeonhero.models.characters.heroes;


import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.utils.billing.InAppProduct;

/**
 * Created by guillaume on 10/2/14.
 */
public abstract class Hero extends Unit implements InAppProduct {

    protected boolean mHasBeenBought = false;

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
        return false;
    }

}
