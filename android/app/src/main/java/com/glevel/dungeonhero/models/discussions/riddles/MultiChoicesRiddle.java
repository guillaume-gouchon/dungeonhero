package com.glevel.dungeonhero.models.discussions.riddles;

import com.glevel.dungeonhero.models.Reward;

import java.util.List;

/**
 * Created by guillaume on 12/2/14.
 */
public class MultiChoicesRiddle extends Riddle {

    private List<Integer> answers;
    private int correctAnswerIndex;

    public MultiChoicesRiddle(int timer, String question, List<Integer> answers, int correctAnswerIndex, Reward reward) {
        super(timer, question, reward);
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public boolean isAnswerCorrect(int answer) {
        return correctAnswerIndex == answer;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

}
