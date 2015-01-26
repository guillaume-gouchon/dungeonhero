package com.glevel.dungeonhero.game.gui;

import android.app.Dialog;
import android.content.Context;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.views.Minimap;

/**
 * Created by guillaume on 1/14/15.
 */
public class DungeonMap extends Dialog {

    public DungeonMap(Context context, Dungeon dungeon) {
        super(context, R.style.Dialog);
        setContentView(R.layout.in_game_dungeon_map);
        setCancelable(true);

        ((Minimap) findViewById(R.id.minimap)).setDungeon(dungeon);
    }

}
