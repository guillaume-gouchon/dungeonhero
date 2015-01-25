package com.glevel.dungeonhero.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.utils.ApplicationUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guillaume ON 10/3/14.
 */
public class CustomCarousel extends LinearLayout implements ViewPager.OnPageChangeListener {

    private static final String TAG = "CustomCarousel";

    /**
     * Hint
     */
    private static final String CAROUSEL_HINT_PREFS = "carousel_hint_prefs";
    private static final int SHOW_HINT_NUMBER_TIME = 3;

    private static final int DEFAULT_NB_COLUMNS = 1;
    private static final float ALPHA_ACTIVE_DOT = 0.65f;
    private static final float ALPHA_PASSIVE_DOT = 0.15f;
    private static final int DOTS_SIZE = 10, DOTS_MARGIN = 7;

    private int mNbColumns;

    private Context mContext;
    private Adapter mAdapter;

    /**
     * UI
     */
    private ViewPager mViewPager;
    private ViewGroup mPagination;
    private View[] mPaginationDots;
    private View mHint;

    public CustomCarousel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_carousel, this, true);

        // show or not the hint
        mHint = findViewById(R.id.hint);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int nbCarouselHintShow = prefs.getInt(CAROUSEL_HINT_PREFS, 0);
        Log.d(TAG, "carousel hint show number = " + nbCarouselHintShow);
        if (nbCarouselHintShow < SHOW_HINT_NUMBER_TIME) {
            Log.d(TAG, "show hint");

            // update prefs
            prefs.edit().putInt(CAROUSEL_HINT_PREFS, nbCarouselHintShow + 1).apply();

            // show hint
            mHint.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_dots));
            mHint.setVisibility(View.VISIBLE);
        }

        mViewPager = (ViewPager) findViewById(R.id.carousel);
        mViewPager.setOnPageChangeListener(this);

        mPagination = (LinearLayout) findViewById(R.id.pagination);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCarousel);
        mNbColumns = a.getInt(R.styleable.CustomCarousel_nbColumns, DEFAULT_NB_COLUMNS);
        a.recycle();
    }

    public void setAdapter(Adapter adapter) {
        adapter.setNbColumns(mNbColumns);
        mAdapter = adapter;
        mViewPager.setAdapter(adapter);
        updatePagination();
    }

    private void updatePagination() {
        mPagination.removeAllViews();
        mPaginationDots = new View[mAdapter.getCount() - (mNbColumns - 1)];

        int sizeDot = ApplicationUtils.convertDpToPixels(mContext, DOTS_SIZE);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(sizeDot, sizeDot);
        layoutParams.setMargins(0, 0, ApplicationUtils.convertDpToPixels(mContext, DOTS_MARGIN), 0);

        View dot;
        for (int n = 0; n < mPaginationDots.length; n++) {
            dot = new View(mContext);
            dot.setLayoutParams(layoutParams);
            dot.setBackgroundResource(R.drawable.pagination_dot);
            dot.setAlpha(n == 0 ? ALPHA_ACTIVE_DOT : ALPHA_PASSIVE_DOT);
            mPaginationDots[n] = dot;
            mPagination.addView(dot);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0.5) {
            mHint.setVisibility(View.GONE);
            mHint.setAnimation(null);
        }
    }

    @Override
    public void onPageSelected(int position) {
        for (int n = 0; n < mPaginationDots.length; n++) {
            if (position == n || position >= mPaginationDots.length && n == mPaginationDots.length - 1) {
                mPaginationDots[n].setAlpha(ALPHA_ACTIVE_DOT);
            } else {
                mPaginationDots[n].setAlpha(ALPHA_PASSIVE_DOT);
            }
        }
    }

    public static abstract class Adapter<T> extends PagerAdapter {

        private final LayoutInflater mInflater;
        protected List<T> mDataList;
        private int mLayoutResource;
        private int mNbColumns;
        private OnClickListener mItemClickedListener;

        private boolean isElementSelected = false;// avoid view pager's multiple selections

        private OnClickListener mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isElementSelected) {
                    isElementSelected = true;
                    if (mItemClickedListener != null) {
                        mItemClickedListener.onClick(view);
                    }

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isElementSelected = false;
                        }
                    }, 500);
                }
            }
        };

        public Adapter(Context context, int layoutResource, List<T> dataList, OnClickListener itemClickedListener) {
            mItemClickedListener = itemClickedListener;
            mLayoutResource = layoutResource;
            mDataList = dataList;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View layout = mInflater.inflate(mLayoutResource, null);
            layout.setOnClickListener(mOnClickListener);

            // remove default sound effect ON click
            layout.setSoundEffectsEnabled(false);

            container.addView(layout);

            return layout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public float getPageWidth(int position) {
            return 1 / (float) mNbColumns;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        public void setNbColumns(int nbColumns) {
            mNbColumns = nbColumns;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
