package com.glevel.dungeonhero.models;

import com.glevel.dungeonhero.utils.billing.InAppProduct;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guillaume ON 10/3/14.
 */
public class Book implements Serializable, InAppProduct {

    private static final long serialVersionUID = -2713633045742704862L;

    private final int bookId;
    private final int name, image;
    private final int introText, outroText;
    private transient final List<Chapter> chapters;
    private final String productId;
    private boolean hasBeenBought;
    private boolean done;

    public Book(int bookId, int name, int image, int introText, int outroText, List<Chapter> chapters, String productId) {
        this.bookId = bookId;
        this.name = name;
        this.image = image;
        this.introText = introText;
        this.outroText = outroText;
        this.chapters = chapters;
        this.productId = productId;
        this.done = false;
    }

    @Override
    public String getProductId() {
        return productId;
    }

    @Override
    public void setHasBeenBought(boolean hasBeenBought) {
        this.hasBeenBought = hasBeenBought;
    }

    @Override
    public boolean isAvailable() {
        return isFree() || hasBeenBought;
    }

    @Override
    public boolean isFree() {
        return productId == null;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public int getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getIntroText() {
        return introText;
    }

    public int getOutroText() {
        return outroText;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getBookId() {
        return bookId;
    }

}
