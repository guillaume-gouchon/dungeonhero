package org.andengine.entity.text.exception;

import org.andengine.util.exception.AndEngineRuntimeException;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:53:31 - 01.11.2011
 */
class TextException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = -412281825916020126L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	TextException() {
		super();
	}

	TextException(final String pMessage) {
		super(pMessage);
	}

	TextException(final Throwable pThrowable) {
		super(pThrowable);
	}

	TextException(final String pMessage, final Throwable pThrowable) {
		super(pMessage, pThrowable);
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
