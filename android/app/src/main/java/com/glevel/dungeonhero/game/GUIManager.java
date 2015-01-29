package com.glevel.dungeonhero.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.BookChooserActivity;
import com.glevel.dungeonhero.activities.HomeActivity;
import com.glevel.dungeonhero.activities.ShopActivity;
import com.glevel.dungeonhero.activities.fragments.StoryFragment;
import com.glevel.dungeonhero.activities.games.GameActivity;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.MyBaseGameActivity;
import com.glevel.dungeonhero.game.base.interfaces.OnActionExecuted;
import com.glevel.dungeonhero.game.base.interfaces.OnDiscussionReplySelected;
import com.glevel.dungeonhero.game.gui.DungeonMap;
import com.glevel.dungeonhero.game.gui.GameMenu;
import com.glevel.dungeonhero.game.gui.HeroInfo;
import com.glevel.dungeonhero.game.gui.Loading;
import com.glevel.dungeonhero.game.gui.RewardDialog;
import com.glevel.dungeonhero.game.gui.discussions.RiddleBox;
import com.glevel.dungeonhero.game.gui.discussions.SimpleDiscussionBox;
import com.glevel.dungeonhero.game.gui.items.ItemInfoInGame;
import com.glevel.dungeonhero.game.gui.skills.ImproveSkill;
import com.glevel.dungeonhero.game.gui.skills.UseSkill;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.dungeons.Room;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.equipments.Equipment;
import com.glevel.dungeonhero.models.items.equipments.weapons.TwoHandedWeapon;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.views.CustomAlertDialog;
import com.glevel.dungeonhero.views.LifeBar;

public class GUIManager {

    private static final String TAG = "GUIManager";

    private MyBaseGameActivity mGameActivity;
    private Resources mResources;
    private Hero mHero;

    private Dialog mLoadingScreen, mGameMenuDialog, mHeroInfoDialog, mDiscussionDialog,
            mRewardDialog, mBagDialog, mItemInfoDialog, mNewLevelDialog, mDungeonMapDialog;
    private TextView mBigLabel;
    private Animation mBigLabelAnimation;
    private ViewGroup mSelectedElementLayout, mActiveHeroLayout, mQueueLayout;
    private ViewGroup mSkillButtonsLayout;
    private CustomAlertDialog mConfirmDialog;

    public GUIManager(MyBaseGameActivity activity) {
        mGameActivity = activity;
        mResources = mGameActivity.getResources();
    }

    public void initGUI() {
        mQueueLayout = (ViewGroup) mGameActivity.findViewById(R.id.queue);
        mSelectedElementLayout = (ViewGroup) mGameActivity.findViewById(R.id.selectedElement);

        mActiveHeroLayout = (ViewGroup) mGameActivity.findViewById(R.id.hero);
        mActiveHeroLayout.setOnClickListener(mGameActivity);

        mGameActivity.findViewById(R.id.bag).setOnClickListener(mGameActivity);
        mGameActivity.findViewById(R.id.map).setOnClickListener(mGameActivity);

        // setup big label TV
        mBigLabelAnimation = AnimationUtils.loadAnimation(mGameActivity, R.anim.big_label_in_game);
        mBigLabel = (TextView) mGameActivity.findViewById(R.id.bigLabel);

        mSkillButtonsLayout = (ViewGroup) mGameActivity.findViewById(R.id.skillButtonsLayout);
    }

    public void setData(Hero hero) {
        mHero = hero;
    }

