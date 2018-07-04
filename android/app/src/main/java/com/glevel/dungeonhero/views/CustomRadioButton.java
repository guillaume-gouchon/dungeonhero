package com.glevel.dungeonhero.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.glevel.dungeonhero.BaseApplication.FONTS;

@SuppressLint("AppCompatCustomView")
public class CustomRadioButton extends RadioButton {

    private static final Typeface font = FONTS.main;

    public CustomRadioButton(Context context) {
        super(context);
        init();
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setTypeface(font);
    }

}
