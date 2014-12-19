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

import com.glevel.dungeonhero.MyDatabase;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.adapters.BooksAdapter;
import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.utils.billing.InAppBillingHelper;
import com.glevel.dungeonhero.utils.billing.OnBillingServiceConnectedListener;
import com.glevel.dungeonhero.utils.database.DatabaseHelper;
import com.glevel.dungeonhero.views.CustomAlertDialog;
import com.glevel.dungeonhero.views.CustomCarousel;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.util.List;

public class BookChooserActivity extends BaseGameActivity implements OnBillingServiceConnectedListener, OnClickListener {

    private static final int REQUEST_ACHIEVEMENTS = 100;

    private Game mGame;
    private DatabaseHelper mDbHelper;
    private InAppBillingHelper mInAppBillingHelper;
    private List<Book> mLstBooks;
    private SharedPreferences mSharedPrefs;

    /**
     * UI
     */
    private ImageView mStormsBg;
    private Runnable mStormEffect;
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
        setContentView(R.layout.activity_book_chooser);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mDbHelper = new MyDatabase(getApplicationContext());

        // retrieve active game
        mGame = (Game) getIntent().getExtras().getSerializable(Game.class.getName());
        mGame.setDungeon(null);
        long gameId = mDbHelper.getRepository(MyDatabase.Repositories.GAME.toString()).save(mGame);
        mGame.setId(gameId);

        // retrieve books
        mLstBooks = BookFactory.getAll();
        for (Book book : mLstBooks) {
            if (mGame.getBooksDone().contains(book.getId())) {
                book.setDone(true);
            }
        }

        mInAppBillingHelper = new InAppBillingHelper(this, this);

        setupUI();
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        // init carousel
        CustomCarousel carousel = (CustomCarousel) findViewById(R.id.books);
        CustomCarousel.Adapter adapter = new BooksAdapter(getApplicationContext(), R.layout.book_chooser_item, mLstBooks, mOnStorySelectedListener);
        carousel.setAdapter(adapter);

        // start message animation
        findViewById(R.id.chooseBookMessage).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.big_label_in_game));

        findViewById(R.id.shop_button).setOnClickListener(this);
        findViewById(R.id.tavern_button).setOnClickListener(this);
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
        mSharedPrefs.edit().putInt(GameConstants.TUTORIAL_DONE, 1).apply();
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
            mGame.setBook(selectedBook);
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(Game.class.getName(), mGame);
            startActivity(intent);
            finish();
        } else {
            mInAppBillingHelper.purchaseItem(selectedBook);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_button:
                Intent intent = new Intent(this, ShopActivity.class);
                intent.putExtra(Game.class.getName(), mGame);
                startActivity(intent);
                finish();
                break;
            case R.id.tavern_button:
                startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), REQUEST_ACHIEVEMENTS);
                break;
        }
    }

    @Override
    public void onSignInFailed() {
    }

    @Override
    public void onSignInSucceeded() {
        findViewById(R.id.tavern_button).setVisibility(View.VISIBLE);
    }

}
