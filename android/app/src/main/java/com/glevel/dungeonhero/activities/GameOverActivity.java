package com.glevel.dungeonhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.providers.MyContentProvider;
import com.glevel.dungeonhero.utils.ApplicationUtils;

public class GameOverActivity extends MyActivity implements View.OnClickListener {

    private Game mGame;

    /**
     * UI
     */
    private Runnable mStormEffect;
    private ImageView mStormsBg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        setupUI();

        mGame = (Game) getIntent().getSerializableExtra(Game.class.getName());

        // reset game to start of this chapter
        mGame.setDungeon(null);
        mGame.getBook().getActiveChapter().resetChapter();

        // save game
        getContentResolver().insert(MyContentProvider.URI_GAMES, mGame.toContentValues());
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        View retryButton = findViewById(R.id.retry_btn);
        retryButton.setOnClickListener(this);
        retryButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_in));

        View exitButton = findViewById(R.id.exit_btn);
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
    protected int[] getMusicResource() {
        return new int[]{R.raw.game_over};
    }

    @Override
    public void onPause() {
        super.onPause();
        mStormsBg.removeCallbacks(mStormEffect);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.retry_btn:
                intent = new Intent(this, mGame.getBook().getActivityClass());
                intent.putExtra(Game.class.getName(), mGame);
                startActivity(intent);
                finish();
                break;
            case R.id.exit_btn:
                intent = new Intent(this, BookChooserActivity.class);
                intent.putExtra(Game.class.getName(), mGame);
                startActivity(intent);
                finish();
                break;
        }
    }

}
