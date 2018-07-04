package org.andengine.entity.shape;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:32:52 - 07.07.2010
 */
public interface IShape extends IEntity, ITouchArea {
	// ===========================================================
	// Constants
	// ===========================================================

	int BLENDFUNCTION_SOURCE_DEFAULT = GLES20.GL_SRC_ALPHA;
	int BLENDFUNCTION_DESTINATION_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

	int BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE;
	int BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT = GLES20.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Methods
	// ===========================================================

	boolean collidesWith(final IShape pOtherShape);

	boolean isBlendingEnabled();
	void setBlendingEnabled(final boolean pBlendingEnabled);
	int getBlendFunctionSource();
	int getBlendFunctionDestination();
	void setBlendFunctionSource(final int pBlendFunctionSource);
	void setBlendFunctionDestination(final int pBlendFunctionDestination);
	void setBlendFunction(final int pBlendFunctionSource, final int pBlendFunctionDestination);

	VertexBufferObjectManager getVertexBufferObjectManager();
	IVertexBufferObject getVertexBufferObject();
	ShaderProgram getShaderProgram();
	void setShaderProgram(final ShaderProgram pShaderProgram);
}