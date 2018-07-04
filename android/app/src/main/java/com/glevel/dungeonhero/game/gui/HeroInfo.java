package com.glevel.dungeonhero.game.gui;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.characters.Hero;

public class HeroInfo extends Dialog {

    public HeroInfo(Context context, Hero hero) {
        super(context, R.style.Dialog);
        setContentView(R.layout.in_game_hero_details);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        ((TextView) findViewById(R.id.name)).setText(hero.getHeroName());
        ((TextView) findViewById(R.id.description)).setText(hero.getDescription(context.getResources()));
        ((TextView) findViewById(R.id.hp)).setText(context.getString(R.string.hp_in_game, hero.getCurrentHP(), hero.getHp()));
        ((TextView) findViewById(R.id.level)).setText(context.getString(R.string.level_in_game, hero.getLevel(), hero.getXp(), hero.getNextLevelXPAmount()));
        ((TextView) findViewById(R.id.strength)).setText(String.valueOf(hero.getStrength()));
        ((TextView) findViewById(R.id.dexterity)).setText(String.valueOf(hero.getDexterity()));
        ((TextView) findViewById(R.id.spirit)).setText(String.valueOf(hero.getSpirit()));
        ((TextView) findViewById(R.id.movement)).setText(String.valueOf(hero.calculateMovement()));
        ((TextView) findViewById(R.id.damage)).setText(context.getString(R.string.damage) + " : " + hero.getReadableDamage());
        ((TextView) findViewById(R.id.protection)).setText(context.getString(R.string.protection) + " : " + hero.calculateProtection());
        ((TextView) findViewById(R.id.dodge)).setText(context.getString(R.string.dodge) + " : " + hero.calculateDodge() + "%");
        ((TextView) findViewById(R.id.block)).setText(context.getString(R.string.block) + " : " + hero.calculateBlock() + "%");
        ((TextView) findViewById(R.id.critical)).setText(context.getString(R.string.critical) + " : " + hero.calculateCritical() + "%");
        ((TextView) findViewById(R.id.frags)).setText(context.getString(R.string.frags, hero.getFrags().size()));
    }

}
