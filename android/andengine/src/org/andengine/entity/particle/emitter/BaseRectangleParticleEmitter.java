package org.andengine.entity.particle.emitter;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:53:18 - 01.10.2010
 */
abstract class BaseRectangleParticleEmitter extends BaseParticleEmitter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	float mWidth;
	float mHeight;
	float mWidthHalf;
	float mHeightHalf;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseRectangleParticleEmitter(final float pCenterX, final float pCenterY, final float pSize) {
		this(pCenterX, pCenterY, pSize, pSize);
	}

	BaseRectangleParticleEmitter(final float pCenterX, final float pCenterY, final float pWidth, final float pHeight) {
		super(pCenterX, pCenterY);
		this.setWidth(pWidth);
		this.setHeight(pHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getWidth() {
		return this.mWidth;
	}

	private void setWidth(final float pWidth) {
		this.mWidth = pWidth;
		this.mWidthHalf = pWidth * 0.5f;
	}

	public float getHeight() {
		return this.mHeight;
	}

	private void setHeight(final float pHeight) {
		this.mHeight = pHeight;
		this.mHeightHalf = pHeight * 0.5f;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
