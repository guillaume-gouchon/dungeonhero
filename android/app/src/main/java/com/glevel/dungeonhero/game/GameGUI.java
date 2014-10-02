package com.glevel.dungeonhero.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.GameActivity;
import com.glevel.dungeonhero.activities.HomeActivity;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.game.models.units.Soldier;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.game.models.units.categories.Unit.Experience;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.views.CustomAlertDialog;

public class GameGUI {

    private GameActivity mGameActivity;
    private Dialog mLoadingScreen, mGameMenuDialog;
    private TextView mBigLabel;
    private Animation mBigLabelAnimation;
    private Button mFinishDeploymentButton;
    private ViewGroup mSelectedUnitLayout;

    public boolean showConfirm = true;

    public GameGUI(GameActivity activity) {
        this.mGameActivity = activity;
    }

    private OnClickListener onFinishDeploymentClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            MusicManager.playSound(mGameActivity.getApplicationContext(), R.raw.button_sound);
            hideDeploymentButton();
            mGameActivity.startGame();
        }
    };

    public void initGUI() {
        // setup selected unit layout
//        mSelectedUnitLayout = (ViewGroup) mGameActivity.findViewById(R.id.selectedUnit);
        mSelectedUnitLayout.setVisibility(View.GONE);

        // setup finish deployment button
        mFinishDeploymentButton = (Button) mGameActivity.findViewById(R.id.finishDeployment);
        mFinishDeploymentButton.setOnClickListener(onFinishDeploymentClicked);

        // setup big label
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
        mLoadingScreen.dismiss();
        mGameActivity.findViewById(R.id.rootLayout).setVisibility(View.VISIBLE);
    }

    public void openGameMenu() {
        mGameMenuDialog = new Dialog(mGameActivity, R.style.FullScreenDialog);
        mGameMenuDialog.setContentView(R.layout.dialog_game_menu);
        mGameMenuDialog.setCancelable(true);
        Animation menuButtonAnimation = AnimationUtils.loadAnimation(mGameActivity, R.anim.bottom_in);
        // surrender button
        mGameMenuDialog.findViewById(R.id.surrenderButton).setAnimation(menuButtonAnimation);
        mGameMenuDialog.findViewById(R.id.surrenderButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playSound(mGameActivity.getApplicationContext(), R.raw.button_sound);
                Dialog confirmDialog = new CustomAlertDialog(mGameActivity, R.style.Dialog, mGameActivity.getString(R.string.confirm_surrender_message),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MusicManager.playSound(mGameActivity.getApplicationContext(), R.raw.button_sound);
                                if (which == R.id.okButton) {
                                    mGameActivity.endGame(mGameActivity.battle.getEnemyPlayer(mGameActivity.battle.getMe()), true);
                                }
                                dialog.dismiss();
                            }
                        });
                confirmDialog.show();
            }
        });
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

    public void displayVictoryLabel(final boolean isVictory) {
        // show battle report when big label animation is over
        mBigLabelAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mGameActivity.goToReport(isVictory);
            }
        });

        // show victory / defeat big label
        if (isVictory) {
            // victory
            displayBigLabel(mGameActivity.getString(R.string.victory), R.color.green);
        } else {
            // defeat
            displayBigLabel(mGameActivity.getString(R.string.defeat), R.color.red);
        }
    }

    public void showLoadingScreen() {
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

    public void updateSelectedElementLayout(final UnitSprite selectedElement) {
//        mGameActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (selectedElement == null || !(selectedElement.getGameElement() instanceof Unit)) {
//                    mSelectedUnitLayout.setVisibility(View.GONE);
//                    return;
//                }
//
//                Unit unit = (Unit) selectedElement.getGameElement();
//
//                // hide enemies info
//                updateUnitInfoVisibility(unit.getArmy() == mGameActivity.battle.getMe().getArmy());

                // name
//                if (unit instanceof Soldier) {
//                    // display real name
//                    ((TextView) mSelectedUnitLayout.findViewById(R.id.unitName)).setText(((Soldier) unit).getRealName());
//                } else {
//                    ((TextView) mSelectedUnitLayout.findViewById(R.id.unitName)).setText(unit.getName());
//                }
//
//                // health
//                ((TextView) mSelectedUnitLayout.findViewById(R.id.unitName)).setTextColor(mGameActivity.getResources().getColor(unit.getHealth().getColor()));
//
//                // experience
//                if (unit.getExperience() != Experience.RECRUIT) {
//                    ((ImageView) mSelectedUnitLayout.findViewById(R.id.unitExperience)).setImageResource(unit.getExperience().getImage());
//                    ((ImageView) mSelectedUnitLayout.findViewById(R.id.unitExperience)).setVisibility(View.VISIBLE);
//                } else {
//                    ((ImageView) mSelectedUnitLayout.findViewById(R.id.unitExperience)).setVisibility(View.INVISIBLE);
//                }
//
//                // weapons
//                // main weapon
//                Weapon mainWeapon = unit.getWeapons().get(0);
//                ((TextView) mSelectedUnitLayout.findViewById(R.id.unitMainWeaponName)).setText(mainWeapon.getName());
//                ((TextView) mSelectedUnitLayout.findViewById(R.id.unitMainWeaponName)).setCompoundDrawablesWithIntrinsicBounds(mainWeapon.getImage(), 0, 0, 0);
//                ((TextView) mSelectedUnitLayout.findViewById(R.id.unitMainWeaponAP)).setBackgroundResource(mainWeapon.getAPColorEfficiency());
//                ((TextView) mSelectedUnitLayout.findViewById(R.id.unitMainWeaponAT)).setBackgroundResource(mainWeapon.getATColorEfficiency());
//                ((TextView) mSelectedUnitLayout.findViewById(R.id.unitMainWeaponAmmo)).setText("" + mainWeapon.getAmmoAmount());
//
//                // secondary weapon
//                if (unit.getWeapons().size() > 1) {
//                    ((ViewGroup) mSelectedUnitLayout.findViewById(R.id.unitSecondaryWeapon)).setVisibility(View.VISIBLE);
//                    Weapon secondaryWeapon = unit.getWeapons().get(1);
//                    ((TextView) mSelectedUnitLayout.findViewById(R.id.unitSecondaryWeaponName)).setText(secondaryWeapon.getName());
//                    ((TextView) mSelectedUnitLayout.findViewById(R.id.unitSecondaryWeaponName)).setCompoundDrawablesWithIntrinsicBounds(
//                            secondaryWeapon.getImage(), 0, 0, 0);
//                    ((TextView) mSelectedUnitLayout.findViewById(R.id.unitSecondaryWeaponAP)).setBackgroundResource(secondaryWeapon.getAPColorEfficiency());
//                    ((TextView) mSelectedUnitLayout.findViewById(R.id.unitSecondaryWeaponAT)).setBackgroundResource(secondaryWeapon.getATColorEfficiency());
//                    ((TextView) mSelectedUnitLayout.findViewById(R.id.unitSecondaryWeaponAmmo)).setText("" + secondaryWeapon.getAmmoAmount());
//                } else {
//                    ((ViewGroup) mSelectedUnitLayout.findViewById(R.id.unitSecondaryWeapon)).setVisibility(View.GONE);
//                }
//                // frags
//                ((TextView) mSelectedUnitLayout.findViewById(R.id.unitFrags)).setText("" + unit.getFrags());
//
//                // current action
//                ((TextView) mSelectedUnitLayout.findViewById(R.id.unitAction)).setText(unit.getCurrentAction().name());
//                ((TextView) mSelectedUnitLayout.findViewById(R.id.unitAction)).setVisibility(unit.isDead() ? View.GONE : View.VISIBLE);
//
//                mSelectedUnitLayout.setVisibility(View.VISIBLE);
//            }
//        });
    }

    private void updateUnitInfoVisibility(boolean isAlly) {
        int visibility = isAlly ? View.VISIBLE : View.GONE;
//        ((ImageView) mSelectedUnitLayout.findViewById(R.id.unitExperience)).setVisibility(visibility);
//        ((TextView) mSelectedUnitLayout.findViewById(R.id.unitMainWeaponAmmo)).setVisibility(visibility);
//        ((TextView) mSelectedUnitLayout.findViewById(R.id.unitSecondaryWeaponAmmo)).setVisibility(visibility);
//        ((TextView) mSelectedUnitLayout.findViewById(R.id.unitFrags)).setVisibility(visibility);
    }

    public void hideDeploymentButton() {
        mGameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFinishDeploymentButton.setVisibility(View.GONE);
            }
        });
    }

}
