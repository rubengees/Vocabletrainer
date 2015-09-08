package com.rubengees.vocables.core.test;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rubengees.vocables.pojo.MeaningList;

/**
 * Created by Ruben Gees on 13.02.2015.
 */
public class TestAnswer implements Parcelable {

    public static final Parcelable.Creator<TestAnswer> CREATOR = new Parcelable.Creator<TestAnswer>() {
        public TestAnswer createFromParcel(Parcel source) {
            return new TestAnswer(source);
        }

        public TestAnswer[] newArray(int size) {
            return new TestAnswer[size];
        }
    };
    private MeaningList question;
    private MeaningList answer;
    private MeaningList given;
    private boolean correct;
    private int time;

    public TestAnswer(@NonNull MeaningList question, @NonNull MeaningList answer, @Nullable MeaningList given, boolean correct, int time) {
        this.question = question;
        this.answer = answer;
        this.given = given;
        this.correct = correct;
        this.time = time;
    }

    protected TestAnswer(Parcel in) {
        this.question = in.readParcelable(MeaningList.class.getClassLoader());
        this.answer = in.readParcelable(MeaningList.class.getClassLoader());
        this.given = in.readParcelable(MeaningList.class.getClassLoader());
        this.correct = in.readByte() != 0;
        this.time = in.readInt();
    }

    public MeaningList getQuestion() {
        return question;
    }

    public MeaningList getAnswer() {
        return answer;
    }

    public MeaningList getGiven() {
        return given;
    }

    public boolean isCorrect() {
        return correct;
    }

    public int getTime() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.question, 0);
        dest.writeParcelable(this.answer, 0);
        dest.writeParcelable(this.given, 0);
        dest.writeByte(correct ? (byte) 1 : (byte) 0);
        dest.writeInt(this.time);
    }
}
