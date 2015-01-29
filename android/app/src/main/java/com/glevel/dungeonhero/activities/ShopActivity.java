package com.glevel.dungeonhero.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.MyActivity;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.data.items.ItemFactory;
import com.glevel.dungeonhero.data.items.PotionFactory;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.game.gui.items.ItemInfoInShop;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.views.CustomAlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShopActivity extends MyActivity implements OnClickListener {

    private static final String TAG = "ShopActivity";

    private static final String LAST_TIME_SHOP_VISITED_PREFS = "LAST_TIME_SHOP_VISITED_PREFS";
    private static final String FILENAME_SHOP_ITEMS = "shop_items";
    private static final int NUMBER_OFFERS = 8;
    private static final int REFRESH_SHOP_PERIOD = 10 * 3600 * 1000;

    private Game mGame;
    private List<Item> mItemsOffered = new ArrayList<>(8);
    private SharedPreferences mSharedPrefs;

    /**
     * UI
     */
    private ImageView mStormsBg;
    private TextView mDiscussionShop, mGoldAmount;
    private Runnable mStormEffect;
    private ViewGroup mOffers, mBag;
    private Dialog mItemInfoDialog;

    public static void resetShop(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(LAST_TIME_SHOP_VISITED_PREFS, 0).apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mGame = (Game) getIntent().getExtras().getSerializable(Game.class.getName());

        setupUI();

        updateGoldAmount();
        updateBag();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                getOffers();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateOffers();
                ApplicationUtils.saveToLocalFile(getApplicationContext(), FILENAME_SHOP_ITEMS, mItemsOffered);
            }
        }.execute();
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        mOffers = (ViewGroup) findViewById(R.id.shop_offers);
        mBag = (ViewGroup) findViewById(R.id.bag);
        mDiscussionShop = (TextView) findViewById(R.id.discussion_shop);
        mGoldAmount = (TextView) findViewById(R.id.gold_amount);

        findViewById(R.id.back_button).setOnClickListener(this);

        ((TextView) findViewById(R.id.hero_name)).setCompoundDrawablesWithIntrinsicBounds(mGame.getHero().getImage(getResources()), 0, 0, 0);
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

        if (mItemInfoDialog != null) {
            mItemInfoDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        goBackToBookChooser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                MusicManager.playSound(this, R.raw.button_sound);
                goBackToBookChooser();
                break;
        }

        if (v.getTag(R.string.sell_item) != null) {
            showItemInfo((Item) v.getTag(R.string.sell_item), true);
        } else if (v.getTag(R.string.buy_item) != null) {
            showItemInfo((Item) v.getTag(R.string.buy_item), false);
        }
    }

    private void goBackToBookChooser() {
        mDiscussionShop.setText(R.string.shop_outro);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ShopActivity.this, BookChooserActivity.class);
                intent.putExtra(Game.class.getName(), mGame);
                startActivity(intent);
                finish();
            }
        }, 200);
    }

    private void getOffers() {
        if (mSharedPrefs.getLong(LAST_TIME_SHOP_VISITED_PREFS, 0) == 0 || mSharedPrefs.getLong(LAST_TIME_SHOP_VISITED_PREFS, 0) - System.currentTimeMillis() > REFRESH_SHOP_PERIOD) {
            Log.d(TAG, "Creating new offers");
            Item item;
            for (int n = 0; n < NUMBER_OFFERS; n++) {
                do {
                    if (n >= NUMBER_OFFERS - 1) {
                        item = PotionFactory.buildHealingPotion();
                    } else if (n >= NUMBER_OFFERS - 2) {
                        item = PotionFactory.getRandomPotion();
                    } else {
                        item = ItemFactory.getRandomItem(mGame.getHero().getLevel());
                    }
                } while (item == null);
                mItemsOffered.add(item);
            }
            mSharedPrefs.edit().putLong(LAST_TIME_SHOP_VISITED_PREFS, System.currentTimeMillis()).apply();
        } else {
            // get items list from the file
            Log.d(TAG, "Getting existing offers from local file");
            mItemsOffered = (List<Item>) ApplicationUtils.readFromLocalFile(getApplicationContext(), FILENAME_SHOP_ITEMS);
            Log.d(TAG, "Found " + mItemsOffered.size() + " existing offers");
        }
    }

    private void updateGoldAmount() {
        mGoldAmount.setText("" + mGame.getHero().getGold());
    }

    private void updateOffers() {
        for (int n = 0; n < NUMBER_OFFERS; n++) {
            updateItemLayout(mOffers.getChildAt(n), mItemsOffered.get(n), false);
        }
    }

    private void updateBag() {
        Hero hero = mGame.getHero();
        Item item;
        for (int n = 0; n < 5; n++) {
            updateItemLayout(mBag.getChildAt(n), hero.getEquipments()[n], true);
        }

        for (int n = 5; n < mBag.getChildCount(); n++) {
            item = null;
            if (n - 5 < hero.getItems().size()) {
                item = hero.getItems().get(n - 5);
            }
            updateItemLayout(mBag.getChildAt(n), item, true);
        }
    }

    private void updateItemLayout(View itemView, Item item, boolean isSellingItem) {
        View background = itemView.findViewById(R.id.background);
        ImageView image = (ImageView) itemView.findViewById(R.id.image);

        itemView.setTag(isSellingItem ? R.string.sell_item : R.string.buy_item, item);

        if (item != null) {
            background.setBackgroundColor(getResources().getColor(item.getColor()));
            image.setImageResource(item.getImage(getResources()));
            itemView.setEnabled(true);
            itemView.setOnClickListener(this);
        } else if (itemView.isEnabled()) {
            background.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            image.setImageResource(0);
            itemView.setEnabled(false);
            itemView.setOnClickListener(null);
        }
    }

    public void showItemInfo(final Item item, final boolean isSelling) {
        if (mItemInfoDialog == null || !mItemInfoDialog.isShowing()) {
            mItemInfoDialog = new ItemInfoInShop(this, item, mGame.getHero(), isSelling, new ItemInfoInShop.OnItemActionSelected() {
                @Override
                public void onActionExecuted(ItemInfoInShop.ItemActionsInShop action) {
                    Dialog confirmationDialog = new CustomAlertDialog(ShopActivity.this, R.style.Dialog,
                            getString(R.string.transaction_confirmation, getString(isSelling ? R.string.sell : R.string.buy), getString(item.getName(getResources()))),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (which == R.id.ok_btn) {
                                        if (isSelling) {
                                            sellItem(item);
                                        } else {
                                            buyItem(item);
                                        }
                                        mItemInfoDialog.dismiss();
                                    }
                                }
                            });
                    confirmationDialog.show();
                }
            });

            mItemInfoDialog.show();
        }
    }

    private void buyItem(Item item) {
        mGame.getHero().addGold(-item.getPrice());
        mGame.getHero().getItems().add(item);
        mItemsOffered.set(mItemsOffered.indexOf(item), null);
        ApplicationUtils.saveToLocalFile(getApplicationContext(), FILENAME_SHOP_ITEMS, mItemsOffered);
        updateGoldAmount();
        updateBag();
        updateOffers();
    }

    private void sellItem(Item item) {
        mGame.getHero().addGold(item.getSellPrice());
        if (mGame.getHero().getItems().indexOf(item) >= 0) {
            mGame.getHero().getItems().remove(item);
        } else {
            for (int n = 0; n < mGame.getHero().getEquipments().length; n++) {
                if (item == mGame.getHero().getEquipments()[n]) {
                    mGame.getHero().getEquipments()[n] = null;
                    break;
                }
            }
        }
        updateGoldAmount();
        updateBag();
    }

}
