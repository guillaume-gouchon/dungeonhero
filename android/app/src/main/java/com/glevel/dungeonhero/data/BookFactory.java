package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.Chapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/6/14.
 */
public class BookFactory {

    public static List<Book> getBooks() {
        List<Book> lstBooks = new ArrayList<Book>();
        lstBooks.add(buildInitiationBook(1 + lstBooks.size()));
        lstBooks.add(buildInitiationBook(1 + lstBooks.size()));
        return lstBooks;
    }

    public static Book buildInitiationBook(int bookId) {
        List<Chapter> chapters = new ArrayList<Chapter>();
        chapters.add(new Chapter(0, 0, 0, null));
        return new Book(bookId, R.string.tutorial, R.drawable.bg_book, 0, 0, chapters, null);
    }

}
