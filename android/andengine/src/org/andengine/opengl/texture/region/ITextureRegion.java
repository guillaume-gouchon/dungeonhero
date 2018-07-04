package org.andengine.opengl.texture.region;

import org.andengine.opengl.texture.ITexture;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:07:17 - 07.08.2011
 */
public interface ITextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	float getTextureX();
	float getTextureY();

	void setTextureX(final float pTextureX);
	void setTextureY(final float pTextureY);
	void setTexturePosition(final float pTextureX, final float pTextureY);

	/**
	 * Note: Takes {@link ITextureRegion#getScale()} into account!
	 */
    float getWidth();
	/**
	 * Note: Takes {@link ITextureRegion#getScale()} into account!
	 */
    float getHeight();

	void setTextureWidth(final float pTextureWidth);
	void setTextureHeight(final float pTextureHeight);
	void setTextureSize(final float pTextureWidth, final float pTextureHeight);

	void set(final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight);

	float getU();
	float getU2();
	float getV();
	float getV2();

	boolean isScaled();
	float getScale();
	boolean isRotated();

	ITexture getTexture();

	ITextureRegion deepCopy();
}