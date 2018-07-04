package org.andengine.entity.sprite;

import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:59:57 - 03.05.2012
 */
public interface IAnimationData {
	// ===========================================================
	// Constants
	// ===========================================================

	int LOOP_CONTINUOUS = -1;

	// ===========================================================
	// Methods
	// ===========================================================
	
	int[] getFrames();
	long[] getFrameDurations();
	int getLoopCount();
	int getFrameCount();
	int getFirstFrameIndex();
	/**
	 * @return in milliseconds.
	 */
    long getAnimationDuration();

	int calculateCurrentFrameIndex(final long pAnimationProgress);

	void set(final long pFrameDurationEach, final int pFrameCount);
	void set(final long pFrameDurationEach, final int pFrameCount, final boolean pLoop);
	void set(final long pFrameDurationEach, final int pFrameCount, final int pLoopCount);

	void set(final long[] pFrameDurations);
	void set(final long[] pFrameDurations, final boolean pLoop);
	void set(final long[] pFrameDurations, final int pLoopCount);

	void set(final long[] pFrameDurations, final int pFirstFrameIndex, final int pLastFrameIndex);
	void set(final long[] pFrameDurations, final int pFirstFrameIndex, final int pLastFrameIndex, final boolean pLoop);
	/**
	 * @param pFrameDurations must have the same length as pFirstFrameIndex to pLastFrameIndex.
	 * @param pFirstFrameIndex
	 * @param pLastFrameIndex
	 * @param pLoopCount
	 */
    void set(final long[] pFrameDurations, final int pFirstFrameIndex, final int pLastFrameIndex, final int pLoopCount);

	/**
	 * Animate specifics frames.
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 */
    void set(final long[] pFrameDurations, final int[] pFrames);
	/**
	 * Animate specifics frames.
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoop
	 */
    void set(final long[] pFrameDurations, final int[] pFrames, final boolean pLoop);
	/**
	 * Animate specifics frames.
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoopCount
	 */
    void set(final long[] pFrameDurations, final int[] pFrames, final int pLoopCount);

	void set(final IAnimationData pAnimationData);

	IAnimationData deepCopy() throws DeepCopyNotSupportedException;
}
