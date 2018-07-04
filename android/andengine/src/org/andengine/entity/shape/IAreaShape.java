package org.andengine.entity.shape;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:01:16 - 07.08.2011
 */
public interface IAreaShape extends IShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	float getWidth();
	float getHeight();

	float getWidthScaled();
	float getHeightScaled();

	void setHeight(final float pHeight);
	void setWidth(final float pWidth);
	void setSize(final float pWidth, final float pHeight);
}
