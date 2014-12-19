package com.glevel.dungeonhero.activities;

import android.widget.Button;
import android.widget.TextView;

import com.glevel.dungeonhero.R;

public class TutorialActivity extends GameActivity {

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
