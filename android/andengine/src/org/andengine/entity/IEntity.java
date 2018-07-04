package org.andengine.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IDrawHandler;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.runnable.RunnableHandler;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import org.andengine.entity.scene.Scene;
import org.andengine.util.IDisposable;
import org.andengine.util.adt.transformation.Transformation;
import org.andengine.util.color.Color;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:20:25 - 08.03.2010
 */
public interface IEntity extends IDrawHandler, IUpdateHandler, IDisposable {
	// ===========================================================
	// Constants
	// ===========================================================

	int TAG_INVALID = Integer.MIN_VALUE;

	// ===========================================================
	// Methods
	// ===========================================================

	boolean isVisible();
	void setVisible(final boolean pVisible);

	boolean isIgnoreUpdate();
	void setIgnoreUpdate(boolean pIgnoreUpdate);

	boolean isChildrenVisible();
	void setChildrenVisible(final boolean pChildrenVisible);

	boolean isChildrenIgnoreUpdate();
	void setChildrenIgnoreUpdate(boolean pChildrenIgnoreUpdate);

	int getTag();
	void setTag(final int pTag);

	int getZIndex();
	void setZIndex(final int pZIndex);

	boolean hasParent();
	IEntity getParent();
	void setParent(final IEntity pEntity);

	float getX();
	float getY();
	void setX(final float pX);
	void setY(final float pY);

	void setPosition(final IEntity pOtherEntity);
	void setPosition(final float pX, final float pY);

	boolean isRotated();
	float getRotation();
	void setRotation(final float pRotation);

