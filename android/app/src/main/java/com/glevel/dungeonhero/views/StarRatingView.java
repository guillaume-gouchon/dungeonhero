package com.glevel.dungeonhero.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.glevel.dungeonhero.R;

public class StarRatingView extends LinearLayout {

    private ImageView mStar1, mStar2, mStar3;
    private Animation mBounceAnimation;

    public StarRatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StarRatingView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.star_rating, this, true);

        mStar1 = findViewById(R.id.star1);
        mStar2 = findViewById(R.id.star2);
        mStar3 = findViewById(R.id.star3);

        mBounceAnimation = AnimationUtils.loadAnimation(context, R.anim.bounce_star_rating);
    }

    public void updateRating(int rating) {
        updateStar(mStar1, rating > 0);
        updateStar(mStar2, rating > 1);
        updateStar(mStar3, rating > 2);
    }

    private void updateStar(ImageView view, boolean isFilled) {
        view.setImageResource(isFilled ? R.drawable.ic_star : R.drawable.ic_star_black);
        view.setAnimation(isFilled ? mBounceAnimation : null);
    }

}
