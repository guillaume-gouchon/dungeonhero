package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.effects.BuffEffect;
import com.glevel.dungeonhero.models.effects.CamouflageEffect;
import com.glevel.dungeonhero.models.effects.DamageEffect;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.HeroicEffect;
import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.effects.PoisonEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;

/**
 * Created by guillaume ON 10/6/14.
 */
public class SkillFactory {

    public static Skill buildRage() {
        BuffEffect extra = new BuffEffect(null, Characteristics.PROTECTION, -5, 3, null);
        Effect effect = new BuffEffect("blood.png", Characteristics.DAMAGE, 10, 3, extra);
        return new ActiveSkill(R.string.berserker_rage, R.string.berserker_rage_description, R.drawable.ic_rage, 0, true, 0, effect);
    }

    public static Skill buildSwirlOfSwords() {
        Effect effect = new DamageEffect("blood.png", -12);
        return new ActiveSkill(R.string.swirl_swords, R.string.swirl_swords_description, R.drawable.ic_swirl, 0, true, 1, effect);
    }

    public static Skill buildWarcry() {
        Effect effect = new StunEffect("blood.png", Characteristics.STRENGTH, 12);
        return new ActiveSkill(R.string.war_cry, R.string.war_cry_description, R.drawable.ic_cry, 0, true, 7, effect);
    }

    public static Skill buildFatalBlow() {
        Effect effect = new PermanentEffect(Characteristics.CRITICAL, 15, null);
        return new PassiveSkill(R.string.fatal_blow, R.string.fatal_blow_description, R.drawable.ic_critics, 0, effect);
    }

    public static Skill buildCamouflage() {
        Effect effect = new CamouflageEffect("blood.png");
        return new ActiveSkill(R.string.camouflage, R.string.camouflage_description, R.drawable.ic_camouflage, 0, true, 0, effect);
    }

    public static Skill buildPoisonousDarts() {
        Effect extra = new PoisonEffect("blood.png", -5, 3, null);
        Effect effect = new DamageEffect("blood.png", -8, extra);
        return new ActiveSkill(R.string.poisonous_darts, R.string.poisonous_darts_description, R.drawable.ic_poisonous_darts, 0, false, 0, effect);
    }

    public static Skill buildDodgeMaster() {
        Effect effect = new PermanentEffect(Characteristics.DODGE, 20, null);
        return new PassiveSkill(R.string.dodge_master, R.string.dodge_master_description, R.drawable.ic_dodge, 0, effect);
    }

    public static Skill buildGroundSlam() {
        Effect extra = new StunEffect("blood.png", Characteristics.STRENGTH, 3);
        Effect effect = new DamageEffect("blood.png", -12, extra);
        return new ActiveSkill(R.string.ground_slam, R.string.ground_slam_description, R.drawable.ic_ground_slam, 0, true, 1, effect);
    }

    public static Skill buildParryScience() {
        Effect effect = new PermanentEffect(Characteristics.BLOCK, 20, null);
        return new PassiveSkill(R.string.parry_science, R.string.parry_science_description, R.drawable.ic_parry_science, 0, effect);
    }

    public static Skill buildDrunkenMaster() {
        Effect extra = new BuffEffect("blood.png", Characteristics.BLOCK, -100, 8, null);
        Effect effect = new DamageEffect("blood.png", 15, extra);
        return new ActiveSkill(R.string.drunken_master, R.string.drunken_master_description, R.drawable.ic_drunken_master, 0, true, 0, effect);
    }

    public static Skill buildFrostArrow() {
        Effect extra = new BuffEffect("blood.png", Characteristics.MOVEMENT, -20, 6, null);
        Effect effect = new DamageEffect("blood.png", -12, extra);
        return new ActiveSkill(R.string.frost_arrow, R.string.frost_arrow_description, R.drawable.ic_frost_arrow, 0, false, 0, effect);
    }

    public static Skill buildCharm() {
        Effect effect = new StunEffect("blood.png", Characteristics.SPIRIT, 8);
        return new ActiveSkill(R.string.charm, R.string.charm_description, R.drawable.ic_charm, 0, false, 0, effect);
    }

    public static Skill buildStarFall() {
        Effect effect = new DamageEffect("blood.png", -8, null);
        return new ActiveSkill(R.string.star_fall, R.string.star_fall_description, R.drawable.ic_star_fall, 0, false, 2, effect);
    }

    public static Skill buildParalysingPlants() {
        Effect extra = new StunEffect("blood.png", Characteristics.STRENGTH, 5);
        Effect effect = new BuffEffect("blood.png", Characteristics.MOVEMENT, -20, 5, extra);
        return new ActiveSkill(R.string.paralysing_plant, R.string.paralysing_plant_description, R.drawable.ic_paralysing_plant, 0, false, 3, effect);
    }

    public static Skill buildHealingHerbs() {
        Effect effect = new DamageEffect("blood.png", 10, null);
        return new ActiveSkill(R.string.healing_plants, R.string.healing_plants_description, R.drawable.ic_healing_herbs, 0, true, 0, effect);
    }

    public static Skill buildWolfHowl() {
        Effect extra = new BuffEffect("blood.png", Characteristics.DODGE, 30, 5, null);
        Effect effect = new BuffEffect("blood.png", Characteristics.CRITICAL, 25, 5, extra);
        return new ActiveSkill(R.string.wolf_howl, R.string.wolf_howl_description, R.drawable.ic_wolf_howl, 0, true, 0, effect);
    }

    public static Skill buildCrowCurse() {
        Effect extra = new BuffEffect("blood.png", Characteristics.PROTECTION, -10, 5, null);
        Effect effect = new BuffEffect("blood.png", Characteristics.DAMAGE, -10, 5, extra);
        return new ActiveSkill(R.string.crow_curse, R.string.crow_curse_description, R.drawable.ic_crow_curse, 0, false, 3, effect);
    }

    public static Skill buildFireball() {
        Effect extra = new PoisonEffect("blood.png", -3, 2, null);
        Effect effect = new DamageEffect("blood.png", -15, extra);
        return new ActiveSkill(R.string.fireball, R.string.fireball_description, R.drawable.ic_fireball, 0, false, 1, effect);
    }

    public static Skill buildSleep() {
        Effect effect = new StunEffect("blood.png", Characteristics.SPIRIT, 10);
        return new ActiveSkill(R.string.sleep, R.string.sleep_description, R.drawable.ic_sleep, 0, false, 0, effect);
    }

    public static Skill buildThunderStorm() {
        Effect effect = new DamageEffect("blood.png", -7, null);
        return new ActiveSkill(R.string.thunder_storm, R.string.thunder_storm_description, R.drawable.ic_thunder_storm, 0, false, 2, effect);
    }

    public static Skill buildStoneSkin() {
        Effect effect = new BuffEffect("blood.png", Characteristics.PROTECTION, 15, 7, null);
        return new ActiveSkill(R.string.stone_skin, R.string.stone_skin_description, R.drawable.ic_stone_skin, 0, true, 0, effect);
    }

    public static Skill buildTerror() {
        Effect extra = new StunEffect("blood.png", Characteristics.SPIRIT, 6);
        Effect effect = new PoisonEffect("blood.png", -3, 5, extra);
        return new ActiveSkill(R.string.terror, R.string.terror_description, R.drawable.ic_terror, 0, false, 3, effect);
    }

    public static Skill buildHeroic() {
        Effect effect = new HeroicEffect("blood.png");
        return new ActiveSkill(R.string.about_title, R.string.about_title, R.drawable.ic_key, 0, true, 0, effect);
    }

}
