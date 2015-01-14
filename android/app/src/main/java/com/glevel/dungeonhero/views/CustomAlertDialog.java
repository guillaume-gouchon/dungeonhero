package com.glevel.dungeonhero.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glevel.dungeonhero.R;

public class CustomAlertDialog extends Dialog implements OnClickListener {

    private OnClickListener mOnClickListener;

    public CustomAlertDialog(Context context, int style, String message, android.content.DialogInterface.OnClickListener onClickListener) {
        super(context, style);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.in_game_discussion);
        findViewById(R.id.rootLayout).getBackground().setAlpha(70);

        TextView pnjName = (TextView) findViewById(R.id.name);
        pnjName.setText(R.string.game_master);
        pnjName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.white_wizard, 0, 0, 0);

        ((TextView) findViewById(R.id.message)).setText(message);

        ViewGroup reactionsLayout = (ViewGroup) findViewById(R.id.reactions);
        LayoutInflater inflater = getLayoutInflater();
        TextView reactionTV;

        reactionTV = (TextView) inflater.inflate(R.layout.in_game_discussion_reply, null);
        reactionTV.setId(R.id.ok_btn);
        reactionTV.setText(R.string.yes);
        reactionTV.setOnClickListener(this);
        reactionsLayout.addView(reactionTV);

        reactionTV = (TextView) inflater.inflate(R.layout.in_game_discussion_reply, null);
        reactionTV.setId(R.id.cancel_btn);
        reactionTV.setText(R.string.no);
        reactionTV.setOnClickListener(this);
        reactionsLayout.addView(reactionTV);
        mOnClickListener = onClickListener;
        reactionsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        mOnClickListener.onClick(this, v.getId());
    }

}
