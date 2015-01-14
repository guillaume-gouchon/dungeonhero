package com.glevel.dungeonhero.models.discussions.riddles;

/**
 * Created by guillaume on 12/2/14.
 */
public class OpenRiddle extends Riddle {

    private static final long serialVersionUID = -6622374013398749183L;

    private final String correctAnswer;

    public OpenRiddle(int timer, String question, String correctAnswer) {
        super(timer, question);
        this.correctAnswer = correctAnswer;
    }

    public boolean isAnswerCorrect(String answer) {
        return formatAnswer(correctAnswer).equals(formatAnswer(answer));
    }

    private static String formatAnswer(String answerToFormat) {
        return answerToFormat.toLowerCase().replaceAll(" ", "");
    }

}
