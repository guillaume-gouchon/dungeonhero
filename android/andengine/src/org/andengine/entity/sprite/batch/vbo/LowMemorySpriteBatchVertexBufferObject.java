package org.andengine.entity.sprite.batch.vbo;

import java.nio.FloatBuffer;

import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.LowMemoryVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:34:46 - 29.03.2012
 */
public class LowMemorySpriteBatchVertexBufferObject extends LowMemoryVertexBufferObject implements ISpriteBatchVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mBufferDataOffset;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LowMemorySpriteBatchVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getBufferDataOffset() {
		return this.mBufferDataOffset;
	}

	@Override
	public void setBufferDataOffset(final int pBufferDataOffset) {
		this.mBufferDataOffset = pBufferDataOffset;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * 1-3
	 * |X|
	 * 2-4
	 */
	@Override
	public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final float pColorABGRPackedInt) {
		final FloatBuffer bufferData = this.getFloatBuffer();
		final int bufferDataOffset = this.mBufferDataOffset;

		final float u = pTextureRegion.getU();
		final float v = pTextureRegion.getV();
		final float u2 = pTextureRegion.getU2();
		final float v2 = pTextureRegion.getV2();

		if(pTextureRegion.isRotated()) {
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_INDEX_X, pX1);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_INDEX_Y, pY1);
			bufferData.put(bufferDataOffset + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX3);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY3);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX3);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY3);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX4);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY4);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);
		} else {
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_INDEX_X, pX1);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_INDEX_Y, pY1);
			bufferData.put(bufferDataOffset + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX3);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY3);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX3);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY3);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX4);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY4);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);
		}

		this.mBufferDataOffset += SpriteBatch.SPRITE_SIZE;
	}


	/**
	 * 1-+
	 * |X|
	 * +-2
	 */
	@Override
	public void addWithPackedColor(final ITextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pColorABGRPackedInt) {
		final FloatBuffer bufferData = this.getFloatBuffer();
		final int bufferDataOffset = this.mBufferDataOffset;

		final float u = pTextureRegion.getU();
		final float v = pTextureRegion.getV();
		final float u2 = pTextureRegion.getU2();
		final float v2 = pTextureRegion.getV2();

		if(pTextureRegion.isRotated()) {
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_INDEX_X, pX1);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_INDEX_Y, pY1);
			bufferData.put(bufferDataOffset + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX1);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY1);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY1);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX1);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);
		} else {
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_INDEX_X, pX1);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_INDEX_Y, pY1);
			bufferData.put(bufferDataOffset + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX1);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY1);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 2 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY1);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 3 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v);

			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX1);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u);
			bufferData.put(bufferDataOffset + 4 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);

			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_X, pX2);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.VERTEX_INDEX_Y, pY2);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.COLOR_INDEX, pColorABGRPackedInt);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_U, u2);
			bufferData.put(bufferDataOffset + 5 * SpriteBatch.VERTEX_SIZE + SpriteBatch.TEXTURECOORDINATES_INDEX_V, v2);
		}

		this.mBufferDataOffset += SpriteBatch.SPRITE_SIZE;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}