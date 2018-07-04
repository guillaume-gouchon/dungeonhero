package org.andengine.audio;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:02:06 - 13.06.2010
 */
public interface IAudioManager<T extends IAudioEntity> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	float getMasterVolume();
	void setMasterVolume(final float pMasterVolume);

	void add(final T pAudioEntity);
	boolean remove(final T pAudioEntity);

	void releaseAll();
}
