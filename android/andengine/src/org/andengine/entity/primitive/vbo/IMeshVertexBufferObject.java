package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.Mesh;
import org.andengine.opengl.vbo.IVertexBufferObject;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:46:51 - 28.03.2012
 */
public interface IMeshVertexBufferObject extends IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	float[] getBufferData();
	void onUpdateColor(final Mesh pMesh);
	void onUpdateVertices(final Mesh pMesh);
}