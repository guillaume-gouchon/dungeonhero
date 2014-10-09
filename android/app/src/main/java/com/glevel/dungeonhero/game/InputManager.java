package com.glevel.dungeonhero.game;

import android.app.Activity;
import android.graphics.Point;
import android.view.MotionEvent;

import com.glevel.dungeonhero.game.base.interfaces.OnUserActionDetected;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.utils.ApplicationUtils;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

public class InputManager implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener, OnUserActionDetected {

    private static final int AUTO_SCROLLING_EDGE_DISTANCE_THRESHOLD = 40;// in pixels
    private static final int AUTO_SCROLLING_SPEED = 5;// in pixels

    private final ZoomCamera mCamera;
    private final OnUserActionDetected mUserActionListener;
    private final SurfaceScrollDetector mScrollDetector;
    private final PinchZoomDetector mPinchZoomDetector;
    private final Point mScreenDimensions;

    private float mPinchZoomStartedCameraZoomFactor;
    private boolean mIsDragged = false;

    private float mLastX;
    private float mLastY;

    private GameElement mSelectedElement;

    private boolean mIsEnabled = true;

    public InputManager(Activity activity, ZoomCamera camera, OnUserActionDetected userActionListener) {
        mCamera = camera;
        mScrollDetector = new SurfaceScrollDetector(this);
        mPinchZoomDetector = new PinchZoomDetector(this);
        mScreenDimensions = ApplicationUtils.getScreenDimensions(activity);
        mUserActionListener = userActionListener;
    }

    public void setLastX(float lastX) {
        mLastX = mLastX;
    }

    public void setLastY(float lastY) {
        mLastY = mLastY;
    }

    /**
     * Map scrolling
     */
    @Override
    public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
        final float zoomFactor = mCamera.getZoomFactor();
        mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
        mIsDragged = true;
    }

    @Override
    public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
        final float zoomFactor = mCamera.getZoomFactor();
        mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
    }

    @Override
    public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
        final float zoomFactor = mCamera.getZoomFactor();
        mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
    }

    /**
     * Map zooming
     */
    @Override
    public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
        mPinchZoomStartedCameraZoomFactor = mCamera.getZoomFactor();
    }

    @Override
    public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
        mCamera.setZoomFactor(mPinchZoomStartedCameraZoomFactor * pZoomFactor);
    }

    @Override
    public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
        mCamera.setZoomFactor(mPinchZoomStartedCameraZoomFactor * pZoomFactor);
    }

    /**
     * Touch on map
     */
    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

        if (mPinchZoomDetector.isZooming()) {
            mScrollDetector.setEnabled(false);
        } else {
            if (pSceneTouchEvent.isActionDown()) {
                mScrollDetector.setEnabled(true);
            }
            mScrollDetector.onTouchEvent(pSceneTouchEvent);


            switch (pSceneTouchEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mIsDragged = false;
                    mLastX = pSceneTouchEvent.getX();
                    mLastY = pSceneTouchEvent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mIsDragged) {
                        onTouch(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                    }
                    mIsDragged = false;
                    break;
            }
        }

        return true;
    }

    public void checkAutoScrolling() {
        float px = 0;
        float py = 0;
        if (mLastX < AUTO_SCROLLING_EDGE_DISTANCE_THRESHOLD) {
            px = -1;
        } else if (mLastX > mScreenDimensions.x - AUTO_SCROLLING_EDGE_DISTANCE_THRESHOLD) {
            px = 1;
        }
        if (mLastY < AUTO_SCROLLING_EDGE_DISTANCE_THRESHOLD) {
            py = -1;
        } else if (mLastY > mScreenDimensions.y - AUTO_SCROLLING_EDGE_DISTANCE_THRESHOLD) {
            py = 1;
        }
        mCamera.offsetCenter(px * AUTO_SCROLLING_SPEED / mCamera.getZoomFactor(), py * AUTO_SCROLLING_SPEED / mCamera.getZoomFactor());
    }

    public GameElement getSelectedElement() {
        return mSelectedElement;
    }

    private void unSelectAll() {
        if (mSelectedElement != null) {
            onElementUnselected();
        }
    }

    public boolean isEnabled() {
        return mIsEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
    }

    @Override
    public void onElementSelected(GameElement gameElement) {
        if (mIsEnabled) {
            unSelectAll();
            mSelectedElement = gameElement;
            mUserActionListener.onElementSelected(gameElement);
        }
    }

    @Override
    public void onElementUnselected() {
        mSelectedElement = null;
        mUserActionListener.onElementUnselected();
    }

    @Override
    public void onTouch(float x, float y) {
        if (mIsEnabled) {
            mUserActionListener.onTouch(x, y);
        }
    }

}
