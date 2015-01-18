package com.glevel.dungeonhero.data.dungeons;

import com.glevel.dungeonhero.data.items.ItemFactory;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.dungeons.decorations.Decoration;
import com.glevel.dungeonhero.models.dungeons.decorations.Light;
import com.glevel.dungeonhero.models.dungeons.decorations.Searchable;
import com.glevel.dungeonhero.models.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class DecorationFactory {

    public static List<Decoration> getAll() {
        List<Decoration> lst = new ArrayList<>();
        lst.add(buildLight());
        lst.add(buildTable(null));
        lst.add(buildPot(null));
        lst.add(buildSmallChest(null));
        lst.add(buildStatue(null));
        return lst;
    }

    public static Decoration buildLight() {
        return new Light("light");
    }

    public static Decoration buildPot(Reward reward) {
        return new Searchable("pot", reward, 18, 16, 1, 1);
    }

    public static Decoration buildTable(Reward reward) {
        return new Searchable("table", reward, 85, 70, 1, 1);
    }

    public static Decoration buildSmallChest(Reward reward) {
        return new Searchable("small_chest", reward, 37, 34, 1, 1);
    }

    public static Decoration buildStatue(Reward reward) {
        return new Searchable("statue", reward, 40, 82, 1, 1);
    }

    public static List<Decoration> getRoomContent(int threatLevel) {
        List<Decoration> l = new ArrayList<>();
        int total = Math.random() < 0.2 ? 0 : 1;
        for (int n = 0; n < total; n++) {
            l.add(getRandomSearchable(threatLevel));
        }
        return l;
    }

    private static Decoration getRandomSearchable(int threatLevel) {
        // create random reward
        Reward reward = null;
        int gold = 0;
        Item item = ItemFactory.getRandomItem(threatLevel);
        if (item == null) {
            gold = (int) (threatLevel * 25 * (int) (Math.random() * 4));
        }
        if (item != null || gold > 0) {
            reward = new Reward(item, gold, 0);
        }

        int random = (int) (Math.random() * 4);
        switch (random) {
            case 0:
                return buildSmallChest(reward);
            case 1:
                return buildTable(reward);
            case 2:
                return buildStatue(reward);
            default:
                return buildPot(reward);
        }
    }

}
