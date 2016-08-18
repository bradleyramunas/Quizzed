package com.bradleyramunas.quizzedv2;

/**
 * Created by Bradley on 8/15/2016.
 */
public interface QuestionType {

    public String getQuestionText();

    public String getAnswerText();

    public boolean checkAnswer();
}