	float getRotationCenterX();
	float getRotationCenterY();
	void setRotationCenterX(final float pRotationCenterX);
	void setRotationCenterY(final float pRotationCenterY);
	void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY);

	boolean isScaled();
	float getScaleX();
	float getScaleY();
	void setScaleX(final float pScaleX);
	void setScaleY(final float pScaleY);
	void setScale(final float pScale);
	void setScale(final float pScaleX, final float pScaleY);

	float getScaleCenterX();
	float getScaleCenterY();
	void setScaleCenterX(final float pScaleCenterX);
	void setScaleCenterY(final float pScaleCenterY);
	void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY);

	boolean isSkewed();
	float getSkewX();
	float getSkewY();
	void setSkewX(final float pSkewX);
	void setSkewY(final float pSkewY);
	void setSkew(final float pSkew);
	void setSkew(final float pSkewX, final float pSkewY);

	float getSkewCenterX();
	float getSkewCenterY();
	void setSkewCenterX(final float pSkewCenterX);
	void setSkewCenterY(final float pSkewCenterY);
	void setSkewCenter(final float pSkewCenterX, final float pSkewCenterY);

	boolean isRotatedOrScaledOrSkewed();

	float getRed();
	float getGreen();
	float getBlue();
	float getAlpha();
	Color getColor();

	void setRed(final float pRed);
	void setGreen(final float pGreen);
	void setBlue(final float pBlue);
	void setAlpha(final float pAlpha);
	void setColor(final Color pColor);
	void setColor(final float pRed, final float pGreen, final float pBlue);
	void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha);

	/**
	 * @return a shared(!) float[] of length 2.
	 */
    float[] getSceneCenterCoordinates();

	/**
	 * @param pReuse must be of length 2.
	 * @return <code>pReuse</code> as a convenience.
	 */
    float[] getSceneCenterCoordinates(final float[] pReuse);

	/**
	 * @param pX
	 * @param pY
	 * @return a shared(!) float[] of length 2.
	 */
    float[] convertLocalToSceneCoordinates(final float pX, final float pY);
	/**
	 * @param pX
	 * @param pY
	 * @param pReuse must be of length 2.
	 * @return <code>pReuse</code> as a convenience.
	 */
    float[] convertLocalToSceneCoordinates(final float pX, final float pY, final float[] pReuse);
	/**
	 * @param pCoordinates must be of length 2.
	 * @return a shared(!) float[] of length 2.
	 */
    float[] convertLocalToSceneCoordinates(final float[] pCoordinates);
	/**
	 * @param pCoordinates must be of length 2.
	 * @param pReuse must be of length 2.
	 * @return <code>pReuse</code> as a convenience.
	 */
    float[] convertLocalToSceneCoordinates(final float[] pCoordinates, final float[] pReuse);

	/**
	 * @param pX
	 * @param pY
	 * @return a shared(!) float[] of length 2.
	 */
    float[] convertSceneToLocalCoordinates(final float pX, final float pY);
	/**
	 * @param pX
	 * @param pY
	 * @param pReuse must be of length 2.
	 * @return <code>pReuse</code> as a convenience.
	 */
    float[] convertSceneToLocalCoordinates(final float pX, final float pY, final float[] pReuse);
	/**
	 * @param pCoordinates must be of length 2.
	 * @return a shared(!) float[] of length 2.
	 */
    float[] convertSceneToLocalCoordinates(final float[] pCoordinates);
	/**
	 * @param pCoordinates must be of length 2.
	 * @param pReuse must be of length 2.
	 * @return <code>pReuse</code> as a convenience.
	 */
    float[] convertSceneToLocalCoordinates(final float[] pCoordinates, final float[] pReuse);

	Transformation getLocalToSceneTransformation();
	Transformation getSceneToLocalTransformation();

	Transformation getLocalToParentTransformation();
	Transformation getParentToLocalTransformation();

	int getChildCount();

	void onAttached();
	void onDetached();

	void attachChild(final IEntity pEntity);

	IEntity getChildByTag(final int pTag);
	IEntity getChildByMatcher(final IEntityMatcher pEntityMatcher);
	IEntity getChildByIndex(final int pIndex);
	IEntity getFirstChild();
	IEntity getLastChild();

	/**
	 * @param pEntityMatcher
	 * @return all children (recursively!) that match the supplied {@link IEntityMatcher}.
	 */
    ArrayList<IEntity> query(final IEntityMatcher pEntityMatcher);
	/**
	 * @param pEntityMatcher
	 * @return the first child (recursively!) that matches the supplied {@link IEntityMatcher} or <code>null</code> if none matches..
	 */
    IEntity queryFirst(final IEntityMatcher pEntityMatcher);
	/**
	 * @param pEntityMatcher
	 * @param pResult the {@link List} to put the result into.
	 * @return all children (recursively!) that match the supplied {@link IEntityMatcher}.
	 */
    <L extends List<IEntity>> L query(final IEntityMatcher pEntityMatcher, final L pResult);
	/**
	 * @param pEntityMatcher
	 * @return the first child (recursively!) that matches the supplied {@link IEntityMatcher} or <code>null</code> if none matches..
	 * @throws ClassCastException when the supplied {@link IEntityMatcher} matched an {@link IEntity} that was not of the requested subtype.
	 */
    <S extends IEntity> S queryFirstForSubclass(final IEntityMatcher pEntityMatcher);
	/**
	 * @param pEntityMatcher
	 * @return all children (recursively!) that match the supplied {@link IEntityMatcher}.
	 * @throws ClassCastException when the supplied {@link IEntityMatcher} matched an {@link IEntity} that was not of the requested subtype.
	 */
    <S extends IEntity> ArrayList<S> queryForSubclass(final IEntityMatcher pEntityMatcher) throws ClassCastException;
	/**
	 * @param pEntityMatcher
	 * @param pResult the {@link List} to put the result into.
	 * @return all children (recursively!) that match the supplied {@link IEntityMatcher}.
	 * @throws ClassCastException when the supplied {@link IEntityMatcher} matched an {@link IEntity} that was not of the requested subtype.
	 */
    <L extends List<S>, S extends IEntity> L queryForSubclass(final IEntityMatcher pEntityMatcher, final L pResult) throws ClassCastException;

	/**
	 * Immediately sorts the {@link IEntity}s based on their ZIndex. Sort is stable.
	 */
    void sortChildren();
	/**
	 * Sorts the {@link IEntity}s based on their ZIndex. Sort is stable.
	 * In contrast to {@link IEntity#sortChildren()} this method is particularly useful to avoid multiple sorts per frame. 
	 * @param pImmediate if <code>true</code>, the sorting is executed immediately.
	 * If <code>false</code> the sorting is executed before the next (visible) drawing of the children of this {@link IEntity}. 
	 */
    void sortChildren(final boolean pImmediate);
	/**
	 * Sorts the {@link IEntity}s based on the {@link Comparator} supplied. Sort is stable.
	 * @param pEntityComparator
	 */
    void sortChildren(final IEntityComparator pEntityComparator);

	boolean detachSelf();

	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise
	 * it may throw an {@link IndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
    boolean detachChild(final IEntity pEntity);
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise
	 * it may throw an {@link IndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
    IEntity detachChild(final int pTag);
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise
	 * it may throw an {@link IndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
    IEntity detachChild(final IEntityMatcher pEntityMatcher);
	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link Scene} or the {@link Engine} itself, because otherwise
	 * it may throw an {@link IndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
    boolean detachChildren(final IEntityMatcher pEntityMatcher);

	void detachChildren();

	void callOnChildren(final IEntityParameterCallable pEntityParameterCallable);
	void callOnChildren(final IEntityParameterCallable pEntityParameterCallable, final IEntityMatcher pEntityMatcher);

	void registerUpdateHandler(final IUpdateHandler pUpdateHandler);
	boolean unregisterUpdateHandler(final IUpdateHandler pUpdateHandler);
	boolean unregisterUpdateHandlers(final IUpdateHandlerMatcher pUpdateHandlerMatcher);
	int getUpdateHandlerCount();
	void clearUpdateHandlers();

	void registerEntityModifier(final IEntityModifier pEntityModifier);
	boolean unregisterEntityModifier(final IEntityModifier pEntityModifier);
	boolean unregisterEntityModifiers(final IEntityModifierMatcher pEntityModifierMatcher);
	int getEntityModifierCount();
	void clearEntityModifiers();

	boolean isCullingEnabled();
	void setCullingEnabled(final boolean pCullingEnabled);
	/**
	 * Will only be performed if {@link IEntity#isCullingEnabled()} is true.
	 *
	 * @param pCamera the currently active camera to perform culling checks against.
	 * @return <code>true</code> when this object is visible by the {@link Camera}, <code>false</code> otherwise.
	 */
    boolean isCulled(final Camera pCamera);

	void setUserData(final Object pUserData);
	Object getUserData();

	void toString(final StringBuilder pStringBuilder);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
