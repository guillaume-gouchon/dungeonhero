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
import com.glevel.dungeonhero.models.discussions.DiscussionCallback;
import com.glevel.dungeonhero.models.discussions.Reaction;
import com.glevel.dungeonhero.models.discussions.riddles.OpenRiddle;

/**
 * Created by guillaume ON 10/6/14.
 */
public class PNJFactory {

    public static Pnj buildTutorialPNJ() {
        final Pnj pnj = new Pnj("tutorial_character", Ranks.NEUTRAL, 35, 2, 20, 14, 9, 0, 0, 5, Hero.HeroTypes.STR, false);

        // intro
        Discussion discussion = new Discussion("initiation_tutorial_intro", false, null);
        discussion.addReaction(new Reaction("ok", 0));
        pnj.getDiscussions().add(discussion);

        // explain movement
        discussion = new Discussion("initiation_tutorial_movement", false, null);
        discussion.addReaction(new Reaction("ok_get_it", 0));
        pnj.getDiscussions().add(discussion);

        // explain talk and search
        discussion = new Discussion("initiation_tutorial_search", false, null);
        discussion.addReaction(new Reaction("ok_get_it", 0));
        pnj.getDiscussions().add(discussion);

        // explain fighting
        discussion = new Discussion("initiation_tutorial_fight", false, null);
        discussion.addReaction(new Reaction("ok_get_it", 0));
        pnj.getDiscussions().add(discussion);

        // explain skills
        discussion = new Discussion("initiation_tutorial_skills", false, null);
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

    private static class DeathDiscussionCallback extends DiscussionCallback {

        private static final long serialVersionUID = -162134829548329521L;

        public DeathDiscussionCallback(Pnj pnj) {
            super(pnj);
        }

        @Override
        public void onDiscussionOver() {
            pnj.setCurrentHP(0);
        }
    }

    public static Pnj buildInitiationQuestGirl() {
        final Pnj pnj = new Pnj("initiation_quest_girl", Ranks.NEUTRAL, 7, 7, 4, 10, 10, 5, 0, 1, Hero.HeroTypes.SPI, false);
        pnj.equip(WeaponFactory.buildDagger(0));

        pnj.setReward(new Reward(null, 5, 0));

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

    private static class EnemyDiscussionCallback extends DiscussionCallback {

        private static final long serialVersionUID = 5869054321871679506L;

        public EnemyDiscussionCallback(Pnj pnj) {
            super(pnj);
        }

        @Override
        public void onDiscussionOver() {
            pnj.setRank(Ranks.ENEMY);
        }
    }

    public static Pnj buildTiggy() {
        final Pnj pnj = new Pnj("tiggy", Ranks.NEUTRAL, 18, 18, 8, 16, 11, 5, 0, 3, Hero.HeroTypes.DEX, true);
        pnj.equip(WeaponFactory.buildShortSword(2));
        pnj.getSkills().add(SkillFactory.buildDodgeMaster().improve().improve());
        pnj.getSkills().add(SkillFactory.buildFatalBlow().improve());

        pnj.setReward(new Reward(WeaponFactory.buildShortSword(2), 0, 45));

        // who are you ?
        Discussion discussion = new Discussion("tiggy_1", false, null);
        discussion.addReaction(new Reaction("tiggy_1_answer_1", 0));
        discussion.addReaction(new Reaction("tiggy_1_answer_2", 3));
        discussion.addReaction(new Reaction("tiggy_1_answer_3", 4));
        pnj.getDiscussions().add(discussion);

        // next target ?
        discussion = new Discussion(new OpenRiddle(25, "tiggy_11", "gundin", new Reward(null, 0, 50)));
        pnj.getDiscussions().add(discussion);

        // wrong answer
        discussion = new Discussion("tiggy_11_wrong", false, null, new EnemyDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("tiggy_11_wrong_answer_1", 4));
        pnj.getDiscussions().add(discussion);

        // right answer
        discussion = new Discussion("tiggy_11_right", false, new Reward(RingFactory.buildSOVRing()));
        discussion.addReaction(new Reaction("ok", 3));
        pnj.getDiscussions().add(discussion);

        // attack
        discussion = new Discussion("tiggy_12", false, null, new EnemyDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("tiggy_12_answer_1", 2));
        pnj.getDiscussions().add(discussion);

        // business
        discussion = new Discussion("tiggy_13", false, null);
        discussion.addReaction(new Reaction("ok", 0));
        pnj.getDiscussions().add(discussion);

        // you again ?
        discussion = new Discussion("tiggy_131", true, null, new EnemyDiscussionCallback(pnj));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

    public static Pnj buildVanark() {
        final Pnj pnj = new Pnj("vanark", Ranks.ENEMY, 25, 25, 13, 12, 10, 5, 0, 3, Hero.HeroTypes.STR, true);
        pnj.equip(WeaponFactory.buildMorgenstern(1));
        pnj.equip(WeaponFactory.buildLargeShield(1));
        pnj.equip(ArmorFactory.buildLamellar(1));
        pnj.getSkills().add(SkillFactory.buildPoisonousDarts().improve());

        pnj.setReward(new Reward(null, 250, 90));

        Discussion discussion = new Discussion("vanark_1", false, null);
        discussion.addReaction(new Reaction("vanark_1_answer_1", 0));
        discussion.addReaction(new Reaction("vanark_1_answer_2", 0));
        discussion.addReaction(new Reaction("vanark_1_answer_3", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

}
