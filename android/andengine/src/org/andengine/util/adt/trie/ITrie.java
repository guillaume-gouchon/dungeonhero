package org.andengine.util.adt.trie;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:18:44 - 30.01.2012
 */
interface ITrie {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	void add(final CharSequence pCharSequence);
	void add(final CharSequence pCharSequence, final int pStart, final int pEnd);
	boolean contains(final CharSequence pCharSequence);
	boolean contains(final CharSequence pCharSequence, final int pStart, final int pEnd);
	/* TODO public void clear(); */
	/* TODO public boolean remove(final CharSequence pCharSequence); */
	/* TODO public boolean remove(final CharSequence pCharSequence, final int pStart, final int pEnd); */
}
