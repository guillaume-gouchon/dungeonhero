package org.andengine.engine.options;

/** 
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:13:07 - 22.11.2011
 */
public class AudioOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SoundOptions mSoundOptions = new SoundOptions();
	private final MusicOptions mMusicOptions = new MusicOptions();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public SoundOptions getSoundOptions() {
		return this.mSoundOptions;
	}

	public MusicOptions getMusicOptions() {
		return this.mMusicOptions;
	}

	public boolean needsSound() {
		return this.mSoundOptions.needsSound();
	}

	public void setNeedsSound(final boolean pNeedsSound) {
		this.mSoundOptions.setNeedsSound(pNeedsSound);
	}

	public boolean needsMusic() {
		return this.mMusicOptions.needsMusic();
	}

	public AudioOptions setNeedsMusic(final boolean pNeedsMusic) {
		this.mMusicOptions.setNeedsMusic(pNeedsMusic);
		return this;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
