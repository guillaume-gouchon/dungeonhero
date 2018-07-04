package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.BaseDoubleValueChangeModifier;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:27:48 - 10.08.2011
 */
abstract class DoubleValueChangeEntityModifier extends BaseDoubleValueChangeModifier<IEntity> implements IEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	DoubleValueChangeEntityModifier(final float pDuration, final float pValueChangeA, final float pValueChangeB) {
		super(pDuration, pValueChangeA, pValueChangeB);
	}

	DoubleValueChangeEntityModifier(final float pDuration, final float pValueChangeA, final float pValueChangeB, final IEntityModifierListener pModifierListener) {
		super(pDuration, pValueChangeA, pValueChangeB, pModifierListener);
	}

	DoubleValueChangeEntityModifier(final DoubleValueChangeEntityModifier pDoubleValueChangeEntityModifier) {
		super(pDoubleValueChangeEntityModifier);
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
