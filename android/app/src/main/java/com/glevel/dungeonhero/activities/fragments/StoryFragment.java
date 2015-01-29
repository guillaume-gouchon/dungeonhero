package com.glevel.dungeonhero.activities.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.BookChooserActivity;
import com.glevel.dungeonhero.activities.games.GameActivity;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.utils.ApplicationUtils;

public class StoryFragment extends DialogFragment implements View.OnClickListener {

    public static final String ARGUMENT_STORY = "story";
    public static final String ARGUMENT_IS_OUTRO = "is_outro";

    private boolean mIsOutro = false;

    /**
     * UI
     */
    private Runnable mStormEffect;
    private ImageView mStormsBg;
    private TextView mStoryTV;

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

        // set the animations to use on showing and hiding the dialog
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_fragment_story, container, false);

        mStormsBg = (ImageView) layout.findViewById(R.id.storms);

        // retrieve story content
        Bundle args = getArguments();
        int storyResource = args.getInt(ARGUMENT_STORY);
        String story = getString(storyResource);

        mStoryTV = (TextView) layout.findViewById(R.id.story);
        mStoryTV.setText(story);

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
        mStoryTV.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.story_text_animation));
    }

}
