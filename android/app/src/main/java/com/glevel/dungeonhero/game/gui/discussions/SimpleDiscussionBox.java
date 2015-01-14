package com.glevel.dungeonhero.game.gui.discussions;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.base.interfaces.OnDiscussionReplySelected;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.Reaction;

/**
 * Created by guillaume on 1/14/15.
 */
public class SimpleDiscussionBox extends DiscussionBox {

    public SimpleDiscussionBox(Activity activity, final Discussion discussion, final Pnj pnj, final OnDiscussionReplySelected callback) {
        super(activity, pnj);

        // content
        ((TextView) findViewById(R.id.message)).setText(discussion.getMessage(mResources));

        // add reactions
        ViewGroup reactionsLayout = (ViewGroup) findViewById(R.id.reactions);
        reactionsLayout.removeAllViews();

        LayoutInflater inflater = activity.getLayoutInflater();
        TextView reactionTV;
        if (discussion.getReactions() != null) {
            for (Reaction reaction : discussion.getReactions()) {
                reactionTV = (TextView) inflater.inflate(R.layout.in_game_discussion_reply, null);
                reactionTV.setText(reaction.getMessage(mResources));
                reactionTV.setTag(R.string.id, reaction.getSkipNextSteps());
                reactionTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        callback.onReplySelected(pnj, (Integer) view.getTag(R.string.id), discussion.getReward());
                    }
                });
                reactionsLayout.addView(reactionTV);
            }
        } else {
            reactionTV = (TextView) inflater.inflate(R.layout.in_game_discussion_reply, null);
            reactionTV.setText(R.string.ok);
            reactionTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    callback.onReplySelected(pnj, -1, discussion.getReward());
                }
            });
            reactionsLayout.addView(reactionTV);
        }
        reactionsLayout.setVisibility(View.VISIBLE);
    }

}
