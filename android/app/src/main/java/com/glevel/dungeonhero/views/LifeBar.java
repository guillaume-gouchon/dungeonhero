package com.glevel.dungeonhero.views;

import android.content.Context;
import android.content.res.TypedArray;
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

    private float mFirstRatio = 0;

    public LifeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LifeBar(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.in_game_life_bar, this, true);

        mContainer = findViewById(R.id.container);
        mLifeBar = findViewById(R.id.life_bar);

        mLowLifeAnimation = AnimationUtils.loadAnimation(context, R.anim.low_life_fade);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LifeBar);
        int lifeColor = a.getResourceId(R.styleable.LifeBar_lifeColor, R.color.green);
        mLifeBar.setBackgroundResource(lifeColor);
        a.recycle();
    }

    public void updateLife(float ratio) {
        if (mFirstRatio == 0) {
            mFirstRatio = ratio;
        }

        if (mContainer.getWidth() > 0 && mContainer.getHeight() > 0) {
            mLifeBar.setLayoutParams(new LayoutParams((int) (mContainer.getWidth() * ratio), mContainer.getHeight()));
        }

        if (ratio < 0.25f) {
            mContainer.startAnimation(mLowLifeAnimation);
        } else {
            mContainer.setAnimation(null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mFirstRatio > 0) {
            updateLife(mFirstRatio);
            mFirstRatio = -1;
        }
    }

}
