package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
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
        List<Pnj> lst = new ArrayList<Pnj>();
        lst.add(buildPNJ());
        return lst;
    }

    public static Pnj buildPNJ() {
        Pnj pnj = new Pnj(false, Ranks.NEUTRAL, R.drawable.princess, "princess.png", 5, 5, 6, 10, 2, 8, 8, R.string.app_name, R.string.choose_hero_message, 10, 1, 500);
        List<Reaction> reactions = new ArrayList<Reaction>();
        reactions.add(new Reaction(R.string.yes, 0));
        reactions.add(new Reaction(R.string.no, 1));
        pnj.getDiscussions().add(new Discussion(pnj.getImage(), pnj.getName(), R.string.hello_discussion, reactions, false, null));
        pnj.getDiscussions().add(new Discussion(pnj.getImage(), pnj.getName(), R.string.hello_discussion2, null, true, null));
        return pnj;
    }

}
