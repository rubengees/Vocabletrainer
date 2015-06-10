package com.rubengees.vocables.core.test.logic;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.rubengees.vocables.core.testsettings.Direction;
import com.rubengees.vocables.core.testsettings.TimeTestSettings;
import com.rubengees.vocables.pojo.Meaning;
import com.rubengees.vocables.pojo.Vocable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ruben on 06.04.2015.
 */
public class TimeTestLogic extends TestLogic<TimeTestSettings> {

    private static final String STATE_RANDOM = "random";
    private static final String STATE_FIELD = "field";
    private static final String STATE_MAX_TIME = "maxTime";
    private static final String STATE_TIME_REMAINING = "timeRemaining";

    private double random;
    private MeaningField field;
    private long maxTime;
    private long timeRemaining;
    private Timer timer;

    private OnTimeListener listener;

    public TimeTestLogic(Context context, TimeTestSettings settings, int sizeX, int sizeY, OnTimeListener listener) {
        super(context, settings);
        this.field = new MeaningField(sizeX, sizeY);
        this.listener = listener;

        this.timeRemaining = getAmount() * 3 * 1000;
        this.maxTime = timeRemaining;
        next();
    }

    public TimeTestLogic(Context context, Bundle savedInstanceState, OnTimeListener listener) {
        super(context, savedInstanceState);
        this.listener = listener;
    }

    private void setTimer() {
        timer = new Timer(timeRemaining, 100);

        timer.start();
    }

    @Override
    public boolean next() {
        super.next();

        random = Math.random();
        field.setCells(getCells());

        return true;
    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {
        super.restoreSavedInstanceState(savedInstanceState);
        random = savedInstanceState.getDouble(STATE_RANDOM);
        field = savedInstanceState.getParcelable(STATE_FIELD);
        maxTime = savedInstanceState.getLong(STATE_MAX_TIME);
        timeRemaining = savedInstanceState.getLong(STATE_TIME_REMAINING);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTimer();
    }

    @Override
    public void onPause() {
        cancelTimer();
        super.onPause();
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);
        outState.putDouble(STATE_RANDOM, random);
        outState.putParcelable(STATE_FIELD, field);
        outState.putLong(STATE_MAX_TIME, maxTime);
        outState.putLong(STATE_TIME_REMAINING, timeRemaining);
    }

    private void cancelTimer() {
        timer.cancel();
    }

    private List<MeaningCell> getCells() {
        List<MeaningCell> result = new ArrayList<>();
        List<Vocable> help = new ArrayList<>(getAmount() - 1);
        Random random = new Random();
        int pos = getAdjustedPosition();

        help.addAll(getSubList(0, pos));
        if (pos + 1 <= getAmount()) {
            help.addAll(getSubList(pos + 1, getAmount()));
        }

        for (int i = 0; i < 9; i++) {
            int index = random.nextInt(help.size());
            Vocable vocable = help.get(index);

            result.add(new MeaningCell(vocable, getAnswerMeaning(vocable)));
            help.remove(index);
        }

        result.add(random.nextInt(10), new MeaningCell(getCurrentVocable(), getAnswerMeaning(getCurrentVocable())));

        return result;
    }

    private Meaning getAnswerMeaning(Vocable vocable) {
        Direction direction = getSettings().getDirection();

        if (direction == Direction.FIRST) {
            return vocable.getSecondMeaning();
        } else if (direction == Direction.SECOND) {
            return vocable.getFirstMeaning();
        } else {
            if (random < 0.5) {
                return vocable.getSecondMeaning();
            } else {
                return vocable.getFirstMeaning();
            }
        }
    }

    public Position processAnswer(Position pos) {
        Vocable current = getCurrentVocable();
        Meaning question = getQuestion();
        Meaning answer = current.getOtherMeaning(question);
        Meaning given = field.getCell(pos).getMeaning();

        boolean correct = answer.equals(given);

        processAnswer(current, question, answer, given, correct);

        if (correct) {
            return null;
        } else {
            return field.findCellPosition(answer);
        }
    }

    public Meaning getQuestion() {
        Vocable vocable = getCurrentVocable();
        Direction direction = getSettings().getDirection();

        if (direction == Direction.FIRST) {
            return vocable.getFirstMeaning();
        } else if (direction == Direction.SECOND) {
            return vocable.getSecondMeaning();
        } else {
            if (random < 0.5) {
                return vocable.getFirstMeaning();
            } else {
                return vocable.getSecondMeaning();
            }
        }
    }

    public MeaningField getField() {
        return field;
    }

    public interface OnTimeListener {
        void onTimeOver();

        void onTimeUpdate(long max, long remaining);
    }

    private class Timer extends CountDownTimer {

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long timeRemaining) {
            TimeTestLogic.this.timeRemaining = timeRemaining;

            listener.onTimeUpdate(maxTime, timeRemaining);
        }

        @Override
        public void onFinish() {
            listener.onTimeOver();
        }
    }
}
