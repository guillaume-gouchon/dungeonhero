package com.glevel.dungeonhero.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.equipments.Armor;
import com.glevel.dungeonhero.models.items.equipments.Equipment;
import com.glevel.dungeonhero.models.items.equipments.weapons.Weapon;
import com.glevel.dungeonhero.models.items.requirements.Requirement;
import com.glevel.dungeonhero.models.items.requirements.StatRequirement;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.views.CustomAlertDialog;
import com.glevel.dungeonhero.views.HintTextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShopActivity extends MyActivity implements OnClickListener {

    private static final String TAG = "ShopActivity";

    private static final String LAST_TIME_SHOP_VISITED_PREFS = "LAST_TIME_SHOP_VISITED_PREFS";
    private static final String FILENAME_SHOP_ITEMS = "shop_items";
    private static final int NUMBER_OFFERS = 8;

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
        getOffers();
        updateOffers();
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        mOffers = (ViewGroup) findViewById(R.id.shop_offers);
        mBag = (ViewGroup) findViewById(R.id.bag);
        mDiscussionShop = (TextView) findViewById(R.id.discussion_shop);
        mGoldAmount = (TextView) findViewById(R.id.gold_amount);

        findViewById(R.id.back_button).setOnClickListener(this);
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

        if (mItemInfoDialog != null) {
            mItemInfoDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        goToBookChooser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                goToBookChooser();
                break;
        }

        if (v.getTag(R.string.sell_item) != null) {
            showItemInfo((Item) v.getTag(R.string.sell_item), true);
        } else if (v.getTag(R.string.buy_item) != null) {
            showItemInfo((Item) v.getTag(R.string.buy_item), false);
        }
    }

    private void goToBookChooser() {
        mDiscussionShop.setText(R.string.shop_outro);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ShopActivity.this, BookChooserActivity.class);
                intent.putExtra(Game.class.getName(), mGame);
                startActivity(intent);
                finish();
            }
        }, 300);
    }

    private void getOffers() {
        if (mSharedPrefs.getLong(LAST_TIME_SHOP_VISITED_PREFS, 0) == 0 || mSharedPrefs.getLong(LAST_TIME_SHOP_VISITED_PREFS, 0) - System.currentTimeMillis() > 10 * 3600 * 1000) {
            Log.d(TAG, "Creating new offers");
            Item item;
            for (int n = 0; n < NUMBER_OFFERS; n++) {
                do {
                    if (n >= NUMBER_OFFERS - 2) {
                        item = PotionFactory.getRandomPotion();
                    } else {
                        item = ItemFactory.getRandomItem(mGame.getHero().getLevel());
                    }
                } while (item == null);
                mItemsOffered.add(item);
            }
            mSharedPrefs.edit().putLong(LAST_TIME_SHOP_VISITED_PREFS, System.currentTimeMillis()).apply();
            saveItemsToFile();
        } else {
            // get items list from the file
            Log.d(TAG, "Getting existing offers");
            try {
                FileInputStream fis = openFileInput(FILENAME_SHOP_ITEMS);
                ObjectInputStream ois = new ObjectInputStream(fis);
                mItemsOffered = (List<Item>) ois.readObject();
                Log.d(TAG, "Found " + mItemsOffered.size() + " existing offers");
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveItemsToFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME_SHOP_ITEMS, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mItemsOffered);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
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
            mItemInfoDialog = new Dialog(this, R.style.DialogNoAnimation);
            mItemInfoDialog.setContentView(R.layout.in_game_item_info);
            mItemInfoDialog.setCancelable(true);

            TextView nameTV = (TextView) mItemInfoDialog.findViewById(R.id.name);
            if (item instanceof Equipment) {
                nameTV.setText(getString(item.getName(getResources())) + " (lvl " + ((Equipment) item).getLevel() + ")");
            } else {
                nameTV.setText(item.getName(getResources()));
            }
            nameTV.setCompoundDrawablesWithIntrinsicBounds(item.getImage(getResources()), 0, 0, 0);

            TextView descriptionTV = (TextView) mItemInfoDialog.findViewById(R.id.description);
            if (item.getDescription(getResources()) > 0) {
                descriptionTV.setText(item.getDescription(getResources()));
            } else {
                descriptionTV.setVisibility(View.GONE);
            }

            TextView dropButton = (TextView) mItemInfoDialog.findViewById(R.id.dropButton);
            dropButton.setVisibility(View.GONE);

            ViewGroup statsLayout = (ViewGroup) mItemInfoDialog.findViewById(R.id.stats);
            ViewGroup requirementsLayout = (ViewGroup) mItemInfoDialog.findViewById(R.id.requirements);
            int indexStats = 0, indexRequirements = 0;

            if (item instanceof Equipment) {
                final Equipment equipment = (Equipment) item;
                if (!mGame.getHero().canEquipItem(equipment)) {
                    nameTV.setTextColor(getResources().getColor(R.color.red));
                }

                // add unique stats
                if (item instanceof Weapon) {
                    Weapon weapon = (Weapon) item;
                    addStatToItemLayout(statsLayout.getChildAt(indexStats++), weapon.getMinDamage() + " - " + (weapon.getMinDamage() + weapon.getDeltaDamage()), R.drawable.ic_damage, R.string.damage, R.color.white);
                } else if (item instanceof Armor) {
                    Armor armor = (Armor) item;
                    addStatToItemLayout(statsLayout.getChildAt(indexStats++), "+" + armor.getProtection(), R.drawable.ic_armor, R.string.protection, R.color.green);
                }

                // add buffs
                for (Effect buff : equipment.getEffects()) {
                    if (buff instanceof PermanentEffect) {
                        addStatToItemLayout(statsLayout.getChildAt(indexStats++), (buff.getValue() > 0 ? "+" : "") + buff.getValue(), buff.getTarget().getImage(), buff.getTarget().getName(), buff.getValue() > 0 ? R.color.green : R.color.red);
                    } else if (buff instanceof StunEffect) {
                        addStatToItemLayout(statsLayout.getChildAt(indexStats++), (buff.getValue() > 0 ? "+" : "") + buff.getValue(), R.drawable.ic_stun, R.string.chance_stun, buff.getValue() > 0 ? R.color.green : R.color.red);
                    }
                }

                // add requirements
                for (Requirement requirement : equipment.getRequirements()) {
                    if (requirement instanceof StatRequirement) {
                        StatRequirement statRequirement = (StatRequirement) requirement;
                        addStatToItemLayout(requirementsLayout.getChildAt(indexRequirements++), getString(R.string.minimum, statRequirement.getValue()), statRequirement.getTarget().getImage(), statRequirement.getTarget().getName(), R.color.white);
                    }
                }
            }

            TextView actionButton = (TextView) mItemInfoDialog.findViewById(R.id.actionButton);
            actionButton.setText(isSelling ? getString(R.string.sell_item_for, item.getSellPrice()) : getString(R.string.buy_item_for, item.getPrice()));
            actionButton.setEnabled(isSelling || mGame.getHero().getGold() >= item.getPrice() && mGame.getHero().getItems().size() < Unit.NB_ITEMS_MAX_IN_BAG);
            actionButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog confirmationDialog = new CustomAlertDialog(ShopActivity.this, R.style.Dialog, getString(R.string.transaction_confirmation), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == R.id.okButton) {
                                dialog.dismiss();
                                if (isSelling) {
                                    sellItem(item);
                                } else {
                                    buyItem(item);
                                }
                            } else {
                                dialog.dismiss();
                            }
                            mItemInfoDialog.dismiss();
                        }
                    });
                    confirmationDialog.show();
                }
            });

            mItemInfoDialog.show();
        }
    }

    private void addStatToItemLayout(View view, String text, int image, int hint, int color) {
        HintTextView statView = (HintTextView) view;
        statView.setText(text);
        statView.setTextHint(hint);
        statView.setCompoundDrawablesWithIntrinsicBounds(image, 0, 0, 0);
        int colorResource = getResources().getColor(color);
        statView.setTextColor(colorResource);
        statView.setVisibility(View.VISIBLE);
    }

    private void buyItem(Item item) {
        mGame.getHero().addGold(-item.getPrice());
        mGame.getHero().getItems().add(item);
        mItemsOffered.set(mItemsOffered.indexOf(item), null);
        saveItemsToFile();
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
