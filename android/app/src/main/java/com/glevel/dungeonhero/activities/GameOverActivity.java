package com.glevel.dungeonhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.utils.ApplicationUtils;

public class GameOverActivity extends MyActivity implements View.OnClickListener {

    private Runnable mStormEffect;
    private ImageView mStormsBg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        setupUI();
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        View retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(this);
        retryButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_in));

        View exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        exitButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_in));

        findViewById(R.id.grave).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_in));

        findViewById(R.id.title).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
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
            case R.id.retryButton:
                // TODO : retry dungeon
                finish();
                break;
            case R.id.exitButton:
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                break;
        }
    }

}
