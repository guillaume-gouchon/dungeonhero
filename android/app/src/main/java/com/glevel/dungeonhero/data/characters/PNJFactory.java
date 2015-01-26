package com.glevel.dungeonhero.data.characters;

import com.glevel.dungeonhero.data.items.ArmorFactory;
import com.glevel.dungeonhero.data.items.PotionFactory;
import com.glevel.dungeonhero.data.items.RingFactory;
import com.glevel.dungeonhero.data.items.WeaponFactory;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Ranks;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.Reaction;
import com.glevel.dungeonhero.models.discussions.callbacks.DeathDiscussionCallback;
import com.glevel.dungeonhero.models.discussions.callbacks.EnemyDiscussionCallback;
import com.glevel.dungeonhero.models.discussions.callbacks.StopDiscussionCallback;
import com.glevel.dungeonhero.models.discussions.callbacks.WhiteWizardDiscussionCallback;
import com.glevel.dungeonhero.models.discussions.riddles.OpenRiddle;

/**
 * Created by guillaume ON 10/6/14.
 */
public class PNJFactory {

    public static Pnj buildTutorialPNJ() {
        Pnj pnj = new Pnj("tutorial_character", Ranks.NEUTRAL, 35, 2, 20, 14, 9, 0, 0, 5, Hero.HeroTypes.STR, false);

        // intro
        Discussion discussion = new Discussion("initiation_tutorial_intro", false, null);
        discussion.addReaction(new Reaction("yes", 0));
        discussion.addReaction(new Reaction("no", 1));
        pnj.getDiscussions().add(discussion);

        // explain tips
        discussion = new Discussion("initiation_tutorial_tips", false, null);
        discussion.addReaction(new Reaction("ok_get_it", 0));
        pnj.getDiscussions().add(discussion);

        // outro
        discussion = new Discussion("initiation_tutorial_outro", false, null);
        discussion.addReaction(new Reaction("initiation_die_weak", 1));
        discussion.addReaction(new Reaction("initiation_pray", 0));
        pnj.getDiscussions().add(discussion);

        // give reward
        discussion = new Discussion("initiation_tutorial_outro_reward", false, new Reward(PotionFactory.buildForcePotion()));
        discussion.addReaction(new Reaction("initiation_tutorial_thanks", 0));
        pnj.getDiscussions().add(discussion);

        pnj.setDiscussionCallback(new DeathDiscussionCallback(pnj));

        return pnj;
    }

