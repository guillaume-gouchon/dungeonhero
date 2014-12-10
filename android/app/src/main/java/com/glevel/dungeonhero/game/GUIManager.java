package com.glevel.dungeonhero.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.BookChooserActivity;
import com.glevel.dungeonhero.activities.GameActivity;
import com.glevel.dungeonhero.activities.HomeActivity;
import com.glevel.dungeonhero.activities.fragments.StoryFragment;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.base.MyBaseGameActivity;
import com.glevel.dungeonhero.game.base.interfaces.OnActionExecuted;
import com.glevel.dungeonhero.game.base.interfaces.OnDiscussionReplySelected;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.Reaction;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.items.Consumable;
import com.glevel.dungeonhero.models.items.Equipment;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.Requirement;
import com.glevel.dungeonhero.models.items.equipments.Armor;
import com.glevel.dungeonhero.models.items.equipments.Weapon;
import com.glevel.dungeonhero.models.riddles.MultiChoicesRiddle;
import com.glevel.dungeonhero.models.riddles.OpenRiddle;
import com.glevel.dungeonhero.models.riddles.Riddle;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.views.CustomAlertDialog;
import com.glevel.dungeonhero.views.HintTextView;
import com.glevel.dungeonhero.views.LifeBar;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GUIManager {

    private static final String TAG = "GUIManager";

    private MyBaseGameActivity mActivity;

    private Dialog mLoadingScreen, mGameMenuDialog, mHeroInfoDialog, mDiscussionDialog, mRewardDialog,
            mBagDialog, mItemInfoDialog, mNewLevelDialog;
    private TextView mBigLabel;
    private Animation mBigLabelAnimation;
    private ViewGroup mSelectedElementLayout, mActiveHeroLayout, mQueueLayout;
    private ViewGroup mSkillButtonsLayout;

    public GUIManager(MyBaseGameActivity activity) {
        mActivity = activity;
    }

    public void initGUI() {
        mQueueLayout = (ViewGroup) mActivity.findViewById(R.id.queue);
        mSelectedElementLayout = (ViewGroup) mActivity.findViewById(R.id.selectedElement);

        mActiveHeroLayout = (ViewGroup) mActivity.findViewById(R.id.hero);
        mActiveHeroLayout.setOnClickListener(mActivity);

        mActivity.findViewById(R.id.bag).setOnClickListener(mActivity);

        // setup big label TV
        mBigLabelAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.big_label_in_game);
        mBigLabel = (TextView) mActivity.findViewById(R.id.bigLabel);

        mSkillButtonsLayout = (ViewGroup) mActivity.findViewById(R.id.skillButtonsLayout);
    }

    public void displayBigLabel(final String text, final int color) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBigLabel.setVisibility(View.VISIBLE);
                mBigLabel.setText("" + text);
                mBigLabel.setTextColor(mActivity.getResources().getColor(color));
                mBigLabel.startAnimation(mBigLabelAnimation);
            }
        });
    }

    public void hideLoadingScreen() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingScreen.dismiss();
                mActivity.findViewById(R.id.rootLayout).setVisibility(View.VISIBLE);
            }
        });
    }

    public void openGameMenu() {
        mGameMenuDialog = new Dialog(mActivity, R.style.FullScreenDialog);
        mGameMenuDialog.setContentView(R.layout.dialog_game_menu);
        mGameMenuDialog.setCancelable(true);
        Animation menuButtonAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.bottom_in);

        // resume game button
        mGameMenuDialog.findViewById(R.id.resumeGameButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.resumeGameButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(mActivity.getApplicationContext(), R.raw.button_sound);
                mGameMenuDialog.dismiss();
            }
        });

        // leave quest button
        mGameMenuDialog.findViewById(R.id.leaveQuestButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.leaveQuestButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(mActivity.getApplicationContext(), R.raw.button_sound);
                showLeaveQuestDialog();
            }
        });

        // exit button
        mGameMenuDialog.findViewById(R.id.exitButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.exitButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(mActivity.getApplicationContext(), R.raw.button_sound);
                mActivity.startActivity(new Intent(mActivity, HomeActivity.class));
                mActivity.finish();
            }
        });

        mGameMenuDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mActivity.resumeGame();
            }
        });

        mGameMenuDialog.show();
        menuButtonAnimation.start();
    }

    public void showLeaveQuestDialog() {
        Dialog confirmDialog = new CustomAlertDialog(mActivity, R.style.Dialog, mActivity.getString(R.string.confirm_leave_quest),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MusicManager.playSound(mActivity.getApplicationContext(), R.raw.button_sound);
                        if (which == R.id.okButton) {
                            Intent intent = new Intent(mActivity, BookChooserActivity.class);
                            intent.putExtra(Game.class.getName(), mActivity.getGame());
                            mActivity.startActivity(intent);
                            mActivity.finish();
                        }
                        dialog.dismiss();
                    }
                });
        confirmDialog.show();
    }

    public void showFinishQuestDialog() {
        Dialog confirmDialog = new CustomAlertDialog(mActivity, R.style.Dialog, mActivity.getString(R.string.confirm_finish_quest),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == R.id.okButton) {
                            showVictoryDialog();
                        }
                        dialog.dismiss();
                    }
                });
        confirmDialog.show();
    }

    public void showVictoryDialog() {
        final Book activeBook = mActivity.getGame().getBook();
        boolean hasNextChapter = activeBook.getChapters().size() > 1;
        Dialog confirmDialog = new CustomAlertDialog(mActivity, R.style.Dialog, mActivity.getString(activeBook.getActiveChapter().getOutroText()),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == R.id.okButton) {
                            boolean hasNextChapter = activeBook.goToNextChapter();
                            if (hasNextChapter) {
                                Intent intent = new Intent(mActivity, GameActivity.class);
                                intent.putExtra(Game.class.getName(), mActivity.getGame());
                                mActivity.startActivity(intent);
                                mActivity.finish();
                            } else {
                                mActivity.getGame().finishBook(activeBook);
                                if (activeBook.getOutroText() > 0) {
                                    // show outro text
                                    Bundle args = new Bundle();
                                    args.putInt(StoryFragment.ARGUMENT_STORY, activeBook.getOutroText());
                                    args.putBoolean(StoryFragment.ARGUMENT_IS_OUTRO, true);
                                    DialogFragment storyFragment = new StoryFragment();
                                    ApplicationUtils.openDialogFragment(mActivity, storyFragment, args);
                                } else {
                                    // go directly to the book chooser, to start a new adventure
                                    Intent intent = new Intent(mActivity, BookChooserActivity.class);
                                    intent.putExtra(Game.class.getName(), mActivity.getGame());
                                    mActivity.startActivity(intent);
                                    mActivity.finish();
                                }
                            }
                        }
                        dialog.dismiss();
                    }
                });
        ((TextView) confirmDialog.findViewById(R.id.name)).setCompoundDrawablesWithIntrinsicBounds(mActivity.getGame().getHero().getImage(), 0, 0, 0);
        ((TextView) confirmDialog.findViewById(R.id.name)).setText(mActivity.getGame().getHero().getHeroName());
        ((TextView) confirmDialog.findViewById(R.id.okButton)).setText(hasNextChapter ? R.string.go_to_next_chapter : R.string.finish_adventure);
        confirmDialog.findViewById(R.id.cancelButton).setVisibility(View.GONE);
        confirmDialog.show();
    }

    public void showChapterIntro(final OnDiscussionReplySelected callback) {
        Book activeBook = mActivity.getGame().getBook();
        Dialog confirmDialog = new CustomAlertDialog(mActivity, R.style.Dialog, mActivity.getString(activeBook.getActiveChapter().getIntroText()),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (callback != null) {
                            callback.onReplySelected(null, 0);
                        }
                    }
                });
        ((TextView) confirmDialog.findViewById(R.id.name)).setCompoundDrawablesWithIntrinsicBounds(mActivity.getGame().getHero().getImage(), 0, 0, 0);
        ((TextView) confirmDialog.findViewById(R.id.name)).setText(mActivity.getGame().getHero().getHeroName());
        ((TextView) confirmDialog.findViewById(R.id.okButton)).setText(R.string.ok);
        confirmDialog.findViewById(R.id.cancelButton).setVisibility(View.GONE);
        confirmDialog.show();
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
    }

    public void showLoadingScreen() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // setup loading screen
                mLoadingScreen = new Dialog(mActivity, R.style.FullScreenDialog);
                mLoadingScreen.setContentView(R.layout.dialog_game_loading);
                mLoadingScreen.setCancelable(false);
                mLoadingScreen.setCanceledOnTouchOutside(false);

                // animate loading dots
                Animation loadingDotsAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.loading_dots);
                mLoadingScreen.findViewById(R.id.loadingDots).startAnimation(loadingDotsAnimation);

                mLoadingScreen.show();
            }
        });
    }

    public void showGameElementInfo(final GameElement gameElement) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView nameTV = (TextView) mSelectedElementLayout.findViewById(R.id.name);
                LifeBar lifeBar = (LifeBar) mSelectedElementLayout.findViewById(R.id.life);

                nameTV.setText(gameElement.getName());

                if (gameElement instanceof Unit) {
                    Unit unit = (Unit) gameElement;
                    nameTV.setCompoundDrawablesWithIntrinsicBounds(unit.getImage(), 0, 0, 0);

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
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSelectedElementLayout.setVisibility(View.GONE);
                if (!isSafe) {
                    mQueueLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void updateActiveHeroLayout(final Hero hero) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LifeBar lifeBar = (LifeBar) mActiveHeroLayout.findViewById(R.id.life);
                lifeBar.updateLife(hero.getLifeRatio());
            }
        });
    }

    public void updateQueue(final Unit activeCharacter, final List<Unit> queue, final boolean isSafe) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSafe) {
                    mQueueLayout.setVisibility(View.GONE);
                } else {
                    mQueueLayout.setVisibility(View.VISIBLE);
                    updateQueueCharacter((ViewGroup) mQueueLayout.findViewById(R.id.activeCharacter), activeCharacter);
                    if (queue.size() > 1) {
                        updateQueueCharacter((ViewGroup) mQueueLayout.findViewById(R.id.nextCharacter), queue.get(0));
                    } else {
                        mQueueLayout.findViewById(R.id.nextCharacter).setVisibility(View.GONE);
                    }
                    if (queue.size() > 2) {
                        updateQueueCharacter((ViewGroup) mQueueLayout.findViewById(R.id.nextnextCharacter), queue.get(1));
                    } else {
                        mQueueLayout.findViewById(R.id.nextnextCharacter).setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void updateQueueCharacter(ViewGroup view, Unit unit) {
        view.setVisibility(View.VISIBLE);
        ((ImageView) view.findViewById(R.id.image)).setImageResource(unit.getImage());
        ((LifeBar) view.findViewById(R.id.life)).updateLife(unit.getLifeRatio());
    }

    public void showHeroInfo(Hero hero) {
        mHeroInfoDialog = new Dialog(mActivity, R.style.Dialog);
        mHeroInfoDialog.setContentView(R.layout.in_game_hero_details);
        mHeroInfoDialog.setCancelable(true);

        ((TextView) mHeroInfoDialog.findViewById(R.id.name)).setText(hero.getHeroName());
        ((TextView) mHeroInfoDialog.findViewById(R.id.description)).setText(hero.getDescription());
        ((TextView) mHeroInfoDialog.findViewById(R.id.hp)).setText(mActivity.getString(R.string.hp_in_game, hero.getCurrentHP(), hero.getHp()));
        ((TextView) mHeroInfoDialog.findViewById(R.id.level)).setText(mActivity.getString(R.string.level_in_game, hero.getLevel(), hero.getXp(), hero.getNextLevelXPAmount()));
        ((TextView) mHeroInfoDialog.findViewById(R.id.strength)).setText("" + hero.getStrength());
        ((TextView) mHeroInfoDialog.findViewById(R.id.dexterity)).setText("" + hero.getDexterity());
        ((TextView) mHeroInfoDialog.findViewById(R.id.spirit)).setText("" + hero.getSpirit());
        ((TextView) mHeroInfoDialog.findViewById(R.id.movement)).setText(mActivity.getString(R.string.movement) + " : " + hero.calculateMovement());
        ((TextView) mHeroInfoDialog.findViewById(R.id.damage)).setText(mActivity.getString(R.string.damage) + " : " + hero.getReadableDamage());
        ((TextView) mHeroInfoDialog.findViewById(R.id.protection)).setText(mActivity.getString(R.string.protection) + " : " + hero.calculateProtection());
        ((TextView) mHeroInfoDialog.findViewById(R.id.dodge)).setText(mActivity.getString(R.string.dodge) + " : " + hero.calculateDodge() + "%");
        ((TextView) mHeroInfoDialog.findViewById(R.id.critical)).setText(mActivity.getString(R.string.critical) + " : " + hero.calculateCritical() + "%");
        ((TextView) mHeroInfoDialog.findViewById(R.id.frags)).setText(hero.getFrags() + " " + mActivity.getString(R.string.frags));

        showSkills(mHeroInfoDialog, mActivity.getGame().getHero());

        mHeroInfoDialog.show();
    }

    public void showDiscussion(final Pnj pnj, final Discussion discussion, final OnDiscussionReplySelected callback) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDiscussionDialog == null || !mDiscussionDialog.isShowing()) {
                    mDiscussionDialog = new Dialog(mActivity, R.style.Dialog);
                    mDiscussionDialog.setContentView(R.layout.in_game_discussion);
                    mDiscussionDialog.setCancelable(false);
                    mDiscussionDialog.findViewById(R.id.rootLayout).getBackground().setAlpha(70);

                    TextView pnjName = (TextView) mDiscussionDialog.findViewById(R.id.name);
                    pnjName.setText(discussion.getName());
                    pnjName.setCompoundDrawablesWithIntrinsicBounds(discussion.getImage(), 0, 0, 0);

                    if (discussion.getRiddle() != null) {
                        showRiddle(pnj, discussion, callback);
                    } else {
                        showNormalDiscussion(pnj, discussion, callback);
                    }

                    mDiscussionDialog.show();
                }
            }
        });
    }

    private void showNormalDiscussion(final Pnj pnj, Discussion discussion, final OnDiscussionReplySelected callback) {
        ((TextView) mDiscussionDialog.findViewById(R.id.message)).setText(discussion.getMessage());

        ViewGroup reactionsLayout = (ViewGroup) mDiscussionDialog.findViewById(R.id.reactions);
        reactionsLayout.setVisibility(View.VISIBLE);
        reactionsLayout.removeAllViews();

        LayoutInflater inflater = mActivity.getLayoutInflater();
        TextView reactionTV;
        if (discussion.getReactions() != null) {
            for (Reaction reaction : discussion.getReactions()) {
                reactionTV = (TextView) inflater.inflate(R.layout.in_game_discussion_reply, null);
                reactionTV.setText(reaction.getMessage());
                reactionTV.setTag(R.string.id, reaction.getSkipNextSteps());
                reactionTV.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDiscussionDialog.dismiss();
                        callback.onReplySelected(pnj, (Integer) view.getTag(R.string.id));
                    }
                });
                reactionsLayout.addView(reactionTV);
            }
        } else {
            reactionTV = (TextView) inflater.inflate(R.layout.in_game_discussion_reply, null);
            reactionTV.setText(R.string.ok);
            reactionTV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDiscussionDialog.dismiss();
                    callback.onReplySelected(pnj, -1);
                }
            });
            reactionsLayout.addView(reactionTV);
        }
    }

    private void showRiddle(final Pnj pnj, Discussion discussion, final OnDiscussionReplySelected callback) {
        final Riddle riddle = discussion.getRiddle();

        // start timer
        final ProgressBar mTimerView = (ProgressBar) mDiscussionDialog.findViewById(R.id.timer);
        mTimerView.setVisibility(View.VISIBLE);
        final Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            final float timer = riddle.getTimer() * (1 + 1.0f * mActivity.getGame().getHero().getSpirit() / 100);
            float currentTime = timer;

            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentTime -= 0.5f;
                        mTimerView.setProgress((int) (100.0f * currentTime / timer));
                        if (currentTime <= 0) {
                            mTimer.cancel();
                            mDiscussionDialog.dismiss();
                            callback.onReplySelected(pnj, 0);
                        }
                    }
                });
            }
        }, 0, 500);

        mDiscussionDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mTimer.cancel();
            }
        });

        ((TextView) mDiscussionDialog.findViewById(R.id.message)).setText(riddle.getQuestion());

        if (riddle instanceof MultiChoicesRiddle) {
            final MultiChoicesRiddle multiChoicesRiddle = (MultiChoicesRiddle) riddle;
            ViewGroup reactionsLayout = (ViewGroup) mDiscussionDialog.findViewById(R.id.reactions);
            reactionsLayout.setVisibility(View.VISIBLE);
            reactionsLayout.removeAllViews();

            LayoutInflater inflater = mActivity.getLayoutInflater();
            TextView reactionTV;
            for (int answer : multiChoicesRiddle.getAnswers()) {
                reactionTV = (TextView) inflater.inflate(R.layout.in_game_discussion_reply, null);
                reactionTV.setText(answer);
                reactionTV.setTag(answer);
                reactionTV.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        mDiscussionDialog.dismiss();
                        if (multiChoicesRiddle.isAnswerCorrect((Integer) view.getTag())) {
                            showReward(riddle.getReward(), new OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    callback.onReplySelected(pnj, 1);
                                }
                            });
                        } else {
                            callback.onReplySelected(pnj, 0);
                        }
                    }
                });
                reactionsLayout.addView(reactionTV);
            }
        } else {
            final OpenRiddle openRiddle = (OpenRiddle) riddle;
            View answerRiddleLayout = mDiscussionDialog.findViewById(R.id.riddle_layout);
            answerRiddleLayout.setVisibility(View.VISIBLE);

            final EditText answerRiddleInput = (EditText) answerRiddleLayout.findViewById(R.id.riddle_input);
            ApplicationUtils.showKeyboard(mActivity, answerRiddleInput);
            answerRiddleInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            answerRiddleInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String answer = v.getEditableText().toString();
                    if (!answer.isEmpty() && actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || event != null
                            && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        answerRiddle(pnj, openRiddle, answer, callback);
                        return true;
                    }
                    return false;
                }
            });

            mDiscussionDialog.findViewById(R.id.okButton).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = answerRiddleInput.getEditableText().toString();
                    answerRiddle(pnj, openRiddle, answer, callback);
                }
            });
        }
    }

    private void answerRiddle(final Pnj pnj, OpenRiddle openRiddle, String answer, final OnDiscussionReplySelected callback) {
        if (!answer.isEmpty()) {
            mDiscussionDialog.dismiss();
            if (openRiddle.isAnswerCorrect(answer)) {
                showReward(openRiddle.getReward(), new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        callback.onReplySelected(pnj, 1);
                    }
                });
            } else {
                callback.onReplySelected(pnj, 0);
            }
        }
    }

    public void showReward(final Reward reward, final OnDismissListener onDismissListener) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRewardDialog == null || !mRewardDialog.isShowing()) {
                    mRewardDialog = new Dialog(mActivity, R.style.Dialog);
                    mRewardDialog.setContentView(R.layout.in_game_reward);
                    mRewardDialog.setCancelable(false);
                    mRewardDialog.setOnDismissListener(onDismissListener);

                    TextView itemTV = (TextView) mRewardDialog.findViewById(R.id.item);
                    TextView goldTV = (TextView) mRewardDialog.findViewById(R.id.gold);
                    TextView xpTV = (TextView) mRewardDialog.findViewById(R.id.xp);

                    if (reward == null) {
                        itemTV.setText(R.string.found_nothing);
                        itemTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.found_nothing);
                        goldTV.setVisibility(View.GONE);
                        xpTV.setVisibility(View.GONE);
                    } else {
                        if (reward.getItem() != null) {
                            itemTV.setText(mActivity.getString(R.string.found_item, mActivity.getString(reward.getItem().getName())));
                            itemTV.setCompoundDrawablesWithIntrinsicBounds(0, reward.getItem().getImage(), 0, 0);
                            itemTV.setVisibility(View.VISIBLE);
                        } else {
                            itemTV.setVisibility(View.GONE);
                        }

                        if (reward.getGold() > 0) {
                            goldTV.setText(mActivity.getString(R.string.found_gold, reward.getGold()));
                            goldTV.setVisibility(View.VISIBLE);
                        } else {
                            goldTV.setVisibility(View.GONE);
                        }

                        if (reward.getXp() > 0) {
                            xpTV.setText(mActivity.getString(R.string.reward_xp, reward.getXp()));
                            xpTV.setVisibility(View.VISIBLE);
                        } else {
                            xpTV.setVisibility(View.GONE);
                        }
                    }

                    mRewardDialog.findViewById(R.id.dismiss_button).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRewardDialog.dismiss();
                        }
                    });

                    mRewardDialog.show();
                }
            }
        });
    }

    public void showBag(Hero hero) {
        mBagDialog = new Dialog(mActivity, R.style.Dialog);
        mBagDialog.setContentView(R.layout.in_game_bag);
        mBagDialog.setCancelable(true);

        updateBag(hero);

        ((TextView) mBagDialog.findViewById(R.id.gold_amount)).setText("" + hero.getGold());

        mBagDialog.show();
    }

    private void updateBag(Hero hero) {
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
    }

    private void updateEquipmentLayout(View itemView, Item item, int defaultImage) {
        View background = itemView.findViewById(R.id.background);
        ImageView image = (ImageView) itemView.findViewById(R.id.image);

        itemView.setTag(R.string.item, item);

        if (item != null) {
            background.setBackgroundColor(mActivity.getResources().getColor(item.getColor()));
            image.setImageResource(item.getImage());
            image.setAlpha(1.0f);
            itemView.setEnabled(true);
            itemView.setOnClickListener(mActivity);
        } else if (itemView.isEnabled()) {
            background.setBackgroundColor(mActivity.getResources().getColor(android.R.color.transparent));
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
            background.setBackgroundColor(mActivity.getResources().getColor(item.getColor()));
            image.setImageResource(item.getImage());
            itemView.setEnabled(true);
            itemView.setOnClickListener(mActivity);
        } else if (itemView.isEnabled()) {
            background.setBackgroundColor(mActivity.getResources().getColor(android.R.color.transparent));
            image.setImageResource(0);
            itemView.setEnabled(false);
            itemView.setOnClickListener(null);
        }
    }

    public void showItemInfo(final Hero hero, final Item item, final OnActionExecuted onDropListener) {
        if (mItemInfoDialog == null || !mItemInfoDialog.isShowing()) {
            mItemInfoDialog = new Dialog(mActivity, R.style.DialogNoAnimation);
            mItemInfoDialog.setContentView(R.layout.in_game_item_info);
            mItemInfoDialog.setCancelable(true);

            TextView nameTV = (TextView) mItemInfoDialog.findViewById(R.id.name);
            nameTV.setText(item.getName());
            nameTV.setCompoundDrawablesWithIntrinsicBounds(item.getImage(), 0, 0, 0);

            TextView descriptionTV = (TextView) mItemInfoDialog.findViewById(R.id.description);
            if (item.getDescription() > 0) {
                descriptionTV.setText(item.getDescription());
            } else {
                descriptionTV.setVisibility(View.GONE);
            }

            TextView actionButton = (TextView) mItemInfoDialog.findViewById(R.id.actionButton);
            TextView dropButton = (TextView) mItemInfoDialog.findViewById(R.id.dropButton);
            ViewGroup statsLayout = (ViewGroup) mItemInfoDialog.findViewById(R.id.stats);
            ViewGroup requirementsLayout = (ViewGroup) mItemInfoDialog.findViewById(R.id.requirements);
            int indexStats = 0, indexRequirements = 0;

            if (item instanceof Equipment) {
                final Equipment equipment = (Equipment) item;
                if (hero.isEquipped(equipment)) {
                    actionButton.setText(R.string.remove);
                    actionButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hero.removeEquipment(equipment);
                            updateBag(hero);
                            mItemInfoDialog.dismiss();
                        }
                    });
                    dropButton.setVisibility(View.GONE);
                } else if (hero.canEquipItem(equipment)) {
                    actionButton.setText(R.string.equip);
                    actionButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hero.equip(equipment);
                            updateBag(hero);
                            mItemInfoDialog.dismiss();
                        }
                    });
                } else {
                    actionButton.setVisibility(View.GONE);
                }

                // add unique stats
                if (item instanceof Weapon) {
                    Weapon weapon = (Weapon) item;
                    addStatToItemLayout(statsLayout.getChildAt(indexStats++), weapon.getMinDamage() + " - " + (weapon.getMinDamage() + weapon.getDeltaDamage()), R.drawable.ic_damage, R.string.damage, R.color.white);
                } else if (item instanceof Armor) {
                    Armor armor = (Armor) item;
                    addStatToItemLayout(statsLayout.getChildAt(indexStats++), "+" + armor.getProtection(), R.drawable.ic_armor, R.string.protection, R.color.white);
                }

                // add buffs
                for (Effect buff : equipment.getEffects()) {
                    addStatToItemLayout(statsLayout.getChildAt(indexStats++), (buff.getValue() > 0 ? "+" : "") + buff.getValue(), buff.getTarget().getImage(), buff.getTarget().getName(), buff.getValue() > 0 ? R.color.attack : R.color.red);
                }

                // add requirements
                for (Requirement requirement : equipment.getRequirements()) {
                    addStatToItemLayout(requirementsLayout.getChildAt(indexRequirements++), mActivity.getString(R.string.minimum, requirement.getValue()), requirement.getTarget().getImage(), requirement.getTarget().getName(), R.color.white);
                }
            } else if (item instanceof Consumable) {
                final Consumable consumable = (Consumable) item;
                actionButton.setText(R.string.use);
                actionButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hero.use(consumable);
                        updateBag(hero);
                        mItemInfoDialog.dismiss();
                    }
                });
            }

            if (item.isDroppable()) {
                dropButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hero.drop(item);
                        updateBag(hero);
                        onDropListener.onActionDone(true);
                        mItemInfoDialog.dismiss();
                    }
                });
            } else {
                dropButton.setVisibility(View.GONE);
            }

            mItemInfoDialog.show();
        }
    }

    private void addStatToItemLayout(View view, String text, int image, int hint, int color) {
        HintTextView statView = (HintTextView) view;
        statView.setText(text);
        statView.setTextHint(hint);
        statView.setCompoundDrawablesWithIntrinsicBounds(image, 0, 0, 0);
        int colorResource = mActivity.getResources().getColor(color);
        statView.setTextColor(colorResource);
        statView.setVisibility(View.VISIBLE);
    }

    public void showNewLevelDialog() {
        mNewLevelDialog = new Dialog(mActivity, R.style.Dialog);
        mNewLevelDialog.setContentView(R.layout.in_game_new_level);
        mNewLevelDialog.setCancelable(false);

        showSkills(mNewLevelDialog, mActivity.getGame().getHero());

        ((TextView) mNewLevelDialog.findViewById(R.id.new_level_title)).setText(mActivity.getString(R.string.new_level_title, mActivity.getGame().getHero().getLevel()));

        mNewLevelDialog.show();
    }

    private void showSkills(Dialog dialog, Hero hero) {
        if (dialog == mNewLevelDialog) {
            if (mActivity.getGame().getHero().getSkillPoints() == 0) {
                updateSkillButtons();
                mNewLevelDialog.dismiss();
                return;
            }

            ((TextView) mNewLevelDialog.findViewById(R.id.points_left)).setText(mActivity.getString(R.string.new_level_points_left, mActivity.getGame().getHero().getSkillPoints()));
        }

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

        background.setBackgroundColor(mActivity.getResources().getColor(skill.getColor()));
        image.setImageResource(skill.getImage());
        itemView.setOnClickListener(mActivity);
    }

    public void showSkillInfo(final Skill skill) {
        if (mItemInfoDialog == null || !mItemInfoDialog.isShowing()) {
            mItemInfoDialog = new Dialog(mActivity, R.style.DialogNoAnimation);
            mItemInfoDialog.setContentView(R.layout.in_game_item_info);
            mItemInfoDialog.setCancelable(true);

            TextView nameTV = (TextView) mItemInfoDialog.findViewById(R.id.name);
            nameTV.setText(mActivity.getString(skill.getName()) + " (lvl " + skill.getLevel() + ")");
            nameTV.setCompoundDrawablesWithIntrinsicBounds(skill.getImage(), 0, 0, 0);

            TextView descriptionTV = (TextView) mItemInfoDialog.findViewById(R.id.description);
            if (skill.getDescription() > 0) {
                descriptionTV.setText(skill.getDescription());
            } else {
                descriptionTV.setVisibility(View.GONE);
            }

            TextView actionButton = (TextView) mItemInfoDialog.findViewById(R.id.actionButton);
            actionButton.setText(R.string.improve_skill);
            actionButton.setVisibility(skill.canBeImproved() && mActivity.getGame().getHero().getSkillPoints() > 0 ? View.VISIBLE : View.GONE);
            actionButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.getGame().getHero().useSkillPoint();
                    skill.improve();
                    showSkills(mNewLevelDialog, mActivity.getGame().getHero());
                    mItemInfoDialog.dismiss();
                }
            });

            mItemInfoDialog.findViewById(R.id.dropButton).setVisibility(View.GONE);
            mItemInfoDialog.show();
        }
    }

    public void updateSkillButtons() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutInflater inflater = mActivity.getLayoutInflater();
                mSkillButtonsLayout.removeAllViews();
                View skillButton;
                for (Skill skill : mActivity.getGame().getHero().getSkills()) {
                    if (skill.getLevel() > 0) {
                        skillButton = inflater.inflate(R.layout.in_game_skill_button, null);
                        skillButton.setTag(R.string.use_skill, skill);
                        ((ImageView) skillButton.findViewById(R.id.image)).setImageResource(skill.getImage());
                        if (skill instanceof PassiveSkill || skill instanceof ActiveSkill && ((ActiveSkill) skill).isUsed()) {
                            skillButton.findViewById(R.id.image).setEnabled(false);
                            skillButton.setAlpha(0.5f);
                        } else {
                            skillButton.setOnClickListener(mActivity);
                        }
                        mSkillButtonsLayout.addView(skillButton);
                    }
                }
            }
        });
    }

}
