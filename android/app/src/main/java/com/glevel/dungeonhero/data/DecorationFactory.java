package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.dungeons.decorations.Decoration;
import com.glevel.dungeonhero.models.dungeons.decorations.Light;
import com.glevel.dungeonhero.models.dungeons.decorations.TreasureChest;

/**
 * Created by guillaume ON 10/6/14.
 */
public class DecorationFactory {

    public static Decoration buildLight() {
        return new Light(R.string.light, "light.png");
    }

    public static Decoration buildTreasureChest() {
        return new TreasureChest(R.string.treasure_chest, "chest.png");
    }

}
