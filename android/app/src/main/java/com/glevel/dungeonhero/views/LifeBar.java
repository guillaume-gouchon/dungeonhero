package com.glevel.dungeonhero.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.glevel.dungeonhero.R;

/**
 * Created by guillaume ON 10/3/14.
 */
public class LifeBar extends LinearLayout {

    private View mContainer, mLifeBar;
    private Animation mLowLifeAnimation;

    public LifeBar(Context context) {
        super(context);
        init(context);
    }

    public LifeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LifeBar(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.in_game_life_bar, this, true);

        mContainer = findViewById(R.id.container);
        mLifeBar = findViewById(R.id.life_bar);

        mLowLifeAnimation = AnimationUtils.loadAnimation(context, R.anim.fade);
    }

    public void updateLife(float ratio) {
        if (mContainer.getWidth() > 0 && mContainer.getHeight() > 0) {
            mLifeBar.setLayoutParams(new LayoutParams((int) (mContainer.getWidth() * ratio), mContainer.getHeight()));
        }

        if (ratio < 0.25) {
            mContainer.startAnimation(mLowLifeAnimation);
        } else {
            mContainer.setAnimation(null);
        }
    }

}
