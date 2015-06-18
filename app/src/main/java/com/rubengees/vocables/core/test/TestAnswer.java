package com.rubengees.vocables.core.test;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rubengees.vocables.pojo.Meaning;

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
    private Meaning question;
    private Meaning answer;
    private Meaning given;
    private boolean correct;
    private int time;

    private TestAnswer(Parcel in) {
        readFromParcel(in);
    }

    public TestAnswer(@NonNull Meaning question, @NonNull Meaning answer, @Nullable Meaning given, boolean correct, int time) {
        this.question = question;
        this.answer = answer;
        this.given = given;
        this.correct = correct;
        this.time = time;
    }

    public Meaning getQuestion() {
        return question;
    }

    public Meaning getAnswer() {
        return answer;
    }

    public Meaning getGiven() {
        return given;
    }

    public boolean isCorrect() {
        return correct;
    }

    public int getTime() {
        return time;
    }

    private void readFromParcel(Parcel in) {
        question = in.readParcelable(Meaning.class.getClassLoader());
        answer = in.readParcelable(Meaning.class.getClassLoader());
        given = in.readParcelable(Meaning.class.getClassLoader());
        correct = in.readInt() == 1;
        time = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(question, 0);
        out.writeParcelable(answer, 0);
        out.writeParcelable(given, 0);
        out.writeInt(correct ? 1 : 0);
        out.writeInt(time);
    }
}
