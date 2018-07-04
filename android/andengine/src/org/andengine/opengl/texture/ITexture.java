package org.andengine.opengl.texture;

import java.io.IOException;

import org.andengine.opengl.util.GLState;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:01:03 - 11.07.2011
 */
public interface ITexture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	int getWidth();
	int getHeight();

	int getHardwareTextureID();

	boolean isLoadedToHardware();
	void setNotLoadedToHardware();

	boolean isUpdateOnHardwareNeeded();
	void setUpdateOnHardwareNeeded(final boolean pUpdateOnHardwareNeeded);

	/**
	 * @see {@link TextureManager#loadTexture(ITexture)}.
	 */
    void load();
	/**
	 * @see {@link TextureManager#loadTexture(GLState, ITexture)}.
	 */
    void load(final GLState pGLState) throws IOException;
	/**
	 * @see {@link TextureManager#unloadTexture(ITexture)}.
	 */
    void unload();
	/**
	 * @see {@link TextureManager#unloadTexture(GLState, ITexture)}.
	 */
    void unload(final GLState pGLState);

	void loadToHardware(final GLState pGLState) throws IOException;
	void unloadFromHardware(final GLState pGLState);
	void reloadToHardware(final GLState pGLState) throws IOException;

	void bind(final GLState pGLState);
	/**
	 * @param pGLActiveTexture from {@link GLES20#GL_TEXTURE0} to {@link GLES20#GL_TEXTURE31}. 
	 */
    void bind(final GLState pGLState, final int pGLActiveTexture);

	PixelFormat getPixelFormat();
	TextureOptions getTextureOptions();

	boolean hasTextureStateListener();
	ITextureStateListener getTextureStateListener();
	void setTextureStateListener(final ITextureStateListener pTextureStateListener);

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
}