package com.glevel.dungeonhero.game.base.interfaces;

import com.glevel.dungeonhero.game.models.GameElement;

/**
 * Created by guillaume on 10/9/14.
 */
public interface OnUserActionDetected {

    public void onElementSelected(GameElement gameElement);

    public void onElementUnselected();

    public void onTouch(float x, float y);

}
