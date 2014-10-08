package com.glevel.dungeonhero.utils.billing;

/**
 * Created by guillaume ON 10/3/14.
 */
public interface InAppProduct {

    public String getProductId();

    public void setHasBeenBought(boolean hasBeenBought);

    public boolean isAvailable();

    public boolean isFree();

}
