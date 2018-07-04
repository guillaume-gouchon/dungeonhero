package com.glevel.dungeonhero.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.glevel.dungeonhero.BaseActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.adapters.HeroesAdapter;
import com.glevel.dungeonhero.data.characters.HeroFactory;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.views.CustomCarousel;

import java.util.List;

public class NewGameActivity extends BaseActivity {

    private List<Hero> mLstHeroes;
    private SharedPreferences mSharedPrefs;

    /**
     * UI
     */
    private ImageView mStormsBg;
    private Runnable mStormEffect;
    private Dialog mHeroNameDialog;

    /**
     * Callbacks
     */
    private final OnClickListener mOnHeroSelectedListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
            int position = Integer.parseInt("" + v.getTag(R.string.id));
            Hero selectedHero = mLstHeroes.get(position);
            showNameInputDialog(selectedHero);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mLstHeroes = HeroFactory.getAll();

        setupUI();
    }

    private void setupUI() {
        mStormsBg = findViewById(R.id.storms);

        // init carousel
        CustomCarousel heroesCarousel = findViewById(R.id.heroes);
        HeroesAdapter mHeroesAdapter = new HeroesAdapter(this, R.layout.hero_chooser_item, mLstHeroes, mOnHeroSelectedListener);
        heroesCarousel.setAdapter(mHeroesAdapter);

        // start message animation
        findViewById(R.id.chooseHeroMessage).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.big_label_in_game));
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

        if (mHeroNameDialog != null) {
            mHeroNameDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void showNameInputDialog(final Hero selectedHero) {
        mHeroNameDialog = new Dialog(this, R.style.Dialog);
        mHeroNameDialog.setContentView(R.layout.dialog_hero_name_input);

        final EditText heroNameInput = mHeroNameDialog.findViewById(R.id.heroNameInput);
        heroNameInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        ApplicationUtils.showKeyboard(this, heroNameInput);
        heroNameInput.setOnEditorActionListener((v, actionId, event) -> {
            MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
            if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || event != null
                    && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                validateHeroName(selectedHero, heroNameInput.getEditableText().toString());
                return true;
            }
            return false;
        });

        mHeroNameDialog.findViewById(R.id.ok_btn).setOnClickListener(v -> {
            MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
            validateHeroName(selectedHero, heroNameInput.getEditableText().toString());
        });

        mHeroNameDialog.setCancelable(true);
        mHeroNameDialog.show();
    }

    private void validateHeroName(Hero hero, String name) {
        if (!name.isEmpty()) {
            name = name.substring(0, 1).toUpperCase() + (name.length() > 1 ? name.substring(1).toLowerCase() : "");
            mHeroNameDialog.dismiss();
            hero.setHeroName(name);
            launchNewGame(hero);
        }
    }

    private void launchNewGame(Hero hero) {
        Game game = new Game();
        game.setHero(hero);
        Intent intent = new Intent(NewGameActivity.this, BookChooserActivity.class);
        intent.putExtra(Game.class.getName(), game);
        startActivity(intent);
        finish();
    }

}
