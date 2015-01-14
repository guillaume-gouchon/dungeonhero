package com.glevel.dungeonhero.game.gui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.glevel.dungeonhero.R;

/**
 * Created by guillaume on 1/14/15.
 */
public class Loading extends Dialog {

    public Loading(Context context) {
        super(context, R.style.FullScreenDialog);
        setContentView(R.layout.dialog_game_loading);
        setCancelable(false);

        // animate loading dots
        Animation loadingDotsAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_dots);
        findViewById(R.id.loadingDots).startAnimation(loadingDotsAnimation);
    }

}
