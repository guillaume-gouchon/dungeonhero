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
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.HomeActivity;
import com.glevel.dungeonhero.game.base.CustomGameActivity;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.models.characters.Unit;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.views.LifeBar;

public class GUIManager {

    private CustomGameActivity mGameActivity;

    private Dialog mLoadingScreen, mGameMenuDialog;
    private TextView mBigLabel;
    private Animation mBigLabelAnimation;
    private ViewGroup mSelectedElementLayout, mActiveCharacter;

    public GUIManager(CustomGameActivity activity) {
        this.mGameActivity = activity;
    }

    public void initGUI() {
        mSelectedElementLayout = (ViewGroup) mGameActivity.findViewById(R.id.selectedElement);
        mActiveCharacter = (ViewGroup) mGameActivity.findViewById(R.id.activeCharacter);

        // setup big label TV
        mBigLabelAnimation = AnimationUtils.loadAnimation(mGameActivity, R.anim.big_label_in_game);
        mBigLabel = (TextView) mGameActivity.findViewById(R.id.bigLabel);
    }

    public void displayBigLabel(final String text, final int color) {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBigLabel.setVisibility(View.VISIBLE);
                mBigLabel.setText("" + text);
                mBigLabel.setTextColor(mGameActivity.getResources().getColor(color));
                mBigLabel.startAnimation(mBigLabelAnimation);
            }
        });
    }

    public void hideLoadingScreen() {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingScreen.dismiss();
                mGameActivity.findViewById(R.id.rootLayout).setVisibility(View.VISIBLE);
            }
        });
    }

    public void openGameMenu() {
        mGameMenuDialog = new Dialog(mGameActivity, R.style.FullScreenDialog);
        mGameMenuDialog.setContentView(R.layout.dialog_game_menu);
        mGameMenuDialog.setCancelable(true);
        Animation menuButtonAnimation = AnimationUtils.loadAnimation(mGameActivity, R.anim.bottom_in);

        // resume game button
        mGameMenuDialog.findViewById(R.id.resumeGameButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.resumeGameButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(mGameActivity.getApplicationContext(), R.raw.button_sound);
                mGameMenuDialog.dismiss();
            }
        });

        // exit button
        mGameMenuDialog.findViewById(R.id.exitButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.exitButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(mGameActivity.getApplicationContext(), R.raw.button_sound);
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
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // setup loading screen
                mLoadingScreen = new Dialog(mGameActivity, R.style.FullScreenDialog);
                mLoadingScreen.setContentView(R.layout.dialog_game_loading);
                mLoadingScreen.setCancelable(false);
                mLoadingScreen.setCanceledOnTouchOutside(false);

                // animate loading dots
                Animation loadingDotsAnimation = AnimationUtils.loadAnimation(mGameActivity, R.anim.loading_dots);
                ((TextView) mLoadingScreen.findViewById(R.id.loadingDots)).startAnimation(loadingDotsAnimation);

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

                nameTV.setText(gameElement.getName());

                if (gameElement instanceof Unit) {
                    Unit unit = (Unit) gameElement;
                    nameTV.setCompoundDrawablesWithIntrinsicBounds(unit.getImage(), 0, 0, 0);

                    lifeBar.updateLife(unit.getCurrentHP() / unit.getHp());
                    lifeBar.setVisibility(View.VISIBLE);
                } else {
                    nameTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    lifeBar.setVisibility(View.GONE);
                }

                mSelectedElementLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideGameElementInfo() {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSelectedElementLayout.setVisibility(View.GONE);
            }
        });
    }

    public void updateActiveHeroLayout() {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSelectedElementLayout.setVisibility(View.GONE);
            }
        });
    }

}
