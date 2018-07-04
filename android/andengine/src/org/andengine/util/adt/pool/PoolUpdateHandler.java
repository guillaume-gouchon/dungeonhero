package org.andengine.util.adt.pool;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.util.adt.list.ShiftList;
import org.andengine.util.adt.queue.IQueue;
import org.andengine.util.adt.queue.concurrent.SynchronizedQueue;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Valentin Milea
 * @author Nicolas Gramlich
 * 
 * @since 23:02:58 - 21.08.2010
 */
public abstract class PoolUpdateHandler<T extends PoolItem> implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Pool<T> mPool;
	private final IQueue<T> mScheduledPoolItemQueue = new SynchronizedQueue<>(new ShiftList<T>());

	// ===========================================================
	// Constructors
	// ===========================================================

	PoolUpdateHandler() {
		this.mPool = new Pool<T>() {
			@Override
			protected T onAllocatePoolItem() {
				return PoolUpdateHandler.this.onAllocatePoolItem();
			}
		};
	}

	PoolUpdateHandler(final int pInitialPoolSize) {
		this.mPool = new Pool<T>(pInitialPoolSize) {
			@Override
			protected T onAllocatePoolItem() {
				return PoolUpdateHandler.this.onAllocatePoolItem();
			}
		};
	}

	PoolUpdateHandler(final int pInitialPoolSize, final int pGrowth) {
		this.mPool = new Pool<T>(pInitialPoolSize, pGrowth) {
			@Override
			protected T onAllocatePoolItem() {
				return PoolUpdateHandler.this.onAllocatePoolItem();
			}
		};
	}

	PoolUpdateHandler(final int pInitialPoolSize, final int pGrowth, final int pAvailableItemCountMaximum) {
		this.mPool = new Pool<T>(pInitialPoolSize, pGrowth, pAvailableItemCountMaximum) {
			@Override
			protected T onAllocatePoolItem() {
				return PoolUpdateHandler.this.onAllocatePoolItem();
			}
		};
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract T onAllocatePoolItem();

	protected abstract void onHandlePoolItem(final T pPoolItem);

	@Override
	public void onUpdate(final float pSecondsElapsed) {

		T item;
		while((item = this.mScheduledPoolItemQueue.poll()) != null) {
			this.onHandlePoolItem(item);
			this.mPool.recyclePoolItem(item);
		}
	}

	@Override
	public void reset() {

		T item;
		while((item = this.mScheduledPoolItemQueue.poll()) != null) {
			this.mPool.recyclePoolItem(item);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public T obtainPoolItem() {
		return this.mPool.obtainPoolItem();
	}

	public void postPoolItem(final T pPoolItem) {
		if(pPoolItem == null) {
			throw new IllegalArgumentException("PoolItem already recycled!");
		} else if(!this.mPool.ownsPoolItem(pPoolItem)) {
			throw new IllegalArgumentException("PoolItem from another pool!");
		}

		this.mScheduledPoolItemQueue.enter(pPoolItem);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
