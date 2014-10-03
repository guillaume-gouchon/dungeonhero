package com.glevel.dungeonhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.adapters.HeroesAdapter;
import com.glevel.dungeonhero.activities.fragments.StoryFragment;
import com.glevel.dungeonhero.game.data.GameData;
import com.glevel.dungeonhero.models.characters.heroes.Hero;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.utils.billing.InAppBillingHelper;
import com.glevel.dungeonhero.utils.billing.OnBillingServiceConnectedListener;
import com.glevel.dungeonhero.views.CustomCarousel;

import java.util.List;

public class NewGameActivity extends MyActivity implements OnBillingServiceConnectedListener {

    private ImageView mStormsBg;
    private Runnable mStormEffect;
    private InAppBillingHelper mInAppBillingHelper;
    private List<Hero> mHeroes;


    /**
     * Callbacks
     */
    private OnClickListener mOnHeroSelectedListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
            int position = Integer.parseInt("" + v.getTag(R.string.id));

            // TODO

            if (false) {
                mInAppBillingHelper.purchaseItem(mHeroes.get(position));
            }

            ApplicationUtils.openDialogFragment(NewGameActivity.this, new StoryFragment(), null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHeroes = GameData.getHeroes();

        setContentView(R.layout.activity_new_game);
        setupUI();

        mInAppBillingHelper = new InAppBillingHelper(this, this);
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        // init carousel
        CustomCarousel heroesCarousel = (CustomCarousel) findViewById(R.id.heroes);
        CustomCarousel.Adapter heroesAdapter = new HeroesAdapter(getApplicationContext(), R.layout.hero_chooser_item, mHeroes, mOnHeroSelectedListener);
        heroesCarousel.setAdapter(heroesAdapter);

        // start message animation
        findViewById(R.id.chooseHeroMessage).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.big_label_in_game));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStormEffect = ApplicationUtils.addStormBackgroundAtmosphere(mStormsBg, 150, 50);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStormsBg.removeCallbacks(mStormEffect);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInAppBillingHelper.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onBillingServiceConnected() {
        mInAppBillingHelper.doIOwn(mHeroes);
    }

}
