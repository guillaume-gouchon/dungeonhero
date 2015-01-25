package com.glevel.dungeonhero.game.base.interfaces;

/**
 * Created by guillaume on 10/9/14.
 */
public interface UserActionListener {

    public void onMove(float x, float y);

    public void onTap(float x, float y);

    public void onCancel(float x, float y);

    public void onPinchZoom(float zoomFactor);

}
