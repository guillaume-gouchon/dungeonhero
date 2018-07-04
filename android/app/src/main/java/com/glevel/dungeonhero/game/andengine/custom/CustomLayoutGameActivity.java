package com.glevel.dungeonhero.game.andengine.custom;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:18:50 - 06.10.2010
 */
public abstract class CustomLayoutGameActivity extends CustomBaseGameActivity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    protected abstract int getLayoutID();

    protected abstract int getRenderSurfaceViewID();

    @Override
    protected void onSetContentView() {
        super.setContentView(this.getLayoutID());

        this.mRenderSurfaceView = this.findViewById(this.getRenderSurfaceViewID());

        this.mRenderSurfaceView.setRenderer(this.mEngine, this);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
