package com.glevel.dungeonhero.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.glevel.dungeonhero.MyApplication.FONTS;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.utils.MusicManager;

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

    @Override
    public void setOnClickListener(final OnClickListener l) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(getContext(), R.raw.button_sound);
                l.onClick(v);
            }
        });
    }
}
