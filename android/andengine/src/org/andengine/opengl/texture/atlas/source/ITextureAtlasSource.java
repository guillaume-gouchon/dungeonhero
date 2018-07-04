package org.andengine.opengl.texture.atlas.source;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:46:56 - 12.07.2011
 */
public interface ITextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	int getTextureX();
	int getTextureY();
	void setTextureX(final int pTextureX);
	void setTextureY(final int pTextureY);

	int getTextureWidth();
	int getTextureHeight();
	void setTextureWidth(final int pTextureWidth);
	void setTextureHeight(final int pTextureHeight);

	ITextureAtlasSource deepCopy();
}