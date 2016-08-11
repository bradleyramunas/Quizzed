package com.bradleyramunas.quizzedv2;

/**
 * Created by Bradley on 8/4/2016.
 */
public class QuestionFRQ extends Question{

    private String _questionText;
    private String _answerText;

    public QuestionFRQ() {
    }

    public QuestionFRQ(String _questionText, String _answerText) {
        this._questionText = _questionText;
        this._answerText = _answerText;
    }

    public String get_questionText() {
        return _questionText;
    }

    public void set_questionText(String _questionText) {
        this._questionText = _questionText;
    }

    public String get_answerText() {
        return _answerText;
    }

    public void set_answerText(String _answerText) {
        this._answerText = _answerText;
    }
}
