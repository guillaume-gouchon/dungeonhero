package com.glevel.dungeonhero.models.riddles;

import com.glevel.dungeonhero.models.Reward;

import java.io.Serializable;

/**
 * Created by guillaume on 12/2/14.
 */
public class OpenRiddle extends Riddle implements Serializable {

    private static final long serialVersionUID = 7852692444871638271L;
    private final String correctAnswer;

    public OpenRiddle(int timer, int question, String correctAnswer, Reward reward) {
        super(timer, question, reward);
        this.correctAnswer = correctAnswer;
    }

    public boolean isAnswerCorrect(String answer) {
        return formatAnswer(correctAnswer).equals(formatAnswer(answer));
    }

    private static String formatAnswer(String answerToFormat) {
        return answerToFormat.toLowerCase().replaceAll(" ", "");
    }

}
