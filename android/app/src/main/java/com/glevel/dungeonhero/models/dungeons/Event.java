package com.glevel.dungeonhero.models.dungeons;

import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.models.characters.Pnj;
import com.glevel.dungeonhero.models.characters.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume on 12/5/14.
 */
public class Event implements Serializable {

    private static final long serialVersionUID = 5021907596161916969L;

    private final List<Pnj> pnjs;
    private final List<Reward> rewards;
    private final List<Unit> monsters;
    private final boolean isDungeonOver;

    public Event(Builder builder) {
        pnjs = builder.pnjs;
        rewards = builder.rewards;
        monsters = builder.monsters;
        isDungeonOver = builder.isDungeonOver;
    }

    public boolean isDungeonOver() {
        return isDungeonOver;
    }

    public List<Pnj> getPnjs() {
        return pnjs;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public List<Unit> getMonsters() {
        return monsters;
    }

    public static class Builder {

        private List<Pnj> pnjs = new ArrayList<>();
        private List<Reward> rewards = new ArrayList<>();
        private List<Unit> monsters = new ArrayList<>();
        private boolean isDungeonOver;

        public Builder(boolean isDungeonOver) {
            this.isDungeonOver = isDungeonOver;
        }

        public Builder addPnj(Pnj pnj) {
            pnjs.add(pnj);
            return this;
        }

        public Builder addReward(Reward reward) {
            rewards.add(reward);
            return this;
        }

        public Builder addMonster(Monster monster) {
            monsters.add(monster);
            return this;
        }

        public Event build() {
            return new Event(this);
        }

    }

}
