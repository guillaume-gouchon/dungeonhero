package org.andengine.input.touch.controller;

import org.andengine.engine.handler.IUpdateHandler;

import android.view.MotionEvent;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:23:45 - 13.07.2010
 */
public interface ITouchController extends IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	void setTouchEventCallback(final ITouchEventCallback pTouchEventCallback);

	void onHandleMotionEvent(final MotionEvent pMotionEvent);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
