package org.andengine.extension.tmx.util.exception;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:20:25 - 08.08.2010
 */
abstract class TMXException extends Exception {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 337819550394833109L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	TMXException() {
		super();
	}

	TMXException(final String pDetailMessage, final Throwable pThrowable) {
		super(pDetailMessage, pThrowable);
	}

	TMXException(final String pDetailMessage) {
		super(pDetailMessage);
	}

	TMXException(final Throwable pThrowable) {
		super(pThrowable);
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
