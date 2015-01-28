package com.glevel.dungeonhero.activities;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.adapters.BooksAdapter;
import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.data.characters.HeroFactory;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.game.gui.GameMenu;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.providers.MyContentProvider;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.views.CustomAlertDialog;
import com.glevel.dungeonhero.views.CustomCarousel;

import java.util.List;

public class BookChooserActivity extends MyActivity implements OnClickListener {

    private static final String TAG = "BookChooserActivity";

    private Game mGame;
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
            int position = Integer.parseInt("" + v.getTag(R.string.id));
            Book selectedBook = mLstBooks.get(position);
            if (position != 0 && mSharedPrefs.getInt(GameConstants.TUTORIAL_DONE, 0) == 0) {
                showTutorialDialog(selectedBook);
            } else {
                onBookSelected(selectedBook);
            }
            mSharedPrefs.edit().putInt(GameConstants.TUTORIAL_DONE, 1).apply();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_chooser);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // retrieve active game
        mGame = (Game) getIntent().getExtras().getSerializable(Game.class.getName());
        mGame.setDungeon(null);
        Uri gameUri = getContentResolver().insert(MyContentProvider.URI_GAMES, mGame.toContentValues());
        mGame.setId(ContentUris.parseId(gameUri));

        // retrieve books
        mLstBooks = BookFactory.getAll();
        boolean hasFinishedTheGame = true;
        int score;
        for (Book book : mLstBooks) {
            if (mGame.getBooksDone().get(book.getId()) != null) {
                score = mGame.getBooksDone().get(book.getId());
                book.setBestScore(score);
                Log.d(TAG, "book " + book.getId() + ", score =" + score);
                if (score < 3) {
                    hasFinishedTheGame = false;
                }
            } else {
                hasFinishedTheGame = false;
            }
        }

        // unblock next character
        if (hasFinishedTheGame) {
            Log.d(TAG, "congratulations ! Game is finished");
            List<Hero> heroes = HeroFactory.getAll();
            for (int n = 0; n < heroes.size(); n++) {
                if (heroes.get(n).getIdentifier().equals(mGame.getHero().getIdentifier()) && n + 1 < heroes.size()) {
                    Hero unblockedHero = heroes.get(n + 1);
                    if (mSharedPrefs.getString(unblockedHero.getProductId(), null) == null) {
                        Dialog dialog = new Dialog(this, R.style.Dialog);
                        dialog.setContentView(R.layout.dialog_unlock_hero);
                        TextView title = (TextView) dialog.findViewById(R.id.title);
                        title.setText(getString(R.string.congratulations_unlock, getString(unblockedHero.getName(getResources()))));
                        title.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_three_stars, 0, unblockedHero.getImage(getResources()));
                        dialog.show();
                    }

                    mSharedPrefs.edit().putString(unblockedHero.getProductId(), "got it !").apply();
                    break;
                }
            }
        }

        setupUI();
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        // init carousel
        CustomCarousel carousel = (CustomCarousel) findViewById(R.id.books);
        CustomCarousel.Adapter adapter = new BooksAdapter(getApplicationContext(), R.layout.book_chooser_item, mLstBooks, mOnStorySelectedListener);
        carousel.setAdapter(adapter);

        // start big message animation
        findViewById(R.id.chooseBookMessage).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.big_label_in_game));

        findViewById(R.id.shop_button).setOnClickListener(this);
        findViewById(R.id.bestiary_button).setOnClickListener(this);

        TextView heroNameTV = (TextView) findViewById(R.id.hero_name);
        heroNameTV.setText(mGame.getHero().getHeroName());
        heroNameTV.setCompoundDrawablesWithIntrinsicBounds(mGame.getHero().getImage(getResources()), 0, 0, 0);
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

        getContentResolver().insert(MyContentProvider.URI_GAMES, mGame.toContentValues());

        mStormsBg.removeCallbacks(mStormEffect);

        if (mGameMenuDialog != null) {
            mGameMenuDialog.dismiss();
        }

        if (mTutorialDialog != null) {
            mTutorialDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        openGameMenu();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.shop_button:
                MusicManager.playSound(this, R.raw.button_sound);
                intent = new Intent(this, ShopActivity.class);
                intent.putExtra(Game.class.getName(), mGame);
                startActivity(intent);
                finish();
                break;

            case R.id.bestiary_button:
                MusicManager.playSound(this, R.raw.button_sound);
                intent = new Intent(this, BestiaryActivity.class);
                intent.putExtra(Game.class.getName(), mGame);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void showTutorialDialog(final Book selectedBook) {
        // ask user if he wants to do the tutorial as he is a noob
        mTutorialDialog = new CustomAlertDialog(this, R.style.Dialog, getString(R.string.ask_tutorial), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MusicManager.playSound(getApplicationContext(), R.raw.button_sound);

                if (which == R.id.ok_btn) {
                    // go to tutorial
                    dialog.dismiss();
                    onBookSelected(BookFactory.buildTutorial());
                } else {
                    dialog.dismiss();
                    onBookSelected(selectedBook);
                }
            }
        });
        mTutorialDialog.show();
    }

    private void openGameMenu() {
        mGameMenuDialog = new GameMenu(this, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookChooserActivity.this, HomeActivity.class));
                finish();
            }
        });

        mGameMenuDialog.show();
    }

    private void onBookSelected(Book selectedBook) {
        mGame.setBook(selectedBook);
        Intent intent = new Intent(this, selectedBook.isTutorial() ? TutorialActivity.class : GameActivity.class);
        intent.putExtra(Game.class.getName(), mGame);
        startActivity(intent);
        finish();
    }

}
