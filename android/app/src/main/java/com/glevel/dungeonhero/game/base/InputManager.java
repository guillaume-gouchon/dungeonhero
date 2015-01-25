package com.glevel.dungeonhero.game.base;

import android.util.Log;
import android.view.MotionEvent;

import com.glevel.dungeonhero.game.base.interfaces.UserActionListener;

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

    private static final String TAG = "InputManager";

    private static final int DRAG_MINIMUM_DISTANCE = 500;// in pixels ^2
    private static final int AUTO_SCROLLING_THRESHOLD = 20;// in pixels
    private static final int AUTO_SCROLLING_SPEED = 2;// in pixels

    private final ZoomCamera mCamera;
    private final UserActionListener mUserActionListener;
    private final SurfaceScrollDetector mScrollDetector;
    private final PinchZoomDetector mPinchZoomDetector;

    private float mPinchZoomStartedCameraZoomFactor;
    private boolean mIsDragged = false;

    private float mLastX;
    private float mLastY;

    private boolean mIsEnabled = true;

    public InputManager(ZoomCamera camera, UserActionListener userActionListener) {
        mCamera = camera;
        mScrollDetector = new SurfaceScrollDetector(this);
        mPinchZoomDetector = new PinchZoomDetector(this);
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
                        mUserActionListener.onMove(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mUserActionListener.onMove(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
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
            } else {
                Log.d(TAG, "input is disabled");
            }
        }

        return true;
    }

    public void checkAutoScrolling(float x, float y) {
        float px = 0;
        float py = 0;
        if (x < mCamera.getCenterX() - AUTO_SCROLLING_THRESHOLD) {
            px = -1;
        } else if (x > mCamera.getCenterX() + AUTO_SCROLLING_THRESHOLD) {
            px = 1;
        }
        if (y > mCamera.getCenterY() + AUTO_SCROLLING_THRESHOLD) {
            py = 1;
        } else if (y < mCamera.getCenterY() - AUTO_SCROLLING_THRESHOLD) {
            py = -1;
        }
        mCamera.offsetCenter(px * AUTO_SCROLLING_SPEED, py * AUTO_SCROLLING_SPEED);
    }

    public void setEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
    }

    public boolean ismIsEnabled() {
        return mIsEnabled;
    }

}
