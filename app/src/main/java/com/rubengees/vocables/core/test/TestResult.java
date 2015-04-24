package com.rubengees.vocables.core.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben Gees on 13.02.2015.
 */
public class TestResult implements Serializable{

    private List<TestAnswer> answers;
    private int correct = 0;
    private int incorrect = 0;
    private int averageTime = 0;
    private int bestTime = Integer.MAX_VALUE;

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
}
