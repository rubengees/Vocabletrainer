package com.rubengees.vocables.core.test;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben Gees on 13.02.2015.
 */
public class TestResult implements Parcelable {

    public static final Parcelable.Creator<TestResult> CREATOR = new Parcelable.Creator<TestResult>() {

        public TestResult createFromParcel(Parcel in) {
            return new TestResult(in);
        }

        public TestResult[] newArray(int size) {
            return new TestResult[size];
        }

    };
    private List<TestAnswer> answers;
    private int correct = 0;
    private int incorrect = 0;
    private int averageTime = 0;
    private int bestTime = Integer.MAX_VALUE;

    private TestResult(Parcel in) {
        readFromParcel(in);
    }

    public TestResult() {
        answers = new ArrayList<>();
    }

    public void addAnswer(TestAnswer answer) {
        int time = answer.getTime();

        answers.add(answer);
        if (answer.isCorrect()) {
            correct++;
        } else {
            incorrect++;
        }
        if (time < bestTime) {
            bestTime = time;
        }
        averageTime = (averageTime * (answers.size() - 1) + time) / answers.size();
    }

    public List<TestAnswer> getAnswers() {
        return answers;
    }

    public int getCorrect() {
        return correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public int getAverageTime() {
        return averageTime;
    }

    public int getBestTime() {
        return bestTime;
    }

    private void readFromParcel(Parcel in) {
        answers = new ArrayList<>();
        in.readList(answers, TestAnswer.class.getClassLoader());
        correct = in.readInt();
        incorrect = in.readInt();
        averageTime = in.readInt();
        bestTime = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(answers);
        out.writeInt(correct);
        out.writeInt(incorrect);
        out.writeInt(averageTime);
        out.writeInt(bestTime);
    }

    public TestAnswer getAnswerAt(int position) {
        return answers.get(position);
    }
}
