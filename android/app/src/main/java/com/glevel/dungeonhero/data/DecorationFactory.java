package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.dungeons.decorations.Decoration;
import com.glevel.dungeonhero.models.dungeons.decorations.Light;
import com.glevel.dungeonhero.models.dungeons.decorations.TreasureChest;
import com.glevel.dungeonhero.models.items.Item;

/**
 * Created by guillaume ON 10/6/14.
 */
public class DecorationFactory {

    public static Decoration buildLight() {
        return new Light(R.string.light, "light.png");
    }

    public static Decoration buildTreasureChest() {
        return new TreasureChest(R.string.treasure_chest, "chest.png", new Reward(new Item(R.string.about, R.drawable.ic_attack), 210, 20));
    }

}
