package com.glevel.dungeonhero.activities.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.BookChooserActivity;
import com.glevel.dungeonhero.activities.GameActivity;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.utils.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;

public class StoryFragment extends DialogFragment implements View.OnClickListener {

    public static final String ARGUMENT_STORY = "story";
    public static final String ARGUMENT_IS_OUTRO = "is_outro";

    private Runnable mStormEffect;
    private ImageView mStormsBg;

    private List<View> mStoryViews = new ArrayList<View>();
    private int mCurrentLine;
    private boolean mIsOutro = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen); // remove title from dialog fragment
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null)
            return;

        // set the animations to use ON showing and hiding the dialog
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_story_fragment, container, false);

        mStormsBg = (ImageView) layout.findViewById(R.id.storms);

        // retrieve story resource
        Bundle args = getArguments();
        int storyResource = args.getInt(ARGUMENT_STORY);
        String story = getString(storyResource);

        // split story into paragraphs
        String[] storyLines = story.split("\n");

        ViewGroup rootLayout = (ViewGroup) layout.findViewById(R.id.rootLayout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView storyTV;
        for (String storyLine : storyLines) {
            storyTV = (TextView) inflater.inflate(R.layout.story_line, null);
            storyTV.setText(storyLine);
            storyTV.setLayoutParams(layoutParams);
            rootLayout.addView(storyTV);
            mStoryViews.add(storyTV);
        }

        startAnimation();

        mIsOutro = args.getBoolean(ARGUMENT_IS_OUTRO);

        layout.findViewById(R.id.skipButton).setOnClickListener(this);
        layout.findViewById(R.id.replayButton).setOnClickListener(this);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mStormEffect = ApplicationUtils.addStormBackgroundAtmosphere(mStormsBg, 150, 50);
    }

    @Override
    public void onPause() {
        super.onPause();
        mStormsBg.removeCallbacks(mStormEffect);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mIsOutro) {
            Intent intent = new Intent(getActivity(), BookChooserActivity.class);
            intent.putExtra(Game.class.getName(), ((GameActivity) getActivity()).getGame());
            getActivity().startActivity(intent);
            getActivity().finish();
        } else {
            ((GameActivity) getActivity()).showChapterIntro();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipButton:
                dismiss();
                break;

            case R.id.replayButton:
                startAnimation();
                break;
        }
    }

    protected void startAnimation() {
        mCurrentLine = 0;

        // reset animations
        for (View tv : mStoryViews) {
            tv.setAnimation(null);
            tv.setVisibility(View.GONE);
        }

        startNextLineAnimation();
    }

    private void startNextLineAnimation() {
        if (mCurrentLine > 0) {
            mStoryViews.get(mCurrentLine - 1).setAnimation(null);
            mStoryViews.get(mCurrentLine - 1).startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.star_wars_text_2));

            if (mCurrentLine > 1) {
                mStoryViews.get(mCurrentLine - 2).setAnimation(null);
                mStoryViews.get(mCurrentLine - 2).setVisibility(View.GONE);
            }
        }
        if (mCurrentLine < mStoryViews.size()) {
            mStoryViews.get(mCurrentLine).setVisibility(View.VISIBLE);

            Animation starWarsAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.star_wars_text);
            starWarsAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    startNextLineAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mStoryViews.get(mCurrentLine).startAnimation(starWarsAnimation);
        }

        mCurrentLine++;
    }

}
