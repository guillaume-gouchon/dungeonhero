package com.glevel.dungeonhero.activities;

import android.widget.Button;
import android.widget.TextView;

import com.glevel.dungeonhero.R;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;

public class TutorialActivity extends GameActivity {

    @Override
    protected int getRenderSurfaceViewID() {
        return 0;
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        return null;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {

    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {

    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {

    }

    @Override
    public void playGeolocalizedSound(String soundName, float x, float y) {

    }

    @Override
    public void drawSprite(float x, float y, String spriteName, int duration, int size) {

    }

    @Override
    public void drawAnimatedSprite(float x, float y, String spriteName, int frameDuration, float scale, int loopCount, boolean removeAfter, int zIndex) {

    }

    private static final int[] TUTORIAL_INSTRUCTIONS = {R.string.tutorial0,
            R.string.tutorial1, R.string.tutorial2, R.string.tutorial3,
            R.string.tutorial4, R.string.tutorial5, R.string.tutorial6,
            R.string.tutorial7, R.string.tutorial8, R.string.tutorial9,
            R.string.tutorial10, R.string.tutorial11};

    private int tutorialStep = 0;

    private Button nextButton;
    private TextView tutorialInstructions;

    @Override
    protected void initGameActivity() {
        mMustSaveGame = false;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tutorial;
    }
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
