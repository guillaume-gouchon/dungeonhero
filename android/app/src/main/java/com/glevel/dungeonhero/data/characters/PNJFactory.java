package com.glevel.dungeonhero.data.characters;

import com.glevel.dungeonhero.data.items.PotionFactory;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.Reaction;
import com.glevel.dungeonhero.models.discussions.riddles.OpenRiddle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class PNJFactory {

    public static List<Pnj> getAll() {
        List<Pnj> lst = new ArrayList<>();
        lst.add(buildPNJ());
        return lst;
    }

    public static Pnj buildPNJ() {
        Pnj pnj = new Pnj("princess", Ranks.NEUTRAL, 5, 5, 6, 10, 2, 7, 1, 500, Hero.HeroTypes.SPI);
        List<Reaction> reactions = new ArrayList<>();
        reactions.add(new Reaction("yes", 0));
        reactions.add(new Reaction("no_i_dont", 1));
        pnj.getDiscussions().add(new Discussion("hello_discussion", reactions, false, null));

        reactions = new ArrayList<>();
        reactions.add(new Reaction("ok", 0));
        pnj.getDiscussions().add(new Discussion("hello_discussion2", reactions, false, null));
        pnj.getDiscussions().add(new Discussion(new OpenRiddle(10, "hello_riddle", "marron", new Reward(PotionFactory.buildLuckPotion()))));
        pnj.getDiscussions().add(new Discussion("hello_riddle_wrong_answer", null, true, null));
        return pnj;
    }

}
