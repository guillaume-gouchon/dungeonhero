package com.glevel.dungeonhero.game.gui.discussions;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.base.interfaces.OnDiscussionReplySelected;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.riddles.OpenRiddle;
import com.glevel.dungeonhero.models.discussions.riddles.Riddle;
import com.glevel.dungeonhero.utils.ApplicationUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guillaume on 1/14/15.
 */
public class RiddleBox extends DiscussionBox {

    public RiddleBox(final Activity activity, Discussion discussion, final Pnj pnj, final OnDiscussionReplySelected callback) {
        super(activity, pnj);

        final Riddle riddle = discussion.getRiddle();

        // content
        ((TextView) findViewById(R.id.message)).setText(riddle.getQuestion(mResources));

        // start timer
        final ProgressBar mTimerView = (ProgressBar) findViewById(R.id.timer);
        mTimerView.setVisibility(View.VISIBLE);
        final Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            private final float timer = riddle.getTimer();
            private float currentTime = timer;

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentTime -= 0.5f;
                        mTimerView.setProgress((int) (100.0f * currentTime / timer));
                        if (currentTime <= 0) {
                            mTimer.cancel();
                            dismiss();
                            callback.onReplySelected(pnj, 0, null);
                        }
                    }
                });
            }
        }, 0, 500);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mTimer.cancel();
            }
        });

        if (riddle instanceof OpenRiddle) {
            final OpenRiddle openRiddle = (OpenRiddle) riddle;
            View answerRiddleLayout = findViewById(R.id.riddle_layout);
            answerRiddleLayout.setVisibility(View.VISIBLE);

            // setup text input
            final EditText answerRiddleInput = (EditText) answerRiddleLayout.findViewById(R.id.riddle_input);
            ApplicationUtils.showKeyboard(activity, answerRiddleInput);
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

            findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = answerRiddleInput.getEditableText().toString();
                    answerRiddle(pnj, openRiddle, answer, callback);
                }
            });
        }
    }

    private void answerRiddle(final Pnj pnj, final OpenRiddle openRiddle, String answer, final OnDiscussionReplySelected callback) {
        if (!answer.isEmpty()) {
            dismiss();
            boolean isAnswerCorrect = openRiddle.isAnswerCorrect(answer);
            callback.onReplySelected(pnj, isAnswerCorrect ? 1 : 0, null);
        }
    }

}
