package com.glevel.dungeonhero.game.base;

import android.app.Activity;
import android.graphics.Point;
import android.view.MotionEvent;

import com.glevel.dungeonhero.game.base.interfaces.UserActionListener;
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

public class InputManager implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {

    private static final int DRAG_MINIMUM_DISTANCE = 300;// in pixels ^2
    private static final int AUTO_SCROLLING_EDGE_DISTANCE_THRESHOLD = 40;// in pixels
    private static final int AUTO_SCROLLING_SPEED = 5;// in pixels

    private final ZoomCamera mCamera;
    private final UserActionListener mUserActionListener;
    private final SurfaceScrollDetector mScrollDetector;
    private final PinchZoomDetector mPinchZoomDetector;
    private final Point mScreenDimensions;

    private float mPinchZoomStartedCameraZoomFactor;
    private boolean mIsDragged = false;

    private float mLastX;
    private float mLastY;

    private boolean mIsEnabled = true;

    public InputManager(Activity activity, ZoomCamera camera, UserActionListener userActionListener) {
        mCamera = camera;
        mScrollDetector = new SurfaceScrollDetector(this);
        mPinchZoomDetector = new PinchZoomDetector(this);
        mScreenDimensions = ApplicationUtils.getScreenDimensions(activity);
        mUserActionListener = userActionListener;
    }

    /**
     * Map scrolling
     */
    @Override
    public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
        final float zoomFactor = mCamera.getZoomFactor();
        mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
        if (pDistanceX * pDistanceX + pDistanceY * pDistanceY > DRAG_MINIMUM_DISTANCE) {
            mIsDragged = true;
        }
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

            if (mIsEnabled) {
                switch (pSceneTouchEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsDragged = false;
                        mLastX = pSceneTouchEvent.getX();
                        mLastY = pSceneTouchEvent.getY();
                        mUserActionListener.onTouch(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mUserActionListener.onTouch(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mIsDragged) {
                            mUserActionListener.onTap(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                        } else {
                            mUserActionListener.onCancel(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                        }
                        mIsDragged = false;
                        break;
                }
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

    public void setEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
    }

}