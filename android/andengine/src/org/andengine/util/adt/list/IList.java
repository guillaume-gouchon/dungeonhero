package org.andengine.util.adt.list;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:27:16 - 01.02.2012
 */
public interface IList<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	boolean isEmpty();
	T get(final int pIndex) throws IndexOutOfBoundsException;
	void set(final int pIndex, final T pItem) throws IndexOutOfBoundsException;
	int indexOf(final T pItem);
	void add(final T pItem);
	void add(final int pIndex, final T pItem) throws IndexOutOfBoundsException;
	boolean remove(final T pItem);
	T removeFirst();
	T removeLast();
	T remove(final int pIndex) throws IndexOutOfBoundsException;
	int size();
	void clear();
}
