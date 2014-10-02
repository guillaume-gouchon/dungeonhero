package com.glevel.dungeonhero.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.glevel.dungeonhero.MyApplication.FONTS;

public class CustomButton extends Button {

    public CustomButton(Context context) {
        super(context);
        initCustomButton();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCustomButton();
    }

    private void initCustomButton() {
        setTypeface(FONTS.main);
        
        // disable button default sound when clicked
        setSoundEffectsEnabled(false);
    }

}
