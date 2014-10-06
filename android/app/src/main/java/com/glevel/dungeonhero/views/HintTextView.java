package com.glevel.dungeonhero.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.glevel.dungeonhero.MyApplication.FONTS;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.utils.ApplicationUtils;

public class HintTextView extends TextView implements View.OnClickListener {

    private static final Typeface font = FONTS.text;

    private int mTextHint;

    public HintTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public HintTextView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context, attrs, style);
    }

    private void init(Context context, AttributeSet attrs, int style) {
        setTypeface(font);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HintTextView);
        mTextHint = a.getResourceId(R.styleable.HintTextView_textHint, 0);
        a.recycle();

        setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        ApplicationUtils.showToast(getContext(), mTextHint, Toast.LENGTH_SHORT);
    }
}
