package com.glevel.dungeonhero.game.base.interfaces;

public interface UserActionListener {

    void onMove(float x, float y);

    void onTap(float x, float y);

    void onCancel(float x, float y);

    void onPinchZoom(float zoomFactor);

}
