package org.andengine.opengl.texture.region;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:31:52 - 08.08.2011
 */
public interface ITiledTextureRegion extends ITextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	int getCurrentTileIndex();
	void setCurrentTileIndex(final int pCurrentTileIndex);
	void nextTile();
	ITextureRegion getTextureRegion(final int pTileIndex);
	int getTileCount();

	float getTextureX(final int pTileIndex);
	float getTextureY(final int pTileIndex);

	void setTextureX(final int pTileIndex, final float pTextureX);
	void setTextureY(final int pTileIndex, final float pTextureY);
	void setTexturePosition(final int pTileIndex, final float pTextureX, final float pTextureY);

	/**
	 * Note: Takes {@link ITiledTextureRegion#getScale(int)} into account!
	 */
    float getWidth(final int pTileIndex);
	/**
	 * Note: Takes {@link ITiledTextureRegion#getScale(int)} into account!
	 */
    float getHeight(final int pTileIndex);

	void setTextureWidth(final int pTileIndex, final float pWidth);
	void setTextureHeight(final int pTileIndex, final float pHeight);
	void setTextureSize(final int pTileIndex, final float pWidth, final float pHeight);

	void set(final int pTileIndex, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight);

	float getU(final int pTileIndex);
	float getU2(final int pTileIndex);
	float getV(final int pTileIndex);
	float getV2(final int pTileIndex);

	boolean isScaled(final int pTileIndex);
	float getScale(final int pTileIndex);
	boolean isRotated(final int pTileIndex);

	@Override
    ITiledTextureRegion deepCopy();
}
