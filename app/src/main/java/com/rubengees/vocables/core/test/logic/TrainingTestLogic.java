package com.rubengees.vocables.core.test.logic;

import android.content.Context;
import android.os.Bundle;

import com.rubengees.vocables.core.testsettings.Direction;
import com.rubengees.vocables.core.testsettings.TrainingTestSettings;
import com.rubengees.vocables.pojo.MeaningList;
import com.rubengees.vocables.pojo.Vocable;

import java.util.Random;

/**
 * Created by Ruben on 20.07.2015.
 */
public class TrainingTestLogic extends TestLogic<TrainingTestSettings> {

    private static final String STATE_RANDOM = "random";
    private static final String STATE_SHOWING_QUESTION = "showingQuestion";

    private Random random = new Random();
    private double randomValue;
    private boolean showingQuestion;

    public TrainingTestLogic(Context context, TrainingTestSettings settings) {
        super(context, settings);
        showingQuestion = true;

        next();
    }

    public TrainingTestLogic(Context context, Bundle savedInstanceState) {
        super(context, savedInstanceState);
    }

    @Override
    public boolean next() {
        super.next();

        randomValue = random.nextDouble();
        return getPosition() < getAmount();
    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {
        super.restoreSavedInstanceState(savedInstanceState);

        randomValue = savedInstanceState.getDouble(STATE_RANDOM);
        showingQuestion = savedInstanceState.getBoolean(STATE_SHOWING_QUESTION);
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);

        outState.putDouble(STATE_RANDOM, randomValue);
        outState.putBoolean(STATE_SHOWING_QUESTION, showingQuestion);
    }

    @Override
    public String getHint() {
        Vocable current = getCurrentVocable();

        if (current != null) {
            return current.getHint();
        } else {
            return null;
        }
    }

    public void flip() {
        showingQuestion = !showingQuestion;
    }

    public boolean isShowingQuestion() {
        return showingQuestion;
    }

    public void processAnswer(boolean correct) {
        Vocable current = getCurrentVocable();
        MeaningList question = getQuestion();
        MeaningList answer = getAnswer();
        MeaningList given = correct ? answer : null;

        processAnswerDontSave(current, question, answer, given, correct);
    }

    public MeaningList getQuestion() {
        Vocable vocable = getCurrentVocable();
        Direction direction = getSettings().getDirection();

        if (vocable != null) {
            if (direction == Direction.FIRST) {
                return vocable.getFirstMeaningList();
            } else if (direction == Direction.SECOND) {
                return vocable.getSecondMeaningList();
            } else {
                if (randomValue < RANDOM_FACTOR) {
                    return vocable.getFirstMeaningList();
                } else {
                    return vocable.getSecondMeaningList();
                }
            }
        } else {
            return null;
        }
    }

    public MeaningList getAnswer() {
        Vocable vocable = getCurrentVocable();
        Direction direction = getSettings().getDirection();

        if (vocable != null) {
            if (direction == Direction.FIRST) {
                return vocable.getSecondMeaningList();
            } else if (direction == Direction.SECOND) {
                return vocable.getFirstMeaningList();
            } else {
                if (randomValue < RANDOM_FACTOR) {
                    return vocable.getSecondMeaningList();
                } else {
                    return vocable.getFirstMeaningList();
                }
            }
        } else {
            return null;
        }
    }
}
