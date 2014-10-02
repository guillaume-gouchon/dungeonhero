package com.glevel.dungeonhero.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.adapters.MapPagerAdapter;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;

public class NewGameActivity extends MyActivity implements OnPageChangeListener {

    private static final float ALPHA_ACTIVE_DOT = 0.65f;
    private static final float ALPHA_PASSIVE_DOT = 0.15f;

    private ImageView mAllyPovBackground;
    private RadioGroup mRadioGroupArmy;
    private ViewPager mMapsCarousel;
    private ImageView[] paginationDots;

    private Runnable mStormEffect;

    private boolean isMapClicked;// avoid view pager's multiple selection

    /**
     * Callbacks
     */
    private OnClickListener onMapSelectedListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isMapClicked) {
                MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                isMapClicked = true;
            }
        }
    };
    private ImageView mGermanPovBackground;
    private Animation fadeOutAnimation, fadeInAnimation;
    private Animation paraAnim;
    private Animation groundAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_battle_chooser);
        setupUI();
    }

    private void setupUI() {
        // init armies chooser
        mRadioGroupArmy = (RadioGroup) findViewById(R.id.radioGroupArmy);

        // init maps carousel
        mMapsCarousel = (ViewPager) findViewById(R.id.lstMaps);
        PagerAdapter pagerAdapter = new MapPagerAdapter(onMapSelectedListener);
        mMapsCarousel.setAdapter(pagerAdapter);
        mMapsCarousel.setOnPageChangeListener(this);

        // setup pagination
        LinearLayout pagination = (LinearLayout) findViewById(R.id.pagination);
        paginationDots = new ImageView[pagerAdapter.getCount() - 1];
        for (int n = 0; n < pagerAdapter.getCount() - 1; n++) {
            ImageView dot = new ImageView(this);
            dot.setImageResource(R.drawable.ic_pagination_dot);
            dot.setPadding(ApplicationUtils.convertDpToPixels(getApplicationContext(), 10), 0, 0, 0);
            if (n == 0) {
                dot.setAlpha(ALPHA_ACTIVE_DOT);

            } else {
                dot.setAlpha(ALPHA_PASSIVE_DOT);
            }
            paginationDots[n] = dot;
            pagination.addView(dot);
        }

        // prepare background animations
        fadeInAnimation = AnimationUtils.loadAnimation(NewGameActivity.this, R.anim.fade_in);
        fadeInAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mGermanPovBackground.startAnimation(groundAnim);
            }
        });
        fadeOutAnimation = AnimationUtils.loadAnimation(NewGameActivity.this, R.anim.fade_out);
        fadeOutAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAllyPovBackground.setVisibility(View.VISIBLE);
                mGermanPovBackground.setVisibility(View.GONE);
                mGermanPovBackground.setAnimation(null);
            }
        });
        paraAnim = AnimationUtils.loadAnimation(this, R.anim.paratrooper_effect);
        groundAnim = AnimationUtils.loadAnimation(this, R.anim.ground_troops_effect);

        mAllyPovBackground = (ImageView) findViewById(R.id.allyPovBackground);
        mGermanPovBackground = (ImageView) findViewById(R.id.germanPovBackground);

        // stretch the background image a bit to fill all the screen while
        // rotating
        Point screenSize = ApplicationUtils.getScreenDimensions(this);
        android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(
                screenSize.x + 100, screenSize.y + 100);
        layoutParams.setMargins(-50, -50, 0, 0);
        mAllyPovBackground.setLayoutParams(layoutParams);
        mGermanPovBackground.setLayoutParams(layoutParams);

        mAllyPovBackground.startAnimation(paraAnim);
        mRadioGroupArmy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
//                if (checkedId == R.id.german_army) {
//                    mGermanPovBackground.startAnimation(fadeInAnimation);
//                    mGermanPovBackground.setVisibility(View.VISIBLE);
//                    mAllyPovBackground.removeCallbacks(mStormEffect);
//                    mAllyPovBackground.setAnimation(null);
//                    mAllyPovBackground.setVisibility(View.GONE);
//                    mStormEffect = ApplicationUtils.addStormBackgroundAtmosphere(mGermanPovBackground, 100, 0);
//                } else {
//                    mGermanPovBackground.startAnimation(fadeOutAnimation);
//                    mAllyPovBackground.setAnimation(paraAnim);
//                    mGermanPovBackground.removeCallbacks(mStormEffect);
//                    mStormEffect = ApplicationUtils.addStormBackgroundAtmosphere(mAllyPovBackground, 150, 50);
//                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isMapClicked = false;

        // init storm effect
        mStormEffect = ApplicationUtils.addStormBackgroundAtmosphere(mAllyPovBackground, 150, 50);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAllyPovBackground.removeCallbacks(mStormEffect);
        mGermanPovBackground.removeCallbacks(mStormEffect);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int direction = position + (positionOffsetPixels > 0 ? 1 : -1);
        if (direction >= 0 && direction < mMapsCarousel.getAdapter().getCount()) {
            paginationDots[position].setAlpha(ALPHA_ACTIVE_DOT - positionOffset
                    * (ALPHA_ACTIVE_DOT - ALPHA_PASSIVE_DOT));
            paginationDots[direction].setAlpha(ALPHA_PASSIVE_DOT + positionOffset
                    * (ALPHA_ACTIVE_DOT - ALPHA_PASSIVE_DOT));
        }
    }

    @Override
    public void onPageSelected(int position) {
        position = (int) Math.ceil((double) position / 2);
        for (int n = 0; n < paginationDots.length; n++) {
            if (position == n || position >= paginationDots.length && n == paginationDots.length - 1) {
                paginationDots[n].setAlpha(ALPHA_ACTIVE_DOT);
            } else {
                paginationDots[n].setAlpha(ALPHA_PASSIVE_DOT);
            }
        }
    }

}
