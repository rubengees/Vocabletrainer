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

        public TestAnswer createFromParcel(Parcel in) {
            return new TestAnswer(in);
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

    private TestAnswer(Parcel in) {
        readFromParcel(in);
    }

    public TestAnswer(@NonNull MeaningList question, @NonNull MeaningList answer, @Nullable MeaningList given, boolean correct, int time) {
        this.question = question;
        this.answer = answer;
        this.given = given;
        this.correct = correct;
        this.time = time;
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

    private void readFromParcel(Parcel in) {
        question = in.readParcelable(MeaningList.class.getClassLoader());
        answer = in.readParcelable(MeaningList.class.getClassLoader());
        given = in.readParcelable(MeaningList.class.getClassLoader());
        correct = in.readInt() == 1;
        time = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(question, flags);
        out.writeParcelable(answer, flags);
        out.writeParcelable(given, flags);
        out.writeInt(correct ? 1 : 0);
        out.writeInt(time);
    }
}
