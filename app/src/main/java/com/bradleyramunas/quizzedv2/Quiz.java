package com.bradleyramunas.quizzedv2;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Bradley on 8/4/2016.
 */
public class Quiz implements Parcelable {

    private ArrayList<Question> questionList;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Quiz(String name) {
        this.name = name;
        questionList = new ArrayList<>();
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    public void addQuestion(Question q){
        questionList.add(q);
    }

    public boolean removeQuestion(Question q){
        if(questionList.contains(q)){
            questionList.remove(q);
            return true;
        }
        return false;
    }


    protected Quiz(Parcel in) {
        if (in.readByte() == 0x01) {
            questionList = new ArrayList<Question>();
            in.readList(questionList, Question.class.getClassLoader());
        } else {
            questionList = null;
        }
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (questionList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(questionList);
        }
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Quiz> CREATOR = new Parcelable.Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };
}
