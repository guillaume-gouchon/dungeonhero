package com.glevel.dungeonhero.activities.fragments;

import android.content.Intent;
import android.view.View;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.NewGameActivity;

public class VictoryFragment extends StoryFragment implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipButton:
                startActivity(new Intent(getActivity(), NewGameActivity.class));
                dismiss();
                getActivity().finish();
                break;
            case R.id.replayButton:
                startAnimation();
                break;
        }
    }

}
