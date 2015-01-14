package com.glevel.dungeonhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.adapters.MonsterAdapter;
import com.glevel.dungeonhero.data.characters.MonsterFactory;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;

import java.util.List;

public class BestiaryActivity extends MyActivity {

    private static final String TAG = "BestiaryActivity";

    private Game mGame;
    private List<Monster> mLstMonsters;

    /**
     * UI
     */
    private ImageView mStormsBg;
    private Runnable mStormEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestiary);

        mGame = (Game) getIntent().getExtras().getSerializable(Game.class.getName());

        mLstMonsters = MonsterFactory.getAll();

        setupUI();
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        // init list view
        ListView monstersList = (ListView) findViewById(R.id.monsters);
        MonsterAdapter monstersAdapter = new MonsterAdapter(this, R.layout.monster_item, mLstMonsters, mGame.getHero());
        monstersList.setAdapter(monstersAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStormEffect = ApplicationUtils.addStormBackgroundAtmosphere(mStormsBg, 150, 50);
    }

    @Override
    protected int[] getMusicResource() {
        return new int[]{R.raw.main_menu};
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStormsBg.removeCallbacks(mStormEffect);
    }

    @Override
    public void onBackPressed() {
        goBackToBookChooser();
    }

    private void goBackToBookChooser() {
        Intent intent = new Intent(BestiaryActivity.this, BookChooserActivity.class);
        intent.putExtra(Game.class.getName(), mGame);
        startActivity(intent);
        finish();
    }

}
