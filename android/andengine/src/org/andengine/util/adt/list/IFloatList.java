package org.andengine.util.adt.list;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:14:45 - 27.01.2012
 */
public interface IFloatList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	boolean isEmpty();
	float get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	void add(final float pItem);
	void add(final int pIndex, final float pItem) throws ArrayIndexOutOfBoundsException;
	float remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	int size();
	void clear();
	float[] toArray();
}