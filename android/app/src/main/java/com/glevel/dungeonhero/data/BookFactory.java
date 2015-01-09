package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.data.characters.MonsterFactory;
import com.glevel.dungeonhero.data.characters.PNJFactory;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Chapter;
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
        lst.add(buildValarkyBook());
        lst.add(buildBalrogQuest());
        lst.add(buildWizardQuest());
        lst.add(buildPartnershipQuest());
        lst.add(buildAleQuest());
        lst.add(buildDreamQuest());
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

        // add resources
        book.addResource(PNJFactory.buildTutorialPNJ());
        book.addResource(PNJFactory.buildInitiationQuestGirl());

        return book;
    }

    public static Book buildValarkyBook() {
        Book book = new Book(2, "valarky_quest", "valarky_intro", "initiation_outro", null);
        return book;
    }

    public static Book buildBalrogQuest() {
        Book book = new Book(3, "balrog_quest", "initiation_intro", "initiation_outro", null);
        return book;
    }

    public static Book buildWizardQuest() {
        Book book = new Book(4, "wizard_quest", "initiation_intro", "initiation_outro", null);
        return book;
    }

    public static Book buildPartnershipQuest() {
        Book book = new Book(5, "partnership_quest", "initiation_intro", "initiation_outro", null);
        return book;
    }

    public static Book buildAleQuest() {
        Book book = new Book(6, "ale_quest", "initiation_intro", "initiation_outro", null);
        return book;
    }

    public static Book buildDreamQuest() {
        Book book = new Book(7, "dream_quest", "initiation_intro", "initiation_outro", null);
        return book;
    }

}
