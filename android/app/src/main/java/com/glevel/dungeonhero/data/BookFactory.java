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

    public static List<Book> getAll() {
        List<Book> lst = new ArrayList<>();
        lst.add(buildInitiationBook());
        lst.add(buildInitiationBook());
        return lst;
    }

    public static Book buildInitiationBook() {
        Book book = new Book(Book.TUTORIAL_BOOK_ID, "initiation", "initiation_intro", "initiation_outro", null);

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

}
