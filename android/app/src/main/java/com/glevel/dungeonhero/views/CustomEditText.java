package com.glevel.dungeonhero.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.glevel.dungeonhero.MyApplication.FONTS;

public class CustomEditText extends EditText {

    private static final Typeface font = FONTS.text;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init();
    }

    private void init() {
        setTypeface(font);
    }

}
