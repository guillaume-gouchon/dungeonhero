package com.glevel.dungeonhero.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.utils.ApplicationUtils;

public class SpriteView extends ImageView {

    private static final String TAG = "SpriteView";
    private static final int SPRITE_PERIOD = 250;// in ms

    private Rect src = new Rect();
    private Rect dst = new Rect();
    private boolean go;
    private SpriteThread spriteThread;
    private int frameWidth;
    private int frameHeight;
    private Bitmap spriteSheet;

    public SpriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.FIT_XY);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpriteView);
        String spriteName = a.getString(R.styleable.SpriteView_spriteName);
        if (spriteName != null) {
            setSpriteName(spriteName);
        }
        a.recycle();
    }

    public void setSpriteName(final String spriteName) {
        if (spriteSheet == null) {
            Log.d(TAG, "set sprite = " + spriteName);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    spriteSheet = ApplicationUtils.getBitmapFromAsset(getContext(), spriteName);

                    frameWidth = spriteSheet.getWidth() / UnitSprite.SPRITE_ANIM_X;
                    frameHeight = spriteSheet.getHeight() / UnitSprite.SPRITE_ANIM_Y;
                    dst.left = dst.top = 0;
                    dst.right = frameWidth;
                    dst.bottom = frameHeight;
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    startAnimation();
                }
            }.execute();
        } else {
            startAnimation();
        }
    }

    public void startAnimation() {
        Log.d(TAG, "start animation");
        go = true;
        spriteThread = new SpriteThread();
        spriteThread.start();
    }

    public void stopAnimation() {
        Log.d(TAG, "Stop animation");
        go = false;
        if (spriteThread != null) {
            try {
                spriteThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                stopAnimation();
                return null;
            }
        }.execute();
        spriteSheet = null;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (spriteSheet != null) {
            canvas.drawBitmap(spriteSheet, src, dst, null);
        } else {
            super.onDraw(canvas);
        }
    }

    public class SpriteThread extends Thread {

        int screenWidth, screenHeight;

        @Override
        public void run() {
            screenWidth = getWidth();
            screenHeight = getHeight();

            while (go) {
                for (int i = 0; i < UnitSprite.SPRITE_ANIM_X; i++) {
                    src.left = i * frameWidth;
                    src.top = 2 * frameHeight;
                    src.right = src.left + frameWidth;
                    src.bottom = src.top + frameHeight;

                    dst.left = 0;
                    dst.top = -30;
                    dst.right = getWidth();
                    dst.bottom = getHeight();
                    postInvalidate();
                    try {
                        Thread.sleep(SPRITE_PERIOD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}