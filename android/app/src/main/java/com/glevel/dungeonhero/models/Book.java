package com.glevel.dungeonhero.models;

import android.content.res.Resources;
import android.util.Log;

import com.glevel.dungeonhero.activities.games.GameActivity;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.game.graphics.GraphicHolder;
import com.glevel.dungeonhero.models.dungeons.Event;
import com.glevel.dungeonhero.utils.billing.InAppProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/3/14.
 */
public class Book extends StorableResource implements InAppProduct {

    private static final long serialVersionUID = -4662020495531736056L;

    private static final String TAG = "Book";

    private final int id;
    private final int level;
    private final String outroText;
    private final List<Chapter> chapters = new ArrayList<>();
    private final List<GraphicHolder> resourcesToLoad = new ArrayList<>();
    private final String productId;
    private final Class activityClass;
    private String introText;
    private boolean hasBeenBought;
    private int bestScore;
    private int currentScore;

    private Book(Builder builder) {
        super(builder.identifier);
        this.id = builder.id;
        this.introText = builder.intro;
        this.outroText = builder.outro;
        this.productId = builder.productId;
        this.level = builder.level;
        this.activityClass = builder.activityClass;
        this.bestScore = 0;
        this.currentScore = GameConstants.MAXIMAL_STARS_RATING;
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

        // add special resources
        for (Event event : chapter.getEvents()) {
            resourcesToLoad.addAll(event.getMonsters());
            resourcesToLoad.addAll(event.getPnjs());
        }
    }

    public List<GraphicHolder> getResourcesToLoad() {
        return resourcesToLoad;
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
        currentScore = Math.min(GameConstants.MAXIMAL_STARS_RATING, Math.max(1, currentScore + delta));
        Log.d(TAG, "updating score to " + currentScore);
    }

    public int getLevel() {
        return level;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public static class Builder {

        private final int id;
        private final String identifier;
        private final int level;
        private Class activityClass;
        private String intro;
        private String outro;
        private String productId;

        public Builder(int id, String identifier, int level) {
            this.id = id;
            this.identifier = identifier;
            this.level = level;
            this.productId = null;
            this.intro = "";
            this.outro = "";
            this.activityClass = GameActivity.class;
        }

        public Builder setIntro(String intro) {
            this.intro = intro;
            return this;
        }

        public Builder setOutro(String outro) {
            this.outro = outro;
            return this;
        }

        public Builder setProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder setActivityClass(Class activityClass) {
            this.activityClass = activityClass;
            return this;
        }

        public Book build() {
            return new Book(this);
        }

    }

}
