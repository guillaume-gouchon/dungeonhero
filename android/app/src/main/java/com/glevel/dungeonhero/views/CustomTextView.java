package com.glevel.dungeonhero.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.glevel.dungeonhero.BaseApplication.FONTS;

public class CustomTextView extends AppCompatTextView {

    private static final Typeface font = FONTS.text;

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init();
    }

    private void init() {
        setTypeface(font);
    }

}
