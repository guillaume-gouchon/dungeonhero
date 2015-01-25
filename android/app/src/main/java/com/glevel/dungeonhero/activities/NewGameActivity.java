package com.glevel.dungeonhero.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.adapters.HeroesAdapter;
import com.glevel.dungeonhero.data.characters.HeroFactory;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.utils.billing.InAppBillingHelper;
import com.glevel.dungeonhero.utils.billing.OnBillingServiceConnectedListener;
import com.glevel.dungeonhero.views.CustomCarousel;

import java.util.List;

public class NewGameActivity extends MyActivity implements OnBillingServiceConnectedListener {

    private InAppBillingHelper mInAppBillingHelper;
    private List<Hero> mLstHeroes;
    private SharedPreferences mSharedPrefs;
    private HeroesAdapter mHeroesAdapter;

    /**
     * UI
     */
    private ImageView mStormsBg;
    private Runnable mStormEffect;
    private AlertDialog mHeroNameDialog;
    private Dialog mBuyDialog;

    /**
     * Callbacks
     */
    private OnClickListener mOnHeroSelectedListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
            int position = Integer.parseInt("" + v.getTag(R.string.id));
            Hero selectedHero = mLstHeroes.get(position);
            if (selectedHero.isAvailable()) {
                showNameInputDialog(selectedHero);
            } else {
                showBuyHeroPopup(selectedHero);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mLstHeroes = HeroFactory.getAll();

        setupUI();

        mInAppBillingHelper = new InAppBillingHelper(this, this);
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        // init carousel
        CustomCarousel heroesCarousel = (CustomCarousel) findViewById(R.id.heroes);
        mHeroesAdapter = new HeroesAdapter(this, R.layout.hero_chooser_item, mLstHeroes, mOnHeroSelectedListener);
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

        if (mBuyDialog != null) {
            mBuyDialog.dismiss();
        }
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
        mInAppBillingHelper.doIOwn(mLstHeroes);
        mHeroesAdapter.notifyDataSetChanged();
    }

    private void showNameInputDialog(final Hero selectedHero) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_hero_name_input, null);

        mHeroNameDialog = new AlertDialog.Builder(this, R.style.Dialog).setView(view).create();

        final EditText heroNameInput = (EditText) view.findViewById(R.id.heroNameInput);
        heroNameInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        ApplicationUtils.showKeyboard(this, heroNameInput);
        heroNameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    validateHeroName(selectedHero, heroNameInput.getEditableText().toString());
                    return true;
                }
                return false;
            }
        });

        view.findViewById(R.id.ok_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                validateHeroName(selectedHero, heroNameInput.getEditableText().toString());
            }
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


    private void showBuyHeroPopup(final Hero selectedHero) {
        mBuyDialog = new Dialog(this, R.style.Dialog);
        mBuyDialog.setCancelable(true);
        mBuyDialog.setContentView(R.layout.dialog_buy_hero);

        TextView buyHeroButton = (TextView) mBuyDialog.findViewById(R.id.buy_hero);
        buyHeroButton.setText(getString(R.string.buy_hero, getString(selectedHero.getName(getResources()))));
        buyHeroButton.setCompoundDrawablesWithIntrinsicBounds(0, selectedHero.getImage(getResources()), 0, 0);
        buyHeroButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(NewGameActivity.this, R.raw.button_sound);
                mInAppBillingHelper.purchaseItem(selectedHero);
            }
        });

        mBuyDialog.findViewById(R.id.buy_all_heroes).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(NewGameActivity.this, R.raw.button_sound);
                mInAppBillingHelper.purchaseItem(InAppBillingHelper.BUY_ALL_HEROES_IN_APP_ID);
            }
        });

        for (int n = 0; n < mLstHeroes.size(); n++) {
            if (mLstHeroes.get(n) == selectedHero) {
                ((TextView) mBuyDialog.findViewById(R.id.buy_option)).setText(getString(R.string.unblock_hero, getString(mLstHeroes.get(n - 1).getName(getResources()))));
                break;
            }
        }


        mBuyDialog.show();
    }

}
