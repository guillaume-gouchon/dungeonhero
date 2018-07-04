package org.andengine.input.touch.detector;

import org.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @author Greg Haynes
 * @since 20:49:25 - 23.08.2010
 */
public class HoldDetector extends BaseDetector {
	// ===========================================================
	// Constants
	// ===========================================================

	static final long TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT = 200;
	static final float TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	long mTriggerHoldMinimumMilliseconds;
	float mTriggerHoldMaximumDistance;
	private final IHoldDetectorListener mHoldDetectorListener;

	int mPointerID = TouchEvent.INVALID_POINTER_ID;

	long mDownTimeMilliseconds = Long.MIN_VALUE;

	float mDownX;
	float mDownY;

	float mHoldX;
	float mHoldY;

	boolean mMaximumDistanceExceeded;

	boolean mTriggering;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HoldDetector(final IHoldDetectorListener pHoldDetectorListener) {
		this(HoldDetector.TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT, HoldDetector.TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT, pHoldDetectorListener);
	}

	HoldDetector(final long pTriggerHoldMinimumMilliseconds, final float pTriggerHoldMaximumDistance, final IHoldDetectorListener pHoldDetectorListener) {
		this.setTriggerHoldMinimumMilliseconds(pTriggerHoldMinimumMilliseconds);
		this.setTriggerHoldMaximumDistance(pTriggerHoldMaximumDistance);
		this.mHoldDetectorListener = pHoldDetectorListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public long getTriggerHoldMinimumMilliseconds() {
		return this.mTriggerHoldMinimumMilliseconds;
	}

	private void setTriggerHoldMinimumMilliseconds(final long pTriggerHoldMinimumMilliseconds) {
		if(pTriggerHoldMinimumMilliseconds < 0) {
			throw new IllegalArgumentException("pTriggerHoldMinimumMilliseconds must not be < 0.");
		}
		this.mTriggerHoldMinimumMilliseconds = pTriggerHoldMinimumMilliseconds;
	}

	public float getTriggerHoldMaximumDistance() {
		return this.mTriggerHoldMaximumDistance;
	}

	private void setTriggerHoldMaximumDistance(final float pTriggerHoldMaximumDistance) {
		if(pTriggerHoldMaximumDistance < 0) {
			throw new IllegalArgumentException("pTriggerHoldMaximumDistance must not be < 0.");
		}
		this.mTriggerHoldMaximumDistance = pTriggerHoldMaximumDistance;
	}

	public boolean isHolding() {
		return this.mTriggering;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * When {@link HoldDetector#isHolding()} this method will call through to {@link IHoldDetectorListener#onHoldFinished(HoldDetector, long, int, float, float).
	 */
	@Override
	public void reset() {
		if(this.mTriggering) {
			this.triggerOnHoldFinished(System.currentTimeMillis() - this.mDownTimeMilliseconds);
		}

		this.mTriggering = false;
		this.mMaximumDistanceExceeded = false;
		this.mDownTimeMilliseconds = Long.MIN_VALUE;
		this.mPointerID = TouchEvent.INVALID_POINTER_ID;
	}

	@Override
	public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();

		switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				if(this.mPointerID == TouchEvent.INVALID_POINTER_ID) {
					this.prepareHold(pSceneTouchEvent);
					return true;
				} else {
					return false;
				}
			case TouchEvent.ACTION_MOVE:
			{
				if(this.mPointerID == pSceneTouchEvent.getPointerID()) {
					this.mHoldX = pSceneTouchEvent.getX();
					this.mHoldY = pSceneTouchEvent.getY();

					final long holdTimeMilliseconds = System.currentTimeMillis() - this.mDownTimeMilliseconds;
					if(holdTimeMilliseconds >= this.mTriggerHoldMinimumMilliseconds) {
						if(this.mTriggering) {
							this.triggerOnHold(holdTimeMilliseconds);
						} else {
							final float triggerHoldMaximumDistance = this.mTriggerHoldMaximumDistance;
							this.mMaximumDistanceExceeded = this.mMaximumDistanceExceeded || Math.abs(this.mDownX - motionEvent.getX()) > triggerHoldMaximumDistance  || Math.abs(this.mDownY - motionEvent.getY()) > triggerHoldMaximumDistance;

							if(!this.mMaximumDistanceExceeded) {
								if(!this.mTriggering) {
									this.triggerOnHoldStarted();
								} else {
									this.triggerOnHold(holdTimeMilliseconds);
								}
							}
						}
					}
					return true;
				} else {
					return false;
				}
			}
			case TouchEvent.ACTION_UP:
			case TouchEvent.ACTION_CANCEL:
			{
				if(this.mPointerID == pSceneTouchEvent.getPointerID()) {
					this.mHoldX = pSceneTouchEvent.getX();
					this.mHoldY = pSceneTouchEvent.getY();

					final long holdTimeMilliseconds = System.currentTimeMillis() - this.mDownTimeMilliseconds;
					if(holdTimeMilliseconds >= this.mTriggerHoldMinimumMilliseconds) {
						if(this.mTriggering) {
							this.triggerOnHoldFinished(holdTimeMilliseconds);
						} else {
							final float triggerHoldMaximumDistance = this.mTriggerHoldMaximumDistance;
							this.mMaximumDistanceExceeded = this.mMaximumDistanceExceeded || Math.abs(this.mDownX - motionEvent.getX()) > triggerHoldMaximumDistance  || Math.abs(this.mDownY - motionEvent.getY()) > triggerHoldMaximumDistance;

							if(!this.mMaximumDistanceExceeded) {
								this.triggerOnHoldFinished(holdTimeMilliseconds);
							}
						}
					}

					this.mPointerID = TouchEvent.INVALID_POINTER_ID;
					return true;
				} else {
					return false;
				}
			}
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	void prepareHold(final TouchEvent pSceneTouchEvent) {
		final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();
		this.mDownTimeMilliseconds = System.currentTimeMillis();
		this.mDownX = motionEvent.getX();
		this.mDownY = motionEvent.getY();
		this.mMaximumDistanceExceeded = false;
		this.mPointerID = pSceneTouchEvent.getPointerID();
		this.mHoldX = pSceneTouchEvent.getX();
		this.mHoldY = pSceneTouchEvent.getY();

		if(this.mTriggerHoldMinimumMilliseconds == 0) {
			this.triggerOnHoldStarted();
		}
	}

	void triggerOnHoldStarted() {
		this.mTriggering = true;

		if(this.mPointerID != TouchEvent.INVALID_POINTER_ID) {
			this.mHoldDetectorListener.onHoldStarted(this, this.mPointerID, this.mHoldX, this.mHoldY);
		}
	}

	void triggerOnHold(final long pHoldTimeMilliseconds) {
		if(this.mPointerID != TouchEvent.INVALID_POINTER_ID) {
			this.mHoldDetectorListener.onHold(this, pHoldTimeMilliseconds, this.mPointerID, this.mHoldX, this.mHoldY);
		}
	}

	void triggerOnHoldFinished(final long pHoldTimeMilliseconds) {
		this.mTriggering = false;

		if(this.mPointerID != TouchEvent.INVALID_POINTER_ID) {
			this.mHoldDetectorListener.onHoldFinished(this, pHoldTimeMilliseconds, this.mPointerID, this.mHoldX, this.mHoldY);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	protected interface IHoldDetectorListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		void onHoldStarted(final HoldDetector pHoldDetector, final int pPointerID, final float pHoldX, final float pHoldY);
		void onHold(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final int pPointerID, final float pHoldX, final float pHoldY);
		void onHoldFinished(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final int pPointerID, final float pHoldX, final float pHoldY);
	}
}
