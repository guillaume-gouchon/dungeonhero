package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.dungeons.decorations.Decoration;
import com.glevel.dungeonhero.models.dungeons.decorations.Light;
import com.glevel.dungeonhero.models.dungeons.decorations.Searchable;
import com.glevel.dungeonhero.models.dungeons.decorations.TreasureChest;
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
        lst.add(buildBox(null));
        lst.add(buildBarrel(null));
        lst.add(buildSmallChest(null));
        lst.add(buildTreasureChest(null));
        return lst;
    }

    public static Decoration buildLight() {
        return new Light("light");
    }

    public static Decoration buildTreasureChest(Reward reward) {
        return new TreasureChest(reward);
    }

    public static Decoration buildBarrel(Reward reward) {
        return new Searchable("barrel", reward, 13, 16, 1, 1);
    }

    public static Decoration buildBox(Reward reward) {
        return new Searchable("box", reward, 18, 24, 1, 1);
    }

    public static Decoration buildSmallChest(Reward reward) {
        return new Searchable("small_chest", reward, 9, 12, 1, 1);
    }

    public static List<Decoration> getRoomContent(int threatLevel) {
        List<Decoration> l = new ArrayList<>();
        int total = Math.random() < 0.3 ? 0 : 1;
        for (int n = 0; n < total; n++) {
            l.add(getRandomReward(threatLevel));
        }
        return l;
    }

    private static Decoration getRandomReward(int threatLevel) {
        // create random reward
        Reward reward = null;
        int gold = 0;
        Item item = ItemFactory.getRandomItem(threatLevel);
        if (item == null) {
            gold = (int) (Math.pow(threatLevel, 2) * 25 * (int) (Math.random() * 4));
        }
        if (item != null || gold > 0) {
            reward = new Reward(item, gold, 0);
        }

        int random = (int) (Math.random() * 4);
        switch (random) {
            case 0:
                return buildSmallChest(reward);
            case 1:
                return buildBox(reward);
            case 2:
                return buildBarrel(reward);
            default:
                return buildTreasureChest(reward);
        }
    }

}
