package org.andengine.util.adt.list;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:21:53 - 03.05.2012
 */
interface IIntList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	boolean isEmpty();
	float get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	void add(final int pItem);
	void add(final int pIndex, final int pItem) throws ArrayIndexOutOfBoundsException;
	float remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	int size();
	void clear();
	int[] toArray();
}