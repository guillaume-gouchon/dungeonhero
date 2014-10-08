package com.glevel.dungeonhero.activities;

import android.widget.Button;
import android.widget.TextView;

import com.glevel.dungeonhero.R;

public class TutorialActivity extends GameActivity {

    private static final int[] TUTORIAL_INSTRUCTIONS = {R.string.tutorial0,
            R.string.tutorial1, R.string.tutorial2, R.string.tutorial3,
            R.string.tutorial4, R.string.tutorial5, R.string.tutorial6,
            R.string.tutorial7, R.string.tutorial8, R.string.tutorial9,
            R.string.tutorial10, R.string.tutorial11};

    private int tutorialStep = 0;

    private Button nextButton;
    private TextView tutorialInstructions;

//
//    private void setupTutorialUI() {
//        nextButton = (Button) findViewById(R.id.nextButton);
//        nextButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToNextStep();
//            }
//        });
//        tutorialInstructions = (TextView) findViewById(R.id.tutorialInstructions);
//        tutorialInstructions.setText(TUTORIAL_INSTRUCTIONS[tutorialStep]);
//    }
//
//    private void goToNextStep() {
//        tutorialStep++;
//        tutorialInstructions.setText(TUTORIAL_INSTRUCTIONS[tutorialStep]);
//        if (tutorialStep >= TUTORIAL_INSTRUCTIONS.length - 1) {
//            nextButton.setVisibility(View.GONE);
//        }
//    }

}
