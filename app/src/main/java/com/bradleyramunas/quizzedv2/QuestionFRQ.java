package com.bradleyramunas.quizzedv2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bradley on 8/4/2016.
 */
public class QuestionFRQ extends Question implements Parcelable {

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

    protected QuestionFRQ(Parcel in) {
        _questionText = in.readString();
        _answerText = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_questionText);
        dest.writeString(_answerText);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<QuestionFRQ> CREATOR = new Parcelable.Creator<QuestionFRQ>() {
        @Override
        public QuestionFRQ createFromParcel(Parcel in) {
            return new QuestionFRQ(in);
        }

        @Override
        public QuestionFRQ[] newArray(int size) {
            return new QuestionFRQ[size];
        }
    };

    @Override
    public String toString(){
        return "QuestionFRQ, q is " + _questionText + ", answer is " + _answerText;
    }
}