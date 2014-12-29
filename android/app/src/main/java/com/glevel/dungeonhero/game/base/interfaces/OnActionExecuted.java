package com.glevel.dungeonhero.game.base.interfaces;

import java.io.Serializable;

/**
 * Created by guillaume on 10/9/14.
 */
public interface OnActionExecuted extends Serializable {

    public void onActionDone(boolean success);

}
