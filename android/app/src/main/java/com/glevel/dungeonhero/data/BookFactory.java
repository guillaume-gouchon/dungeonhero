package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
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
        lst.add(buildInitiationBook(1 + lst.size()));
        lst.add(buildInitiationBook(1 + lst.size()));
        return lst;
    }

    public static Book buildInitiationBook(int bookId) {
        List<Chapter> chapters = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        events.add(new Event.Builder(true).addPnj(PNJFactory.buildPNJ()).build());
        chapters.add(new Chapter(R.string.about_title, R.string.about_title, events));
        return new Book(bookId, R.string.tutorial, R.drawable.bg_book, R.string.about_title, R.string.about_title, chapters, null);
    }

}
