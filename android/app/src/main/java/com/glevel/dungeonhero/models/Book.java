package com.glevel.dungeonhero.models;

import android.content.res.Resources;
import android.util.Log;

import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.graphics.GraphicHolder;
import com.glevel.dungeonhero.utils.billing.InAppProduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/3/14.
 */
public class Book extends StorableResource implements Serializable, InAppProduct {

    private static final long serialVersionUID = -2713633045742704862L;
    private static final String TAG = "Book";

    private final int id;
    private final String outroText;
    private String introText;
    private final List<Chapter> chapters = new ArrayList<>();
    private final List<GraphicHolder> resourcesToLoad = new ArrayList<>();
    private final String productId;
    private boolean hasBeenBought;
    private boolean done;

    public Book(int id, String identifier, String introText, String outroText, String productId) {
        super(identifier);
        this.id = id;
        this.introText = introText;
        this.outroText = outroText;
        this.productId = productId;
        this.done = false;
    }

    @Override
    public int getImage(Resources resources) {
        return StorableResource.getResource(resources, "bg_book", true);
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

    public int getIntroText(Resources resources) {
        return StorableResource.getResource(resources, introText, false);
    }

    public int getOutroText(Resources resources) {
        return StorableResource.getResource(resources, outroText, false);
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public Chapter getActiveChapter() {
        return chapters.get(0);
    }

    public boolean hasNextChapter() {
        return chapters.size() > 1;
    }

    /**
     * @return true if aventure is over
     */
    public boolean goToNextChapter() {
        Log.d(TAG, "go to next chapter");
        chapters.remove(0);
        Log.d(TAG, chapters.size() + " chapters left");
        return chapters.size() > 0;
    }

    public void read() {
        introText = "";
    }

    public void addChapter(Chapter chapter) {
        chapter.setIndex(chapters.size());
        chapters.add(chapter);
    }

    public List<GraphicHolder> getResourcesToLoad() {
        return resourcesToLoad;
    }

    public void addResource(GameElement element) {
        resourcesToLoad.add(element);
    }

}
