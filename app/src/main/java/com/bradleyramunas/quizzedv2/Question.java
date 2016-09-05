package com.bradleyramunas.quizzedv2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bradley on 8/4/2016.
 */
public class Question implements Parcelable {

    protected Question(Parcel in) {

    }

    public Question() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
