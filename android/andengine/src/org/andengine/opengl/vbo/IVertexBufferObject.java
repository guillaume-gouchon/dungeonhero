package org.andengine.opengl.vbo;

import java.nio.ByteBuffer;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.GLState;
import org.andengine.util.IDisposable;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:32:10 - 15.11.2011
 */
public interface IVertexBufferObject extends IDisposable {
	// ===========================================================
	// Constants
	// ===========================================================

	int HARDWARE_BUFFER_ID_INVALID = -1;

	// ===========================================================
	// Methods
	// ===========================================================

	boolean isAutoDispose();

	int getHardwareBufferID();

	boolean isLoadedToHardware();
	/**
	 * Mark this {@link VertexBufferObject} as not not loaded to hardware.
	 * It will reload itself to hardware when it gets used again.
	 */
    void setNotLoadedToHardware();
	void unloadFromHardware(final GLState pGLState);

	boolean isDirtyOnHardware();
	/** Mark this {@link VertexBufferObject} dirty so it gets updated on the hardware. */
    void setDirtyOnHardware();

	/**
	 * @return the number of <code>float</code>s that fit into this {@link IVertexBufferObject}.
	 */
    int getCapacity();
	/**
	 * @return the number of <code>byte</code>s that fit into this {@link IVertexBufferObject}.
	 */
    int getByteCapacity();

	/**
	 * @return the number of <code>byte</code>s that are allocated on the heap.
	 */
    int getHeapMemoryByteSize();
	/**
	 * @return the number of <code>byte</code>s that are allocated on the native heap (through direct {@link ByteBuffer}s).
	 */
    int getNativeHeapMemoryByteSize();
	/**
	 * @return the number of <code>byte</code>s that are allocated on the GPU.
	 */
    int getGPUMemoryByteSize();

	void bind(final GLState pGLState);
	void bind(final GLState pGLState, final ShaderProgram pShaderProgram);
	void unbind(final GLState pGLState, final ShaderProgram pShaderProgram);

	VertexBufferObjectManager getVertexBufferObjectManager();

	void draw(final int pPrimitiveType, final int pCount);
	void draw(final int pPrimitiveType, final int pOffset, final int pCount);
}
