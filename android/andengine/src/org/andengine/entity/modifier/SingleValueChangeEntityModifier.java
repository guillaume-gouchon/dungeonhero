package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.BaseSingleValueChangeModifier;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:34:35 - 17.06.2010
 */
abstract class SingleValueChangeEntityModifier extends BaseSingleValueChangeModifier<IEntity> implements IEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	SingleValueChangeEntityModifier(final float pDuration, final float pValueChange) {
		super(pDuration, pValueChange);
	}

	SingleValueChangeEntityModifier(final float pDuration, final float pValueChange, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pValueChange, pEntityModifierListener);
	}

	SingleValueChangeEntityModifier(final SingleValueChangeEntityModifier pSingleValueChangeEntityModifier) {
		super(pSingleValueChangeEntityModifier);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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
