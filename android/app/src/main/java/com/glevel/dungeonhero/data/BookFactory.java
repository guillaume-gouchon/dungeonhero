package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.activities.games.IntroductionQuest;
import com.glevel.dungeonhero.activities.games.TutorialQuest;
import com.glevel.dungeonhero.data.characters.MonsterFactory;
import com.glevel.dungeonhero.data.characters.PNJFactory;
import com.glevel.dungeonhero.data.items.WeaponFactory;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Chapter;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.discussions.Discussion;
import com.glevel.dungeonhero.models.discussions.Reaction;
import com.glevel.dungeonhero.models.discussions.callbacks.EnemyDiscussionCallback;
import com.glevel.dungeonhero.models.dungeons.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class BookFactory {

    public static List<Book> getAll() {
        List<Book> lst = new ArrayList<>();
        lst.add(buildTutorial());
        lst.add(buildInitiationBook());
        lst.add(buildVanarkBook());
        lst.add(buildBalrogQuest());
        lst.add(buildWizardQuest());
        lst.add(buildPartnershipQuest());
        lst.add(buildDreamQuest());
        return lst;
    }

    public static Book buildTutorial() {
        Book book = new Book.Builder(0, "tutorial", 0).setIntro("tutorial_intro").setActivityClass(TutorialQuest.class).build();

        // chapter 1
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("", "", events, 4, 1));

        return book;
    }

    public static Book buildInitiationBook() {
        Book book = new Book.Builder(1, "initiation_quest", 0).setIntro("initiation_intro").setOutro("initiation_outro").setActivityClass(IntroductionQuest.class).build();

        // chapter 1
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("initiation_chapter_1", "initiation_chapter_1_outro", events, 3, 3));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(true).addMonster(MonsterFactory.buildOrcCaptain()).addPnj(PNJFactory.buildInitiationQuestGirl()).build());
        book.addChapter(new Chapter("initiation_chapter_2", "", events, 3, 3));

        book.getResourcesToLoad().add(PNJFactory.buildTutorialPNJ());

        return book;
    }

    public static Book buildVanarkBook() {
        Book book = new Book.Builder(2, "sons_quest", 1).setIntro("sons_intro").setOutro("sons_outro").build();

        // chapter 1
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildTiggy()).build());
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("", "sons_chapter_1_outro", events, 3, 3));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(false).addMonster(MonsterFactory.buildTroll()).addReward(new Reward(WeaponFactory.buildSwordOfPotential())).build());
        events.add(new Event.Builder(true).addPnj(PNJFactory.buildVanark()).build());
        book.addChapter(new Chapter("", "sons_chapter_2_outro", events, 3, 3));

        return book;
    }

    public static Book buildBalrogQuest() {
        Book book = new Book.Builder(3, "balrog_quest", 1).setIntro("balrog_intro").setOutro("balrog_outro").build();

        // chapter 1
        List<Event> events = new ArrayList<>();
        Pnj whiteWizard = PNJFactory.buildWhiteWizard();
        events.add(new Event.Builder(false).addPnj(whiteWizard).build());
        events.add(new Event.Builder(false).addPnj(whiteWizard).build());
        events.add(new Event.Builder(false).addPnj(whiteWizard).build());
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("balrog_chapter_1_intro", "balrog_chapter_1_outro", events, 3, 3));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(true).addPnj(PNJFactory.buildBalrog()).build());
        book.addChapter(new Chapter("", "balrog_chapter_2_outro", events, 3, 3));

        return book;
    }

    public static Book buildWizardQuest() {
        Book book = new Book.Builder(4, "wizard_quest", 2).setIntro("wizard_intro").build();

        // chapter 1
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildMinsk()).build());
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildMontaron()).build());
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("wizard_chapter_1_intro", "wizard_chapter_1_outro", events, 3, 3));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(false).addReward(new Reward(WeaponFactory.buildElvenThrowingDaggers())).build());
        events.add(new Event.Builder(true).addPnj(PNJFactory.buildXsar()).addMonster(MonsterFactory.buildTroll()).build());
        book.addChapter(new Chapter("wizard_chapter_2_intro", "wizard_chapter_2_outro", events, 3, 3));

        return book;
    }

    public static Book buildPartnershipQuest() {
        Book book = new Book.Builder(5, "partnership_quest", 2).setIntro("partnership_intro").build();

        // chapter 1
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildLink()).addMonster(MonsterFactory.buildGargoyle()).addMonster(MonsterFactory.buildGargoyle()).build());
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildLink()).addMonster(MonsterFactory.buildOgre()).addMonster(MonsterFactory.buildOgre()).build());
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildLink()).addMonster(MonsterFactory.buildTroll()).addMonster(MonsterFactory.buildTroll()).build());
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("partnership_chapter_1_intro", "", events, 3, 3));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildLinkLoot()).addReward(new Reward(null, 7, 0)).build());
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildLinkLoot()).addReward(new Reward(null, 3, 0)).build());
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildZelda()).build());
        events.add(new Event.Builder(true).addPnj(PNJFactory.buildDoomLord()).addPnj(PNJFactory.buildLinkLast()).addMonster(MonsterFactory.buildChaosWarrior()).build());
        book.addChapter(new Chapter("", "partnership_chapter_2_outro", events, 3, 3));

        return book;
    }

    public static Book buildDreamQuest() {
        Book book = new Book.Builder(6, "dreamer_quest", 3).setIntro("dreamer_intro").setOutro("dreamer_outro").build();

        // chapter 1
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildTutorialPNJ())).build());
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildInitiationQuestGirl())).build());
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildTiggy())).build());
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildVanark())).build());
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildBalrog())).build());
        events.add(new Event.Builder(true).addPnj(turnPnjToGhost(PNJFactory.buildXsar())).build());
        book.addChapter(new Chapter("dreamer_chapter_1_intro", "", events, 3, 3));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildMinsk())).build());
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildMontaron())).build());
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildLink())).build());
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildZelda())).build());
        events.add(new Event.Builder(false).addPnj(turnPnjToGhost(PNJFactory.buildDoomLord())).build());
        events.add(new Event.Builder(true).addPnj(PNJFactory.buildZangdar()).addMonster(MonsterFactory.buildGargoyle()).build());
        book.addChapter(new Chapter("dreamer_chapter_2_intro", "dreamer_chapter_2_outro", events, 3, 3));

        return book;
    }

    private static Pnj turnPnjToGhost(Pnj pnj) {
        pnj.setAutoTalk(true);
        while (pnj.getDiscussions().size() > 0) {
            pnj.getDiscussions().remove(0);
        }

        Discussion discussion = new Discussion("dreamer_ghost_1", false, null, new EnemyDiscussionCallback(pnj));
        discussion.addReaction(new Reaction("dreamer_ghost_1_answer_1", 0));
        pnj.getDiscussions().add(discussion);

        return pnj;
    }

}
