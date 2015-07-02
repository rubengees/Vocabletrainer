package com.rubengees.vocables.core.test.logic;


import android.content.Context;
import android.os.Bundle;

import com.rubengees.vocables.core.testsettings.ClassicTestSettings;
import com.rubengees.vocables.core.testsettings.Direction;
import com.rubengees.vocables.pojo.MeaningList;
import com.rubengees.vocables.pojo.Vocable;

import java.util.Random;

/**
 * Created by Ruben on 04.04.2015.
 */
public class ClassicTestLogic extends TestLogic<ClassicTestSettings> {

    private static final String STATE_RANDOM = "random";

    private Random random = new Random();
    private double randomValue;

    public ClassicTestLogic(Context context, ClassicTestSettings settings) {
        super(context, settings);

        next();
    }

    public ClassicTestLogic(Context context, Bundle savedInstanceState) {
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
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);

        outState.putDouble(STATE_RANDOM, randomValue);
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

    public MeaningList processAnswer(String given) {
        Vocable current = getCurrentVocable();
        MeaningList question = getQuestion();
        MeaningList answer = getAnswer();
        boolean correct = given != null && getSettings().isCaseSensitive() ? answer.contains(given) : answer.containsIgnoreCase(given);

        processAnswer(current, question, answer, given == null ? null : new MeaningList(given), correct);

        if (correct) {
            return null;
        } else {
            return answer;
        }
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
                if (randomValue < 0.5) {
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
                if (randomValue < 0.5) {
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
