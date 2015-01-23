package com.glevel.dungeonhero.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.glevel.dungeonhero.MyApplication.FONTS;

public class CustomTitle extends TextView {

    private static final Typeface font = FONTS.main;

    public CustomTitle(Context context) {
        super(context);
        init();
    }

    public CustomTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTitle(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init();
    }

    private void init() {
        setTypeface(font);
    }

}
