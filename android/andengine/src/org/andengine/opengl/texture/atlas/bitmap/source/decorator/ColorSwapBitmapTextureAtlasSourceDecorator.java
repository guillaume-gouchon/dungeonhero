package org.andengine.opengl.texture.atlas.bitmap.source.decorator;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.IBitmapTextureAtlasSourceDecoratorShape;
import org.andengine.util.color.Color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 19:41:39 - 07.06.2011
 */
public class ColorSwapBitmapTextureAtlasSourceDecorator extends BaseShapeBitmapTextureAtlasSourceDecorator {
    // ===========================================================
    // Constants
    // ===========================================================

    private static final int TOLERANCE_DEFAULT = 0;

    // ===========================================================
    // Fields
    // ===========================================================

    final int mColorKeyColorARGBPackedInt;
    final int mTolerance;
    private final int mColorSwapColorARGBPackedInt;

    // ===========================================================
    // Constructors
    // ===========================================================

    ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final Color pColorKeyColor, final Color pColorSwapColor) {
        this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor.getARGBPackedInt(), pColorSwapColor.getARGBPackedInt());
    }

    ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColorARGBPackedInt, final int pColorSwapColorARGBPackedInt) {
        this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColorARGBPackedInt, TOLERANCE_DEFAULT, pColorSwapColorARGBPackedInt, null);
    }

    ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final Color pColorKeyColor, final Color pColorSwapColor, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
        this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor.getARGBPackedInt(), pColorSwapColor.getARGBPackedInt(), pTextureAtlasSourceDecoratorOptions);
    }

    ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColorARGBPackedInt, final int pColorSwapColorARGBPackedInt, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
        this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColorARGBPackedInt, TOLERANCE_DEFAULT, pColorSwapColorARGBPackedInt, pTextureAtlasSourceDecoratorOptions);
    }

    ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final Color pColorKeyColor, final int pTolerance, final Color pColorSwapColor) {
        this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor.getARGBPackedInt(), pTolerance, pColorSwapColor.getARGBPackedInt());
    }

    ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColorARGBPackedInt, final int pTolerance, final int pColorSwapColorARGBPackedInt) {
        this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColorARGBPackedInt, pTolerance, pColorSwapColorARGBPackedInt, null);
    }

    ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final Color pColorKeyColor, final int pTolerance, final Color pColorSwapColor, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
        this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor.getARGBPackedInt(), pTolerance, pColorSwapColor.getARGBPackedInt(), pTextureAtlasSourceDecoratorOptions);
    }

    ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColorARGBPackedInt, final int pTolerance, final int pColorSwapColorARGBPackedInt, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
        super(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pTextureAtlasSourceDecoratorOptions);

        this.mColorKeyColorARGBPackedInt = pColorKeyColorARGBPackedInt;
        this.mTolerance = pTolerance;
        this.mColorSwapColorARGBPackedInt = pColorSwapColorARGBPackedInt;
//		this.mPaint.setXfermode(new AvoidXfermode(pColorKeyColorARGBPackedInt, pTolerance, Mode.TARGET));
        this.mPaint.setColor(pColorSwapColorARGBPackedInt);
    }

    @Override
    public ColorSwapBitmapTextureAtlasSourceDecorator deepCopy() {
        return new ColorSwapBitmapTextureAtlasSourceDecorator(this.mBitmapTextureAtlasSource, this.mBitmapTextureAtlasSourceDecoratorShape, this.mColorKeyColorARGBPackedInt, this.mTolerance, this.mColorSwapColorARGBPackedInt, this.mTextureAtlasSourceDecoratorOptions);
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