    public void displayBigLabel(final String text, final int color) {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBigLabel.setVisibility(View.VISIBLE);
                mBigLabel.setText("" + text);
                mBigLabel.setTextColor(mResources.getColor(color));
                mBigLabel.startAnimation(mBigLabelAnimation);
            }
        });
    }

    public void hideLoadingScreen() {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingScreen.dismiss();
            }
        });
    }

    public void openGameMenu() {
        mGameMenuDialog = new GameMenu(mGameActivity, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaveQuestConfirmDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameActivity.startActivity(new Intent(mGameActivity, HomeActivity.class));
                mGameActivity.finish();
            }
        });

        mGameMenuDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mGameActivity.resumeGame();
            }
        });

        mGameMenuDialog.show();
    }

    public void showLeaveQuestConfirmDialog() {
        mConfirmDialog = new CustomAlertDialog(mGameActivity, R.style.Dialog, mGameActivity.getString(R.string.confirm_leave_quest),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == R.id.ok_btn) {
                            Intent intent = new Intent(mGameActivity, BookChooserActivity.class);
                            mGameActivity.getGame().setHero(mHero);
                            intent.putExtra(Game.class.getName(), mGameActivity.getGame());
                            mGameActivity.startActivity(intent);
                            mGameActivity.finish();
                        }
                        dialog.dismiss();
                    }
                });
        mConfirmDialog.show();
    }

    public void showFinishQuestConfirmDialog() {
        mConfirmDialog = new CustomAlertDialog(mGameActivity, R.style.Dialog, mGameActivity.getString(R.string.confirm_finish_quest),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == R.id.ok_btn) {
                            // finishing quest is confirmed
                            final Book activeBook = mGameActivity.getGame().getBook();
                            boolean hasNextChapter = activeBook.hasNextChapter();

                            // show outro text
                            if (activeBook.getActiveChapter().getOutroText(mResources) > 0) {
                                showIntrospection(mGameActivity.getGame().getBook().getActiveChapter().getOutroText(mResources),
                                        hasNextChapter ? R.string.go_to_next_floor : R.string.finish_quest,
                                        new OnActionExecuted() {
                                            @Override
                                            public void onActionDone(boolean success) {
                                                finishQuest(activeBook);
                                            }
                                        });
                            } else {
                                finishQuest(activeBook);
                            }
                        }
                        dialog.dismiss();
                    }
                });
        mConfirmDialog.show();
    }

    private void finishQuest(Book activeBook) {
        Game game = mGameActivity.getGame();
        game.setHero(mHero);
        game.setDungeon(null);

        boolean hasNextChapter = activeBook.goToNextChapter();
        if (hasNextChapter) {
            // go to next floor
            Intent intent = new Intent(mGameActivity, GameActivity.class);
            intent.putExtra(Game.class.getName(), game);
            mGameActivity.startActivity(intent);
            mGameActivity.finish();
        } else {
            // book is finished go to book chooser activity
            game.finishBook();

            // reset shop when one quest is finished
            ShopActivity.resetShop(mGameActivity.getApplicationContext());

            if (activeBook.getOutroText(mResources) > 0) {
                // show outro text
                Bundle args = new Bundle();
                args.putInt(StoryFragment.ARGUMENT_STORY, activeBook.getOutroText(mResources));
                args.putBoolean(StoryFragment.ARGUMENT_IS_OUTRO, true);
                ApplicationUtils.openDialogFragment(mGameActivity, new StoryFragment(), args);
            } else {
                // go directly to the book chooser
                Intent intent = new Intent(mGameActivity, BookChooserActivity.class);
                intent.putExtra(Game.class.getName(), game);
                mGameActivity.startActivity(intent);
                mGameActivity.finish();
            }
        }
    }

    public void showChapterIntro(final OnActionExecuted callback) {
        showIntrospection(mGameActivity.getGame().getBook().getActiveChapter().getIntroText(mResources), R.string.ok, callback);
    }

    public void showIntrospection(int text, int okButtonText, final OnActionExecuted callback) {
        mConfirmDialog = new CustomAlertDialog(mGameActivity, R.style.Dialog, mGameActivity.getString(text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (callback != null) {
                            callback.onActionDone(true);
                        }
                    }
                });
        TextView nameTV = (TextView) mConfirmDialog.findViewById(R.id.name);
        nameTV.setCompoundDrawablesWithIntrinsicBounds(mHero.getImage(mResources), 0, 0, 0);
        nameTV.setText(mHero.getHeroName());
        ((TextView) mConfirmDialog.findViewById(R.id.ok_btn)).setText(okButtonText);
        mConfirmDialog.findViewById(R.id.cancel_btn).setVisibility(View.GONE);
        mConfirmDialog.show();
    }

    public void onPause() {
        if (mGameMenuDialog != null) {
            mGameMenuDialog.dismiss();
        }
        if (mLoadingScreen != null) {
            mLoadingScreen.dismiss();
        }
        if (mHeroInfoDialog != null) {
            mHeroInfoDialog.dismiss();
        }
        if (mDiscussionDialog != null) {
            mDiscussionDialog.dismiss();
        }
        if (mRewardDialog != null) {
            mRewardDialog.dismiss();
        }
        if (mBagDialog != null) {
            mBagDialog.dismiss();
        }
        if (mItemInfoDialog != null) {
            mItemInfoDialog.dismiss();
        }
        if (mNewLevelDialog != null) {
            mNewLevelDialog.dismiss();
        }
        if (mConfirmDialog != null) {
            mConfirmDialog.dismiss();
        }
        if (mDungeonMapDialog != null) {
            mDungeonMapDialog.dismiss();
        }
    }

    public void showLoadingScreen() {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingScreen = new Loading(mGameActivity);
                mLoadingScreen.show();
            }
        });
    }

    public void showGameElementInfo(final GameElement gameElement) {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView nameTV = (TextView) mSelectedElementLayout.findViewById(R.id.name);
                LifeBar lifeBar = (LifeBar) mSelectedElementLayout.findViewById(R.id.life);

                nameTV.setText(gameElement.getName(mResources));

                if (gameElement instanceof Unit) {
                    Unit unit = (Unit) gameElement;
                    nameTV.setCompoundDrawablesWithIntrinsicBounds(unit.getImage(mResources), 0, 0, 0);
                    lifeBar.updateLife(unit.getLifeRatio());
                    lifeBar.setVisibility(View.VISIBLE);
                } else {
                    nameTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    lifeBar.setVisibility(View.GONE);
                }

                mQueueLayout.setVisibility(View.GONE);
                mSelectedElementLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideGameElementInfo(final boolean isSafe) {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSelectedElementLayout.setVisibility(View.GONE);
                if (!isSafe) {
                    mQueueLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void updateActiveHeroLayout() {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LifeBar lifeBar = (LifeBar) mActiveHeroLayout.findViewById(R.id.life);
                lifeBar.updateLife(mHero.getLifeRatio());
            }
        });
    }

    public void updateQueue(final Unit activeCharacter, final Room room) {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (room.isSafe()) {
                    mQueueLayout.setVisibility(View.GONE);
                } else {
                    mQueueLayout.setVisibility(View.VISIBLE);
                    updateQueueCharacter((ViewGroup) mQueueLayout.findViewById(R.id.activeCharacter), activeCharacter);
                    if (room.getQueue().size() > 1) {
                        updateQueueCharacter((ViewGroup) mQueueLayout.findViewById(R.id.nextCharacter), room.getQueue().get(0));
                    } else {
                        mQueueLayout.findViewById(R.id.nextCharacter).setVisibility(View.GONE);
                    }
                    if (room.getQueue().size() > 2) {
                        updateQueueCharacter((ViewGroup) mQueueLayout.findViewById(R.id.nextnextCharacter), room.getQueue().get(1));
                    } else {
                        mQueueLayout.findViewById(R.id.nextnextCharacter).setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void updateQueueCharacter(ViewGroup view, Unit unit) {
        view.setVisibility(View.VISIBLE);
        ((ImageView) view.findViewById(R.id.image)).setImageResource(unit.getImage(mResources));
        ((LifeBar) view.findViewById(R.id.life)).updateLife(unit.getLifeRatio());
    }

    public void showHeroInfo(Hero hero) {
        mHeroInfoDialog = new HeroInfo(mGameActivity, hero);
        showSkills(mHeroInfoDialog, mHero);
        mHeroInfoDialog.show();
    }

    public void showDiscussion(final Pnj pnj, final Discussion discussion, final OnDiscussionReplySelected callback) {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDiscussionDialog == null || !mDiscussionDialog.isShowing()) {
                    if (discussion.getRiddle() != null) {
                        // show riddle
                        mDiscussionDialog = new RiddleBox(mGameActivity, discussion, pnj, callback);
                    } else {
                        // show conversation
                        mDiscussionDialog = new SimpleDiscussionBox(mGameActivity, discussion, pnj, callback);
                    }
                    mDiscussionDialog.show();
                }
            }
        });
    }

    public void showReward(final Reward reward, final OnDismissListener onDismissListener) {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRewardDialog == null || !mRewardDialog.isShowing()) {
                    mRewardDialog = new RewardDialog(mGameActivity, reward);
                    mRewardDialog.setOnDismissListener(onDismissListener);
                    mRewardDialog.show();
                }
            }
        });
    }

    public void showBag(Hero hero) {
        mBagDialog = new Dialog(mGameActivity, R.style.Dialog);
        mBagDialog.setContentView(R.layout.in_game_bag);
        mBagDialog.setCancelable(true);

        updateBag(hero);

        ((TextView) mBagDialog.findViewById(R.id.gold_amount)).setText("" + hero.getGold());

        mBagDialog.show();
    }

    public void updateBag(Hero hero) {
        ViewGroup bagLayout = (ViewGroup) mBagDialog.findViewById(R.id.bag);
        Item item;
        for (int n = 0; n < bagLayout.getChildCount(); n++) {
            item = null;
            if (n < hero.getItems().size()) {
                item = hero.getItems().get(n);
            }
            updateItemLayout(bagLayout.getChildAt(n), item);
        }

        ViewGroup equipmentLayout = (ViewGroup) mBagDialog.findViewById(R.id.equipment);
        Equipment equipment;
        for (int n = 0; n < hero.getEquipments().length; n++) {
            equipment = hero.getEquipments()[n];
            updateEquipmentLayout(equipmentLayout.getChildAt(n), equipment, Equipment.getEquipmentEmptyImage(n));
        }

        if (hero.getEquipments()[0] instanceof TwoHandedWeapon) {
            ImageView image = (ImageView) equipmentLayout.getChildAt(1).findViewById(R.id.image);
            image.setImageResource(hero.getEquipments()[0].getImage(mResources));
            image.setAlpha(0.7f);
        }
    }

    private void updateEquipmentLayout(View itemView, Item item, int defaultImage) {
        View background = itemView.findViewById(R.id.background);
        ImageView image = (ImageView) itemView.findViewById(R.id.image);

        itemView.setTag(R.string.item, item);

        if (item != null) {
            background.setBackgroundColor(mResources.getColor(item.getColor()));
            image.setImageResource(item.getImage(mResources));
            image.setAlpha(1.0f);
            itemView.setEnabled(true);
            itemView.setOnClickListener(mGameActivity);
        } else {
            background.setBackgroundColor(mResources.getColor(android.R.color.transparent));
            image.setImageResource(defaultImage);
            image.setAlpha(0.2f);
            itemView.setEnabled(false);
            itemView.setOnClickListener(null);
        }
    }

    private void updateItemLayout(View itemView, Item item) {
        View background = itemView.findViewById(R.id.background);
        ImageView image = (ImageView) itemView.findViewById(R.id.image);

        itemView.setTag(R.string.item, item);

        if (item != null) {
            background.setBackgroundColor(mResources.getColor(item.getColor()));
            image.setImageResource(item.getImage(mResources));
            itemView.setEnabled(true);
            itemView.setOnClickListener(mGameActivity);
        } else if (itemView.isEnabled()) {
            background.setBackgroundColor(mResources.getColor(android.R.color.transparent));
            image.setImageResource(0);
            itemView.setEnabled(false);
            itemView.setOnClickListener(null);
        }
    }

    public void showItemInfo(Item item, ItemInfoInGame.OnItemActionSelected onItemActionCallback) {
        if (mItemInfoDialog == null || !mItemInfoDialog.isShowing()) {
            mItemInfoDialog = new ItemInfoInGame(mGameActivity, item, mHero, onItemActionCallback);
            mItemInfoDialog.show();
        }
    }

    public void showNewLevelDialog(final OnActionExecuted onActionExecuted) {
        Log.d(TAG, "Show new level dialog for " + mHero.getHeroName());
        mNewLevelDialog = new Dialog(mGameActivity, R.style.Dialog);
        mNewLevelDialog.setContentView(R.layout.in_game_new_level);
        mNewLevelDialog.setCancelable(false);

        showSkills(mNewLevelDialog, mHero);

        ((TextView) mNewLevelDialog.findViewById(R.id.new_level_title)).setText(mGameActivity.getString(R.string.new_level_title, mHero.getLevel()));

        if (onActionExecuted != null) {
            mNewLevelDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    onActionExecuted.onActionDone(true);
                }
            });
        }

        mNewLevelDialog.show();
        mGameActivity.playSound("new_level", false);
    }

    private void showSkills(Dialog dialog, Hero hero) {
        if (dialog == mNewLevelDialog) {
            if (mHero.getSkillPoints() == 0) {
                updateSkillButtons();
                mNewLevelDialog.dismiss();
                return;
            }

            ((TextView) mNewLevelDialog.findViewById(R.id.points_left)).setText(mGameActivity.getString(R.string.new_level_points_left, mHero.getSkillPoints()));
        }

        Log.d(TAG, "Show " + hero.getSkills().size() + " skills");
        ViewGroup skillLayout = (ViewGroup) dialog.findViewById(R.id.skills);
        for (int n = 0; n < skillLayout.getChildCount(); n++) {
            if (n < hero.getSkills().size()) {
                updateSkillLayout(skillLayout.getChildAt(n), hero.getSkills().get(n));
            } else {
                skillLayout.getChildAt(n).setVisibility(View.GONE);
            }
        }
    }

    private void updateSkillLayout(View itemView, Skill skill) {
        View background = itemView.findViewById(R.id.background);
        ImageView image = (ImageView) itemView.findViewById(R.id.image);

        itemView.setTag(R.string.skill, skill);

        background.setBackgroundColor(mResources.getColor(skill.getColor()));
        image.setImageResource(skill.getImage(mResources));
        itemView.setOnClickListener(mGameActivity);
    }

    public void updateSkillButtons() {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutInflater inflater = mGameActivity.getLayoutInflater();
                mSkillButtonsLayout.removeAllViews();
                View skillButton;
                for (final Skill skill : mHero.getSkills()) {
                    if (skill.getLevel() > 0) {
                        skillButton = inflater.inflate(R.layout.in_game_skill_button, null);
                        skillButton.setTag(R.string.show_skill, skill);
                        ((ImageView) skillButton.findViewById(R.id.image)).setImageResource(skill.getImage(mResources));
                        if (skill instanceof PassiveSkill || skill instanceof ActiveSkill && ((ActiveSkill) skill).isUsed()) {
                            skillButton.findViewById(R.id.image).setEnabled(false);
                            skillButton.setAlpha(0.5f);
                        }

                        skillButton.setOnClickListener(mGameActivity);

                        mSkillButtonsLayout.addView(skillButton);
                    }
                }
            }
        });
    }

    public void showUseSkillInfo(Skill skill) {
        if (mItemInfoDialog == null || !mItemInfoDialog.isShowing()) {
            mItemInfoDialog = new UseSkill(mGameActivity, skill, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGameActivity.onClick(v);
                }
            });

            mItemInfoDialog.show();
        }
    }

    public void showImproveSkillDialog(final Skill skill) {
        if (mItemInfoDialog == null || !mItemInfoDialog.isShowing()) {
            mItemInfoDialog = new ImproveSkill(mGameActivity, skill, mHero, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHero.useSkillPoint();
                    skill.improve();
                    showSkills(mNewLevelDialog, mHero);
                }
            });

            mItemInfoDialog.show();
        }
    }

    public void hideBag() {
        if (mBagDialog != null) {
            mBagDialog.dismiss();
        }
    }

    public void showDungeonMap() {
        if (mDungeonMapDialog == null || !mDungeonMapDialog.isShowing()) {
            mDungeonMapDialog = new DungeonMap(mGameActivity, mGameActivity.getGame().getDungeon());
            mDungeonMapDialog.show();
        }
    }

}
