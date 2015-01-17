package com.glevel.dungeonhero.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.adapters.MonsterAdapter;
import com.glevel.dungeonhero.data.characters.MonsterFactory;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;

import java.util.List;

public class BestiaryActivity extends MyActivity implements View.OnClickListener {

    private static final String TAG = "BestiaryActivity";

    private Game mGame;
    private List<Monster> mLstMonsters;
    private SharedPreferences mSharedPrefs;

    /**
     * UI
     */
    private ImageView mStormsBg;
    private Runnable mStormEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestiary);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mGame = (Game) getIntent().getExtras().getSerializable(Game.class.getName());

        mLstMonsters = MonsterFactory.getAll();

        setupUI();
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        findViewById(R.id.back_button).setOnClickListener(this);

        ((ImageView) findViewById(R.id.hero_image)).setImageResource(mGame.getHero().getImage(getResources()));

        // init list view
        ListView monstersList = (ListView) findViewById(R.id.monsters);
        MonsterAdapter monstersAdapter = new MonsterAdapter(this, R.layout.monster_item, mLstMonsters, mGame.getHero());

        // add empty header view
        LinearLayout viewHeader = new LinearLayout(getApplicationContext());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ApplicationUtils.convertDpToPixels(getApplicationContext(), 50));
        viewHeader.setLayoutParams(lp);
        monstersList.addHeaderView(viewHeader, null, false);

        monstersList.setAdapter(monstersAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStormEffect = ApplicationUtils.addStormBackgroundAtmosphere(mStormsBg, 150, 50);
    }

    @Override
    protected int[] getMusicResource() {
        if (mSharedPrefs.getBoolean(GameConstants.GAME_PREFS_METAL_MUSIC, false)) {
            return new int[]{R.raw.main_menu_metal};
        } else {
            return new int[]{R.raw.main_menu};
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStormsBg.removeCallbacks(mStormEffect);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                MusicManager.playSound(this, R.raw.button_sound);
                goBackToBookChooser();
                break;
        }
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
