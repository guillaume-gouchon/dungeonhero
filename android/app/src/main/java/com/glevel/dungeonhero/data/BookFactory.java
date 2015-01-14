package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.data.characters.MonsterFactory;
import com.glevel.dungeonhero.data.characters.PNJFactory;
import com.glevel.dungeonhero.data.items.WeaponFactory;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Chapter;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.dungeons.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class BookFactory {

    public static final int TUTORIAL_BOOK_ID = 1;

    public static List<Book> getAll() {
        List<Book> lst = new ArrayList<>();
        lst.add(buildInitiationBook());
        lst.add(buildVanarkBook());
        lst.add(buildBalrogQuest());
        lst.add(buildWizardQuest());
//        lst.add(buildPartnershipQuest());
//        lst.add(buildDreamQuest());
        return lst;
    }

    public static Book buildInitiationBook() {
        Book book = new Book(TUTORIAL_BOOK_ID, "initiation_quest", "initiation_intro", "initiation_outro", null);

        // chapter 1
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("initiation_chapter_1", "initiation_chapter_1_outro", events));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(true).addMonster(MonsterFactory.buildOrcCaptain()).addPnj(PNJFactory.buildInitiationQuestGirl()).build());
        book.addChapter(new Chapter("initiation_chapter_2", "", events));

        return book;
    }

    public static Book buildVanarkBook() {
        Book book = new Book(2, "sons_quest", "sons_intro", "sons_outro", null);

        // chapter 1
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildTiggy()).build());
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("", "sons_chapter_1_outro", events));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(false).addMonster(MonsterFactory.buildTroll()).addReward(new Reward(WeaponFactory.buildSwordOfPotential())).build());
        events.add(new Event.Builder(true).addPnj(PNJFactory.buildVanark()).build());
        book.addChapter(new Chapter("", "sons_chapter_2_outro", events));

        return book;
    }

    public static Book buildBalrogQuest() {
        Book book = new Book(3, "balrog_quest", "balrog_intro", "balrog_outro", null);

        // chapter 1
        List<Event> events = new ArrayList<>();
        Pnj whiteWizard = PNJFactory.buildWhiteWizard();
        events.add(new Event.Builder(false).addPnj(whiteWizard).build());
        events.add(new Event.Builder(false).addPnj(whiteWizard).build());
        events.add(new Event.Builder(false).addPnj(whiteWizard).build());
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("balrog_chapter_1_intro", "balrog_chapter_1_outro", events));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(true).addPnj(PNJFactory.buildBalrog()).build());
        book.addChapter(new Chapter("", "balrog_chapter_2_outro", events));

        return book;
    }

    public static Book buildWizardQuest() {
        Book book = new Book(4, "wizard_quest", "wizard_intro", "", null);

        // chapter 1
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildMinsk()).build());
        events.add(new Event.Builder(false).addPnj(PNJFactory.buildMontaron()).build());
        events.add(new Event.Builder(true).build());
        book.addChapter(new Chapter("wizard_chapter_1_intro", "wizard_chapter_1_outro", events));

        // chapter 2
        events = new ArrayList<>();
        events.add(new Event.Builder(false).addReward(new Reward(WeaponFactory.buildElvenThrowingDagger())).build());
        events.add(new Event.Builder(true).addPnj(PNJFactory.buildXsar()).addMonster(MonsterFactory.buildTroll()).build());
        book.addChapter(new Chapter("wizard_chapter_2_intro", "wizard_chapter_2_outro", events));

        return book;
    }

    public static Book buildPartnershipQuest() {
        Book book = new Book(5, "partnership_quest", "initiation_intro", "initiation_outro", null);
        return book;
    }

    public static Book buildDreamQuest() {
        Book book = new Book(6, "dream_quest", "initiation_intro", "initiation_outro", null);
        return book;
    }

}
