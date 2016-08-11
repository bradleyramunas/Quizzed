package com.bradleyramunas.quizzedv2;

/**
 * Created by Bradley on 8/4/2016.
 */
public class QuestionMCQ extends Question{

    private String _questionText;
    private String _optionOne;
    private String _optionTwo;
    private String _optionThree;
    private String _optionFour;
    private String _answerText;


    public QuestionMCQ() {
    }

    public QuestionMCQ(String _questionText, String _optionOne, String _optionTwo, String _optionThree, String _optionFour, String _answerText) {
        this._questionText = _questionText;
        this._optionOne = _optionOne;
        this._optionTwo = _optionTwo;
        this._optionThree = _optionThree;
        this._optionFour = _optionFour;
        this._answerText = _answerText;
    }

    public String get_questionText() {
        return _questionText;
    }

    public void set_questionText(String _questionText) {
        this._questionText = _questionText;
    }

    public String get_optionOne() {
        return _optionOne;
    }

    public void set_optionOne(String _optionOne) {
        this._optionOne = _optionOne;
    }

    public String get_optionTwo() {
        return _optionTwo;
    }

    public void set_optionTwo(String _optionTwo) {
        this._optionTwo = _optionTwo;
    }

    public String get_optionThree() {
        return _optionThree;
    }

    public void set_optionThree(String _optionThree) {
        this._optionThree = _optionThree;
    }

    public String get_optionFour() {
        return _optionFour;
    }

    public void set_optionFour(String _optionFour) {
        this._optionFour = _optionFour;
    }

    public String get_answerText() {
        return _answerText;
    }

    public void set_answerText(String _answerText) {
        this._answerText = _answerText;
    }
}
