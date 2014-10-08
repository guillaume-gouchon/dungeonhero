package com.glevel.dungeonhero.game.andengine.custom;

import org.andengine.engine.camera.ZoomCamera;

public class CustomZoomCamera extends ZoomCamera {

    private final float mZoomMin, mZoomMax;

    public CustomZoomCamera(final float pX, final float pY, final float pWidth, final float pHeight, float zoomMin, float zoomMax) {
        super(pX, pY, pWidth, pHeight);
        mZoomMin = zoomMin;
        mZoomMax = zoomMax;
        setZoomFactor(zoomMin);
    }

    @Override
    public void setZoomFactor(final float pZoomFactor) {
        if (pZoomFactor >= mZoomMin && pZoomFactor <= mZoomMax) {
            this.mZoomFactor = pZoomFactor;

            if (this.mBoundsEnabled) {
                this.ensureInBounds();
            }
        }
    }

}
