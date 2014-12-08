package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.dungeons.decorations.Decoration;
import com.glevel.dungeonhero.models.dungeons.decorations.Light;
import com.glevel.dungeonhero.models.dungeons.decorations.Searchable;
import com.glevel.dungeonhero.models.dungeons.decorations.TreasureChest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class DecorationFactory {

    public static List<Decoration> getAll() {
        List<Decoration> lst = new ArrayList<Decoration>();
        lst.add(buildLight());
        lst.add(buildBox());
        lst.add(buildBarrel());
        lst.add(buildSmallChest());
        lst.add(buildTreasureChest());
        return lst;
    }

    public static Decoration buildLight() {
        return new Light(R.string.light, "light.png");
    }

    public static Decoration buildTreasureChest() {
        return new TreasureChest(new Reward(WeaponFactory.buildSword(), 210, 0));
    }

    public static Decoration buildBarrel() {
        return new Searchable(R.string.barrel, "barrel.png", new Reward(null, 210, 0), 13, 16, 1, 1);
    }

    public static Decoration buildBox() {
        return new Searchable(R.string.box, "box.png", new Reward(null, 210, 0), 18, 24, 1, 1);
    }

    public static Decoration buildSmallChest() {
        return new Searchable(R.string.treasure_chest, "small_chest.png", new Reward(null, 210, 0), 9, 12, 1, 1);
    }

//    public static Decoration buildRewards() {
//
//    }

}
