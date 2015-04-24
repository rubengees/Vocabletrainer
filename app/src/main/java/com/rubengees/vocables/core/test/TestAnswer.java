package com.rubengees.vocables.core.test;

import com.rubengees.vocables.pojo.Meaning;

import java.io.Serializable;

/**
 * Created by Ruben Gees on 13.02.2015.
 */
public class TestAnswer implements Serializable {

    private Meaning question;
    private Meaning answer;
    private Meaning given;
    private boolean correct;
    private int time;

    public TestAnswer(Meaning question, Meaning answer, Meaning given, boolean correct, int time) {
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
}
