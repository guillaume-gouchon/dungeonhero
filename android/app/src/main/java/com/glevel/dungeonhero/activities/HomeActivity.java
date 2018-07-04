package com.glevel.dungeonhero.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.glevel.dungeonhero.BaseActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.fragments.LoadGameFragment;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.providers.MyContentProvider;
import com.glevel.dungeonhero.providers.MyDatabaseHelper;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;

public class HomeActivity extends BaseActivity implements OnClickListener, LoadGameFragment.FragmentCallbacks {

    private SharedPreferences mSharedPrefs;

    /**
     * UI
     */
    private ScreenState mScreenState = ScreenState.HOME;
    private Animation mMainButtonAnimationRightIn, mMainButtonAnimationRightOut, mMainButtonAnimationLeftIn, mMainButtonAnimationLeftOut;
    private Animation mFadeOutAnimation, mFadeInAnimation;
    private Button mNewGameButton, mSettingsButton, mLoadGameButton;
    private ViewGroup mSettingsLayout;
    private View mBackButton, mAppNameView;
    private Dialog mAboutDialog = null;
    private ImageView mStormsBg;
    private Runnable mStormEffect;
    private Cursor mGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setupUI();

        ApplicationUtils.showRateDialogIfNeeded(this);

        showMainHomeButtons();
    }

    private void retrieveLoadGames() {
        if (MyDatabaseHelper.isDataBaseReady(getApplicationContext())) {
            new AsyncTask<Void, Void, Cursor>() {
                @Override
                protected Cursor doInBackground(Void... params) {
                    return getContentResolver().query(MyContentProvider.URI_GAMES, new String[]{Game.COLUMN_ID}, null, null, Game.COLUMN_ID + " LIMIT 1");
                }

                @Override
                protected void onPostExecute(Cursor games) {
                    super.onPostExecute(games);
                    mGames = games;
                    mLoadGameButton.setEnabled(games.getCount() > 0);
                }
            }.execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStormsBg.removeCallbacks(mStormEffect);

        if (mAboutDialog != null) {
            mAboutDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.isShown()) {
            switch (v.getId()) {
                case R.id.newGameButton:
                    startActivity(new Intent(this, NewGameActivity.class));
                    finish();
                    break;

                case R.id.loadGameButton:
                    ApplicationUtils.openDialogFragment(this, new LoadGameFragment(), null);
                    hideMainHomeButtons();
                    mBackButton.setVisibility(View.GONE);
                    mBackButton.setAnimation(null);
                    break;

                case R.id.settingsButton:
                    showSettings();
                    break;

                case R.id.backButton:
                    onBackPressed();
                    break;

                case R.id.aboutButton:
                    openAboutDialog();
                    break;

                case R.id.rateButton:
                    ApplicationUtils.rateTheApp(this);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        switch (mScreenState) {
            case HOME:
                super.onBackPressed();
                break;

            case SETTINGS:
                showMainHomeButtons();
                hideSettings();
                break;
        }
    }

    private void setupUI() {
        mStormsBg = findViewById(R.id.storms);

        mMainButtonAnimationRightIn = AnimationUtils.loadAnimation(this, R.anim.main_btn_right_in);
        mMainButtonAnimationLeftIn = AnimationUtils.loadAnimation(this, R.anim.main_btn_left_in);
        mMainButtonAnimationRightOut = AnimationUtils.loadAnimation(this, R.anim.main_btn_right_out);
        mMainButtonAnimationLeftOut = AnimationUtils.loadAnimation(this, R.anim.main_btn_left_out);

        mSettingsLayout = findViewById(R.id.settingsLayout);

        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        mNewGameButton = findViewById(R.id.newGameButton);
        mNewGameButton.setOnClickListener(this);

        mLoadGameButton = findViewById(R.id.loadGameButton);
        mLoadGameButton.setOnClickListener(this);

        mSettingsButton = findViewById(R.id.settingsButton);
        mSettingsButton.setOnClickListener(this);

        mBackButton = findViewById(R.id.backButton);
        mBackButton.setOnClickListener(this);

        mAppNameView = findViewById(R.id.appName);

        // volume radio buttons
        RadioGroup radioMusicVolume = findViewById(R.id.musicVolume);
        // update radio buttons states according to the music preference
        int musicVolume = mSharedPrefs.getInt(GameConstants.GAME_PREFS_KEY_MUSIC_VOLUME, GameConstants.MusicStates.ON.ordinal());
        ((RadioButton) radioMusicVolume.getChildAt(musicVolume)).setChecked(true);
        radioMusicVolume.setOnCheckedChangeListener((group, checkedId) -> {
            // enable / disable sound in preferences
            GameConstants.MusicStates newMusicState;
            switch (checkedId) {
                case R.id.musicOn:
                    newMusicState = GameConstants.MusicStates.ON;
                    break;
                default:
                    newMusicState = GameConstants.MusicStates.OFF;
                    break;
            }
            mSharedPrefs.edit().putInt(GameConstants.GAME_PREFS_KEY_MUSIC_VOLUME, newMusicState.ordinal()).apply();
            if (newMusicState == GameConstants.MusicStates.ON) {
                MusicManager.playMusic(HomeActivity.this, getMusicResource());
            } else {
                MusicManager.release();
            }
        });

        // screen orientation in game
        RadioGroup screenOrientationRadioGroup = findViewById(R.id.landscapeMode);
        // update radio buttons states according to the preferences
        boolean isLandscapeMode = mSharedPrefs.getBoolean(GameConstants.GAME_PREFS_LANDSCAPE, false);
        int index = isLandscapeMode ? 1 : 0;
        ((RadioButton) screenOrientationRadioGroup.getChildAt(index)).setChecked(true);
        screenOrientationRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // enable / disable sound in preferences
            MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
            boolean isLandscapeMode1 = checkedId == R.id.landcape;
            Editor editor = mSharedPrefs.edit();
            editor.putBoolean(GameConstants.GAME_PREFS_LANDSCAPE, isLandscapeMode1);
            editor.apply();
        });

        // type of music
        RadioGroup musicTypeRadioGroup = findViewById(R.id.musicType);
        // update radio buttons states according to the preferences
        boolean isMetal = mSharedPrefs.getBoolean(GameConstants.GAME_PREFS_METAL_MUSIC, false);
        index = isMetal ? 1 : 0;
        ((RadioButton) musicTypeRadioGroup.getChildAt(index)).setChecked(true);
        musicTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // enable / disable sound in preferences
            MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
            boolean isMetal1 = checkedId == R.id.metal;
            Editor editor = mSharedPrefs.edit();
            editor.putBoolean(GameConstants.GAME_PREFS_METAL_MUSIC, isMetal1);
            editor.apply();
        });

        // settings buttons
        findViewById(R.id.aboutButton).setOnClickListener(this);
        findViewById(R.id.rateButton).setOnClickListener(this);
    }

    private void openAboutDialog() {
        mAboutDialog = new Dialog(this, R.style.Dialog);
        mAboutDialog.setCancelable(true);
        mAboutDialog.setContentView(R.layout.dialog_about);
        // activate the dialog links
        TextView creditsTV = mAboutDialog.findViewById(R.id.aboutCredits);
        creditsTV.setMovementMethod(LinkMovementMethod.getInstance());
        TextView contactTV = mAboutDialog.findViewById(R.id.aboutContact);
        contactTV.setMovementMethod(LinkMovementMethod.getInstance());
        TextView sourcesTV = mAboutDialog.findViewById(R.id.aboutSources);
        sourcesTV.setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) mAboutDialog.findViewById(R.id.version)).setText(getString(R.string.about_version, ApplicationUtils.getAppVersion(getApplicationContext())));
        mAboutDialog.show();
    }

    private void showButton(View view, boolean fromRight) {
        if (fromRight) {
            view.startAnimation(mMainButtonAnimationRightIn);
        } else {
            view.startAnimation(mMainButtonAnimationLeftIn);
        }
        view.setVisibility(View.VISIBLE);
        view.setEnabled(true);
    }

    private void hideButton(final View view, boolean toRight) {
        if (toRight) {
            view.startAnimation(mMainButtonAnimationRightOut);
        } else {
            mMainButtonAnimationLeftOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setAnimation(null);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(mMainButtonAnimationLeftOut);
        }
        view.setVisibility(View.GONE);
        view.setEnabled(false);
    }

    private void showMainHomeButtons() {
        mScreenState = ScreenState.HOME;
        mBackButton.startAnimation(mFadeOutAnimation);
        mBackButton.setVisibility(View.GONE);
        mAppNameView.startAnimation(mFadeInAnimation);
        mAppNameView.setVisibility(View.VISIBLE);
        showButton(mNewGameButton, true);
        showButton(mLoadGameButton, false);
        retrieveLoadGames();

        showButton(mSettingsButton, true);

        mLoadGameButton.setEnabled(mGames != null && mGames.getCount() > 0);
    }

    private void hideMainHomeButtons() {
        mAppNameView.startAnimation(mFadeOutAnimation);
        mAppNameView.setVisibility(View.GONE);
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.startAnimation(mFadeInAnimation);
        hideButton(mNewGameButton, true);
        hideButton(mLoadGameButton, false);
        hideButton(mSettingsButton, true);
    }

    private void showSettings() {
        mScreenState = ScreenState.SETTINGS;
        mSettingsLayout.setVisibility(View.VISIBLE);
        mSettingsLayout.startAnimation(mFadeInAnimation);
        hideMainHomeButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // init storm effect
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

    private void hideSettings() {
        mSettingsLayout.setVisibility(View.GONE);
        mSettingsLayout.startAnimation(mFadeOutAnimation);
    }

    @Override
    public void OnFragmentClosed() {
        showMainHomeButtons();
        mBackButton.setVisibility(View.GONE);
        mBackButton.setAnimation(null);
    }

    private enum ScreenState {
        HOME, SOLO, SETTINGS
    }

}
