package com.bradleyramunas.quizzedv2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bradley on 8/4/2016.
 */
public class QuestionMCQ extends Question implements Parcelable {

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

    protected QuestionMCQ(Parcel in) {
        _questionText = in.readString();
        _optionOne = in.readString();
        _optionTwo = in.readString();
        _optionThree = in.readString();
        _optionFour = in.readString();
        _answerText = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_questionText);
        dest.writeString(_optionOne);
        dest.writeString(_optionTwo);
        dest.writeString(_optionThree);
        dest.writeString(_optionFour);
        dest.writeString(_answerText);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<QuestionMCQ> CREATOR = new Parcelable.Creator<QuestionMCQ>() {
        @Override
        public QuestionMCQ createFromParcel(Parcel in) {
            return new QuestionMCQ(in);
        }

        @Override
        public QuestionMCQ[] newArray(int size) {
            return new QuestionMCQ[size];
        }
    };

    @Override
    public String toString(){
        return "QuestionMCQ, q is " + _questionText + ", answer is " + _answerText;
    }
}