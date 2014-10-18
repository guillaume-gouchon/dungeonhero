package com.glevel.dungeonhero.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.adapters.BooksAdapter;
import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.utils.billing.InAppBillingHelper;
import com.glevel.dungeonhero.utils.billing.OnBillingServiceConnectedListener;
import com.glevel.dungeonhero.views.CustomAlertDialog;
import com.glevel.dungeonhero.views.CustomCarousel;

import java.util.List;

public class BookChooserActivity extends MyActivity implements OnBillingServiceConnectedListener {

    private ImageView mStormsBg;
    private Runnable mStormEffect;
    private InAppBillingHelper mInAppBillingHelper;
    private List<Book> mLstBooks;
    private SharedPreferences mSharedPrefs;
    private Dialog mGameMenuDialog;
    private CustomAlertDialog mTutorialDialog;

    /**
     * Callbacks
     */
    private OnClickListener mOnStorySelectedListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
            int position = Integer.parseInt("" + v.getTag(R.string.id));
            Book selectedBook = mLstBooks.get(position);
            if (position != 0 && mSharedPrefs.getInt(GameConstants.TUTORIAL_DONE, 0) == 0) {
                showTutorialDialog(selectedBook);
            } else {
                onBookSelected(selectedBook);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLstBooks = BookFactory.getAll();

        setContentView(R.layout.activity_book_chooser);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setupUI();

        mInAppBillingHelper = new InAppBillingHelper(this, this);
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        // init carousel
        CustomCarousel carousel = (CustomCarousel) findViewById(R.id.heroes);
        CustomCarousel.Adapter adapter = new BooksAdapter(getApplicationContext(), R.layout.book_chooser_item, mLstBooks, mOnStorySelectedListener);
        carousel.setAdapter(adapter);

        // start message animation
        findViewById(R.id.chooseBookMessage).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.big_label_in_game));
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

        if (mGameMenuDialog != null) {
            mGameMenuDialog.dismiss();
        }

        if (mTutorialDialog != null) {
            mTutorialDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInAppBillingHelper.onDestroy();
    }

    @Override
    public void onBackPressed() {
        openGameMenu();
    }

    @Override
    public void onBillingServiceConnected() {
        mInAppBillingHelper.doIOwn(mLstBooks);
    }

    private void showTutorialDialog(final Book selectedBook) {
        // ask user if he wants to do the tutorial as he is a noob
        mTutorialDialog = new CustomAlertDialog(this, R.style.Dialog, getString(R.string.ask_tutorial), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MusicManager.playSound(getApplicationContext(), R.raw.button_sound);

                if (which == R.id.okButton) {
                    // go to tutorial
                    dialog.dismiss();
                    onBookSelected(mLstBooks.get(0));
                } else {
                    dialog.dismiss();
                    onBookSelected(selectedBook);
                }
            }
        });
        mTutorialDialog.show();
        mSharedPrefs.edit().putInt(GameConstants.TUTORIAL_DONE, 1).commit();
    }

    private void openGameMenu() {
        mGameMenuDialog = new Dialog(this, R.style.FullScreenDialog);
        mGameMenuDialog.setContentView(R.layout.dialog_game_menu);
        mGameMenuDialog.setCancelable(true);
        Animation menuButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_in);

        // resume game button
        mGameMenuDialog.findViewById(R.id.resumeGameButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.resumeGameButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                mGameMenuDialog.dismiss();
            }
        });

        mGameMenuDialog.findViewById(R.id.leaveQuestButton).setVisibility(View.GONE);

        // exit button
        mGameMenuDialog.findViewById(R.id.exitButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.exitButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                startActivity(new Intent(BookChooserActivity.this, HomeActivity.class));
                finish();
            }
        });
        mGameMenuDialog.show();
        menuButtonAnimation.start();
    }

    private void onBookSelected(Book selectedBook) {
        if (selectedBook.isAvailable()) {
            Hero hero = (Hero) getIntent().getSerializableExtra(Hero.class.getName());
            Game game = new Game(hero, selectedBook);

            Intent intent = new Intent(BookChooserActivity.this, GameActivity.class);
            intent.putExtra(Game.class.getName(), game);
            startActivity(intent);
            finish();
        } else {
            mInAppBillingHelper.purchaseItem(selectedBook);
        }
    }

}
