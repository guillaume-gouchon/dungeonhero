package com.glevel.dungeonhero.models;

import android.content.res.Resources;
import android.util.Log;

import com.glevel.dungeonhero.data.BookFactory;
import com.glevel.dungeonhero.data.characters.PNJFactory;
import com.glevel.dungeonhero.game.graphics.GraphicHolder;
import com.glevel.dungeonhero.models.dungeons.Event;
import com.glevel.dungeonhero.utils.billing.InAppProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/3/14.
 */
public class Book extends StorableResource implements InAppProduct {

    private static final String TAG = "Book";

    private final int id;
    private final int level;
    private final String outroText;
    private final List<Chapter> chapters = new ArrayList<>();
    private final List<GraphicHolder> resourcesToLoad = new ArrayList<>();
    private final String productId;
    private String introText;
    private boolean hasBeenBought;
    private int bestScore;
    private int currentScore;

    public Book(int id, String identifier, String introText, String outroText, String productId, int level) {
        super(identifier);
        this.id = id;
        this.introText = introText;
        this.outroText = outroText;
        this.productId = productId;
        this.bestScore = 0;
        this.currentScore = 3;
        this.level = level;
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
        return bestScore > 0;
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

        if (id == BookFactory.TUTORIAL_BOOK_ID) {
            resourcesToLoad.add(PNJFactory.buildTutorialPNJ());
        }

        // add resources
        for (Event event : chapter.getEvents()) {
            resourcesToLoad.addAll(event.getMonsters());
            resourcesToLoad.addAll(event.getPnjs());
        }
    }

    public List<GraphicHolder> getResourcesToLoad() {
        return resourcesToLoad;
    }

    public boolean isTutorialTime() {
        return id == BookFactory.TUTORIAL_BOOK_ID && getActiveChapter().isFirst() && !isDone();
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int score) {
        this.bestScore = score;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void updateScore(int delta) {
        currentScore = Math.min(3, Math.max(1, currentScore + delta));
        Log.d(TAG, "updating score to " + currentScore);
    }

    public int getLevel() {
        return level;
    }
}