    public static Pnj buildInitiationQuestGirl() {
        Pnj pnj = new Pnj("initiation_quest_girl", Ranks.NEUTRAL, 7, 7, 4, 10, 10, 5, 0, 1, Hero.HeroTypes.SPI, false);
        pnj.equip(WeaponFactory.buildDagger(0));

        pnj.setReward(new Reward(null, 5, 10));

        // who are you ?
        Discussion discussion = new Discussion("initiation_girl_1", false, null);
        discussion.addReaction(new Reaction("initiation_girl_1_answer_1", 0));
        discussion.addReaction(new Reaction("initiation_girl_1_answer_2", 1));
        discussion.addReaction(new Reaction("initiation_girl_1_answer_3", 2));
        pnj.getDiscussions().add(discussion);

        // why ?
        discussion = new Discussion("initiation_girl_11", false, null);
        discussion.addReaction(new Reaction("initiation_girl_11_answer_1", 2));
        discussion.addReaction(new Reaction("initiation_girl_11_answer_2", 3));
        pnj.getDiscussions().add(discussion);

        // yes, rescue her
        discussion = new Discussion("initiation_girl_12", false, new Reward(null, 0, 100));
        discussion.addReaction(new Reaction("initiation_girl_12_answer_1", 3));
        pnj.getDiscussions().add(discussion);

        // rob her
        discussion = new Discussion("initiation_girl_13", false, new Reward(null, 35, 0));
        discussion.addReaction(new Reaction("initiation_girl_13_answer_1", 2));
        pnj.getDiscussions().add(discussion);

        // she slaps you
        discussion = new Discussion("initiation_girl_111", false, null, new EnemyDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("initiation_girl_111_answer_1", 1));
        pnj.getDiscussions().add(discussion);

        // she gives you her ring
        discussion = new Discussion("initiation_girl_112", false, new Reward(RingFactory.getRandomRing(1)));
        discussion.addReaction(new Reaction("initiation_girl_112_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildTiggy() {
        Pnj pnj = new Pnj("tiggy", Ranks.NEUTRAL, 18, 18, 8, 16, 11, 5, 0, 3, Hero.HeroTypes.DEX, true);
        pnj.equip(WeaponFactory.buildDarkElvenDagger());
        pnj.equip(WeaponFactory.buildDarkElvenDagger());
        pnj.getSkills().add(SkillFactory.buildDodgeMaster().improve().improve());
        pnj.getSkills().add(SkillFactory.buildFatalBlow().improve());

        pnj.setReward(new Reward(WeaponFactory.buildDarkElvenDagger(), 0, 70));

        // who are you ?
        Discussion discussion = new Discussion("tiggy_1", false, null);
        discussion.addReaction(new Reaction("tiggy_1_answer_1", 0));
        discussion.addReaction(new Reaction("tiggy_1_answer_2", 3));
        discussion.addReaction(new Reaction("tiggy_1_answer_3", 4));
        pnj.getDiscussions().add(discussion);

        // next target ?
        discussion = new Discussion(new OpenRiddle(25, "tiggy_11", "gundin"));
        pnj.getDiscussions().add(discussion);

        // wrong answer
        discussion = new Discussion("tiggy_11_wrong", false, null, new EnemyDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("tiggy_11_wrong_answer_1", 4));
        pnj.getDiscussions().add(discussion);

        // right answer
        discussion = new Discussion("tiggy_11_right", false, new Reward(RingFactory.buildSOVRing(), 0, 80));
        discussion.addReaction(new Reaction("ok", 3));
        pnj.getDiscussions().add(discussion);

        // attack
        discussion = new Discussion("tiggy_12", false, null, new EnemyDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("tiggy_12_answer_1", 2));
        pnj.getDiscussions().add(discussion);

        // business
        discussion = new Discussion("tiggy_13", false, null, new StopDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("ok", 0));
        pnj.getDiscussions().add(discussion);

        // you again ?
        discussion = new Discussion("tiggy_131", true, null, new EnemyDiscussionCallback(pnj));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildVanark() {
        Pnj pnj = new Pnj("vanark", Ranks.ENEMY, 25, 25, 13, 12, 10, 5, 0, 3, Hero.HeroTypes.STR, true);
        pnj.equip(WeaponFactory.buildMorgenstern(1));
        pnj.equip(WeaponFactory.buildLargeShield(1));
        pnj.equip(ArmorFactory.buildLamellar(1));
        pnj.getSkills().add(SkillFactory.buildPoisonousDarts().improve());

        pnj.setReward(new Reward(null, 250, 120));

        Discussion discussion = new Discussion("vanark_1", false, null);
        discussion.addReaction(new Reaction("vanark_1_answer_1", 0));
        discussion.addReaction(new Reaction("vanark_1_answer_2", 0));
        discussion.addReaction(new Reaction("vanark_1_answer_3", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildWhiteWizard() {
        Pnj pnj = new Pnj("white_wizard", Ranks.NEUTRAL, 42, 42, 10, 12, 20, 4, 0, 6, Hero.HeroTypes.SPI, false);
        pnj.equip(WeaponFactory.buildWizardStaff(6));
        pnj.equip(ArmorFactory.buildRobe(6));
        pnj.getSkills().add(SkillFactory.buildFireball().improve().improve().improve());
        pnj.getSkills().add(SkillFactory.buildFireball().improve().improve().improve());

        pnj.setReward(new Reward(null, 0, 0));

        // riddle 1
        Discussion discussion = new Discussion(new OpenRiddle(40, "white_wizard_1", "nothing"));
        pnj.getDiscussions().add(discussion);

        // wrong answer
        discussion = new Discussion("white_wizard_wrong", false, null, new WhiteWizardDiscussionCallback(pnj, false));
        discussion.addReaction(new Reaction("white_wizard_wrong_answer", 1));
        pnj.getDiscussions().add(discussion);

        // right answer
        discussion = new Discussion("white_wizard_right", false, new Reward(null, 0, 40), new WhiteWizardDiscussionCallback(pnj, true));
        pnj.getDiscussions().add(discussion);

        // riddle 2
        discussion = new Discussion(new OpenRiddle(40, "white_wizard_2", "fire"));
        pnj.getDiscussions().add(discussion);

        // wrong answer
        discussion = new Discussion("white_wizard_wrong", false, null, new WhiteWizardDiscussionCallback(pnj, false));
        discussion.addReaction(new Reaction("white_wizard_wrong_answer", 1));
        pnj.getDiscussions().add(discussion);

        // right answer
        discussion = new Discussion("white_wizard_right", false, new Reward(null, 0, 60), new WhiteWizardDiscussionCallback(pnj, true));
        pnj.getDiscussions().add(discussion);

        // riddle 3
        discussion = new Discussion(new OpenRiddle(40, "white_wizard_3", "secret"));
        pnj.getDiscussions().add(discussion);

        // wrong answer
        discussion = new Discussion("white_wizard_wrong", false, null, new WhiteWizardDiscussionCallback(pnj, false));
        discussion.addReaction(new Reaction("white_wizard_wrong_answer", 1));
        pnj.getDiscussions().add(discussion);

        // right answer
        discussion = new Discussion("white_wizard_right_final", false, new Reward(null, 0, 80), new WhiteWizardDiscussionCallback(pnj, true));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildBalrog() {
        Pnj pnj = new Pnj("balrog", Ranks.NEUTRAL, 22, 22, 11, 16, 9, 6, 0, 3, Hero.HeroTypes.DEX, true);
        pnj.equip(WeaponFactory.buildShortSword(2));
        pnj.equip(WeaponFactory.buildShortSword(2));
        pnj.equip(ArmorFactory.buildLeatherPlastron(2));
        pnj.getSkills().add(SkillFactory.buildDodgeMaster().improve().improve().improve());

        pnj.setReward(new Reward(null, 150, 80));

        // hello
        Discussion discussion = new Discussion("balrog_1", false, null);
        discussion.addReaction(new Reaction("balrog_1_answer_1", 4));
        discussion.addReaction(new Reaction("balrog_1_answer_2", 0));
        pnj.getDiscussions().add(discussion);

        // why are you here ?
        discussion = new Discussion("balrog_12", false, null);
        discussion.addReaction(new Reaction("balrog_12_answer_1", 0));
        discussion.addReaction(new Reaction("balrog_12_answer_2", 3));
        pnj.getDiscussions().add(discussion);

        // what do you want ?
        discussion = new Discussion("balrog_121", false, null);
        discussion.addReaction(new Reaction("balrog_121_answer_1", 0));
        discussion.addReaction(new Reaction("balrog_121_answer_2", 1));
        pnj.getDiscussions().add(discussion);

        // teach dice game
        discussion = new Discussion("balrog_1211", false, new Reward(null, 0, 150));
        discussion.addReaction(new Reaction("ok", 2));
        pnj.getDiscussions().add(discussion);

        // play dice
        discussion = new Discussion("balrog_1212", false, null);
        discussion.addReaction(new Reaction("balrog_1212_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        // die demon !
        discussion = new Discussion("balrog_answer_fight", false, null, new EnemyDiscussionCallback(pnj));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildXsar() {
        Pnj pnj = new Pnj("xsar", Ranks.ENEMY, 30, 30, 10, 12, 18, 5, 0, 5, Hero.HeroTypes.SPI, true);
        pnj.equip(WeaponFactory.buildDagger(3));
        pnj.equip(ArmorFactory.buildRobe(3));
        pnj.getSkills().add(SkillFactory.buildSleep().improve().improve());
        pnj.getSkills().add(SkillFactory.buildStoneSkin().improve().improve());
        pnj.getSkills().add(SkillFactory.buildFireball().improve());
        pnj.getSkills().add(SkillFactory.buildPoisonousDarts().improve());

        pnj.setReward(new Reward(null, 200, 160));

        // hello
        Discussion discussion = new Discussion("xsar_1", false, null);
        discussion.addReaction(new Reaction("xsar_1_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildMinsk() {
        Pnj pnj = new Pnj("minsk", Ranks.ENEMY, 28, 28, 18, 12, 4, 4, 0, 3, Hero.HeroTypes.STR, true);
        pnj.equip(WeaponFactory.buildBroadSword(2));
        pnj.equip(ArmorFactory.buildLamellar(2));
        pnj.getSkills().add(SkillFactory.buildRage().improve().improve());
        pnj.getSkills().add(SkillFactory.buildParryScience().improve().improve());

        pnj.setReward(new Reward(null, 140, 100));

        // hello
        Discussion discussion = new Discussion("minsk_1", false, null);
        discussion.addReaction(new Reaction("minsk_1_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildMontaron() {
        Pnj pnj = new Pnj("montaron", Ranks.NEUTRAL, 23, 23, 13, 15, 9, 5, 0, 3, Hero.HeroTypes.STR_DEX, false);
        pnj.equip(WeaponFactory.buildMorgenstern(2));
        pnj.equip(WeaponFactory.buildRoundShield(2));
        pnj.equip(ArmorFactory.buildLeatherPlastron(2));
        pnj.getSkills().add(SkillFactory.buildDodgeMaster().improve());
        pnj.getSkills().add(SkillFactory.buildPoisonousDarts().improve().improve());
        pnj.getSkills().add(SkillFactory.buildStoneSkin().improve());

        pnj.setReward(new Reward(null, 90, 120));

        // hello
        Discussion discussion = new Discussion("montaron_1", false, null, new EnemyDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("montaron_1_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildLink() {
        Pnj pnj = new Pnj("link", Ranks.ALLY, 26, 26, 13, 14, 9, 4, 0, 4, Hero.HeroTypes.STR_DEX, true);
        pnj.equip(WeaponFactory.buildLongSword(2));
        pnj.equip(WeaponFactory.buildRoundShield(2));
        pnj.equip(ArmorFactory.buildLeatherVest(2));
        pnj.getSkills().add(SkillFactory.buildDodgeMaster().improve().improve());

        pnj.setReward(new Reward(null, 220, 160));

        // hello
        Discussion discussion = new Discussion("link_1", false, null, new StopDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("link_1_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        // thank for the help
        discussion = new Discussion("link_2", true, null);
        discussion.addReaction(new Reaction("link_2_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildLinkLoot() {
        Pnj pnj = buildLink();
        pnj.getDiscussions().remove(0);
        pnj.getDiscussions().remove(0);

        // I already looted everything
        Discussion discussion = new Discussion("link_3", true, null, new StopDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("link_3_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildZelda() {
        Pnj pnj = new Pnj("zelda", Ranks.NEUTRAL, 13, 13, 7, 12, 9, 5, 0, 1, Hero.HeroTypes.DEX_SPI, false);
        pnj.equip(WeaponFactory.buildDagger(3));
        pnj.getSkills().add(SkillFactory.buildCharm().improve().improve().improve());
        pnj.getSkills().add(SkillFactory.buildCharm().improve().improve().improve());

        pnj.setReward(new Reward(null, 200, 80));

        // save me
        Discussion discussion = new Discussion("zelda_1", false, null);
        discussion.addReaction(new Reaction("zelda_1_answer_1", 0));
        discussion.addReaction(new Reaction("zelda_1_answer_2", 1));
        pnj.getDiscussions().add(discussion);

        // kiss
        discussion = new Discussion("zelda_11", false, null);
        discussion.addReaction(new Reaction("zelda_11_answer_1", 1));
        pnj.getDiscussions().add(discussion);

        // I prefer gold coins
        discussion = new Discussion("zelda_12", false, new Reward(null, 200, 0));
        discussion.addReaction(new Reaction("zelda_12_answer_1", 0));
        pnj.getDiscussions().add(discussion);


        return pnj;
    }

    public static Pnj buildLinkLast() {
        Pnj pnj = buildLink();
        pnj.setAutoTalk(false);
        pnj.getDiscussions().remove(0);
        pnj.getDiscussions().remove(0);

        // it was cool
        Discussion discussion = new Discussion("link_4", false, null);
        discussion.addReaction(new Reaction("link_4_answer_1", 0));
        discussion.addReaction(new Reaction("link_4_answer_2", 1));
        pnj.getDiscussions().add(discussion);

        // see you at the tavern
        discussion = new Discussion("link_41", true, null);
        pnj.getDiscussions().add(discussion);

        // attack
        discussion = new Discussion("link_42", false, null, new EnemyDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("link_42_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildDoomLord() {
        Pnj pnj = new Pnj("doom_lord", Ranks.ENEMY, 30, 30, 15, 8, 11, 3, 0, 5, Hero.HeroTypes.STR, true);
        pnj.equip(WeaponFactory.buildLongSword(3));
        pnj.equip(ArmorFactory.buildBreastPlate(2));
        pnj.getSkills().add(SkillFactory.buildFatalBlow().improve());
        pnj.getSkills().add(SkillFactory.buildFrostArrow().improve().improve());
        pnj.getSkills().add(SkillFactory.buildTerror().improve());

        pnj.setReward(new Reward(null, 120, 190));

        // hello
        Discussion discussion = new Discussion("doom_lord_1", false, null);
        discussion.addReaction(new Reaction("doom_lord_1_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildZangdar() {
        Pnj pnj = new Pnj("zangdar", Ranks.ENEMY, 26, 26, 11, 14, 25, 5, 0, 5, Hero.HeroTypes.SPI, true);
        pnj.equip(WeaponFactory.buildWizardStaff(5));
        pnj.equip(ArmorFactory.buildRobe(5));
        pnj.equip(RingFactory.getRandomRing(5));
        pnj.equip(RingFactory.getRandomRing(5));
        pnj.getSkills().add(SkillFactory.buildSleep().improve().improve());
        pnj.getSkills().add(SkillFactory.buildSleep().improve().improve());
        pnj.getSkills().add(SkillFactory.buildSleep().improve().improve());
        pnj.getSkills().add(SkillFactory.buildSleep().improve().improve());

        pnj.setReward(new Reward(null, 180, 250));

        // hello
        Discussion discussion = new Discussion("zangdar_1", false, null);
        discussion.addReaction(new Reaction("zangdar_1_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }
}
