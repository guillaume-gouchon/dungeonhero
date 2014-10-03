package com.glevel.dungeonhero.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.utils.ApplicationUtils;

public class StoryFragment extends DialogFragment implements View.OnClickListener {

    private Runnable mStormEffect;
    private ImageView mStormsBg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0); // remove title from dialog fragment
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
        View layout = inflater.inflate(R.layout.dialog_story_fragment, container, false);

        mStormsBg = (ImageView) layout.findViewById(R.id.storms);

        TextView storyTV = (TextView) layout.findViewById(R.id.story);
        // TODO
        storyTV.setText("asdjfh asjdf jasdhfjldsahjfhjsadhfjsahjh sjdfha jsdahjf hjshdjafhsdjahfjsdhajfhsjadhfjsadhjfsadhf\nfhdsjfhdsf sdfdsjfhdsjfhsdj hsdjfhsdjhf jsdhf sjdhf jsdhfjsdhfj sdj hjsd hjshfjsdhjs dfsjdhfjdshfjdhsfhdjfhjsdhfjsd jsdhjhsdjfhsdjhj js jsdh jfhsdjhfjdsh jsdh jfhsdjhfjds  sdhsdjhf ");
        storyTV.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.star_wars_text));

        layout.findViewById(R.id.skipButton).setOnClickListener(this);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipButton:
                dismiss();
                break;
        }
    }

}
