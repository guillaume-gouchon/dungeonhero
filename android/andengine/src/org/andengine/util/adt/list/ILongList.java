package org.andengine.util.adt.list;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:36:57 - 03.05.2012
 */
interface ILongList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	boolean isEmpty();
	float get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	void add(final long pItem);
	void add(final int pIndex, final long pItem) throws ArrayIndexOutOfBoundsException;
	float remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	int size();
	void clear();
	long[] toArray();
}