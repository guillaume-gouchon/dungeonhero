package org.andengine.ui;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:03:08 - 14.03.2010
 */
public interface IGameInterface {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	EngineOptions onCreateEngineOptions();
	Engine onCreateEngine(final EngineOptions pEngineOptions);

	void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback);
	void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws Exception;
	void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception;

	void onReloadResources();
	void onDestroyResources() throws Exception;

	void onGameCreated();
	void onResumeGame();
	void onPauseGame();
	void onGameDestroyed();

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	interface OnCreateResourcesCallback {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		void onCreateResourcesFinished();
	}

	interface OnCreateSceneCallback {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		void onCreateSceneFinished(final Scene pScene);
	}

	interface OnPopulateSceneCallback {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		void onPopulateSceneFinished();
	}
}
