package com.glevel.dungeonhero.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.HomeActivity;
import com.glevel.dungeonhero.game.base.CustomGameActivity;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.views.LifeBar;

import java.util.List;

public class GUIManager {

    private CustomGameActivity mActivity;

    private Dialog mLoadingScreen, mGameMenuDialog;
    private TextView mBigLabel;
    private Animation mBigLabelAnimation;
    private ViewGroup mSelectedElementLayout, mActiveCharacter, mQueueLayout;

    public GUIManager(CustomGameActivity activity) {
        this.mActivity = activity;
    }

    public void initGUI() {
        mQueueLayout = (ViewGroup) mActivity.findViewById(R.id.queue);
        mSelectedElementLayout = (ViewGroup) mActivity.findViewById(R.id.selectedElement);
        mActiveCharacter = (ViewGroup) mActivity.findViewById(R.id.hero);

        // setup big label TV
        mBigLabelAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.big_label_in_game);
        mBigLabel = (TextView) mActivity.findViewById(R.id.bigLabel);
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

    public void onPause() {
        if (mGameMenuDialog != null) {
            mGameMenuDialog.dismiss();
        }
        if (mLoadingScreen != null) {
            mLoadingScreen.dismiss();
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
                ((TextView) mLoadingScreen.findViewById(R.id.loadingDots)).startAnimation(loadingDotsAnimation);

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

    public void hideGameElementInfo() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSelectedElementLayout.setVisibility(View.GONE);
                mQueueLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void updateActiveHeroLayout() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    public void updateQueue(final Unit activeCharacter, final List<Unit> queue) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    private void updateQueueCharacter(ViewGroup view, Unit unit) {
        view.setVisibility(View.VISIBLE);
        ((ImageView) view.findViewById(R.id.image)).setImageResource(unit.getImage());
        ((LifeBar) view.findViewById(R.id.life)).updateLife(unit.getLifeRatio());
    }

}
