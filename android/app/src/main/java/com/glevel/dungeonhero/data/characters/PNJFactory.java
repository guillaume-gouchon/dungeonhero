package com.glevel.dungeonhero.data.characters;

import com.glevel.dungeonhero.data.items.PotionFactory;
import com.glevel.dungeonhero.game.base.interfaces.OnActionExecuted;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.Reaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class PNJFactory {

    public static List<Pnj> getAll() {
        List<Pnj> lst = new ArrayList<>();
        lst.add(buildTutorialPNJ());
        return lst;
    }

    public static Pnj buildTutorialPNJ() {
        final Pnj pnj = new Pnj("princess", Ranks.NEUTRAL, 5, 5, 13, 10, 8, 0, 0, 1, Hero.HeroTypes.STR);

        // intro
        Discussion discussion = new Discussion("hello_discussion", false, null);
        discussion.addReaction(new Reaction("ok", 0));
        pnj.getDiscussions().add(discussion);

        // explain movement
        discussion = new Discussion("hello_discussion", false, null);
        discussion.addReaction(new Reaction("ok_get_it", 0));
        pnj.getDiscussions().add(discussion);

        // explain talk and search
        discussion = new Discussion("hello_discussion", false, null);
        discussion.addReaction(new Reaction("ok_get_it", 0));
        pnj.getDiscussions().add(discussion);

        // explain fighting
        discussion = new Discussion("hello_discussion", false, null);
        discussion.addReaction(new Reaction("ok_get_it", 0));
        pnj.getDiscussions().add(discussion);

        // explain skills
        discussion = new Discussion("hello_discussion", false, null);
        discussion.addReaction(new Reaction("yes", 0));
        discussion.addReaction(new Reaction("no", 1));
        pnj.getDiscussions().add(discussion);

        // explain tips
        discussion = new Discussion("hello_discussion", false, null);
        discussion.addReaction(new Reaction("ok_get_it", 0));
        pnj.getDiscussions().add(discussion);

        // outro
        discussion = new Discussion("hello_discussion", false, null);
        discussion.addReaction(new Reaction("die_weak", 1));
        discussion.addReaction(new Reaction("pray", 0));
        pnj.getDiscussions().add(discussion);

        // give reward
        discussion = new Discussion("hello_discussion", false, new Reward(PotionFactory.buildForcePotion()));
        discussion.addReaction(new Reaction("thanks", 0));
        pnj.getDiscussions().add(discussion);

        pnj.setOnDiscussionOver(new OnActionExecuted() {
            @Override
            public void onActionDone(boolean success) {
                pnj.setHp(0);
            }
        });

        return pnj;
    }

}
