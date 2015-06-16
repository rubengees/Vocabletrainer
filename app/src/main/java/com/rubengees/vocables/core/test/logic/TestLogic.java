package com.rubengees.vocables.core.test.logic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.test.TestAnswer;
import com.rubengees.vocables.core.test.TestResult;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.pojo.Meaning;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by ruben on 28.04.15.
 */

public abstract class TestLogic<E extends TestSettings> {

    private static final String STATE_VOCABLES = "vocables";
    private static final String STATE_SETTINGS = "settings";
    private static final String STATE_POSITION = "position";
    private static final String STATE_CURRENT_TIME = "currentTime";
    private static final String STATE_RESULT = "result";

    private Context context;
    private ArrayList<Vocable> vocables;
    private int position = -1;
    private long currentTime;
    private E settings;
    private TestResult result;

    public TestLogic(final Context context, E settings) {
        this.context = context;
        this.settings = settings;
        this.vocables = new ArrayList<>();
        this.result = new TestResult();

        Map<Integer, Unit> units = Core.getInstance((Activity) context).getVocableManager().getUnitMap();

        for (Integer unitId : settings.getUnitIds()) {
            vocables.addAll(units.get(unitId).getVocables(settings.getMaxRate()));
        }

        Collections.shuffle(vocables);
    }

    public TestLogic(Context context, Bundle savedInstanceState) {
        this.context = context;

        restoreSavedInstanceState(savedInstanceState);
    }

    protected void restoreSavedInstanceState(final Bundle savedInstanceState) {
        vocables = savedInstanceState.getParcelableArrayList(STATE_VOCABLES);
        settings = savedInstanceState.getParcelable(STATE_SETTINGS);
        position = savedInstanceState.getInt(STATE_POSITION);
        currentTime = savedInstanceState.getLong(STATE_CURRENT_TIME);
        result = savedInstanceState.getParcelable(STATE_RESULT);
    }

    public void onPause() {

    }

    public void onResume() {

    }

    public boolean next() {
        position++;
        currentTime = System.currentTimeMillis();

        return true;
    }

    public final Context getContext() {
        return context;
    }

    public final int getPosition() {
        return position;
    }

    public final Vocable getCurrentVocable() {
        return vocables.get(getAdjustedPosition());
    }

    public final int getAdjustedPosition() {
        int currentPos = position;
        int maxIndices = getAmount() - 1;

        if (currentPos > maxIndices) {
            currentPos = currentPos / maxIndices;
            return position - (currentPos * maxIndices);
        } else {
            return currentPos;
        }
    }

    public final int getAmount() {
        return vocables.size();
    }

    public final Vocable getVocableAtPos(int position) {
        return vocables.get(position);
    }

    public final List<Vocable> getSubList(int start, int end) {
        return vocables.subList(start, end);
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public ArrayList<Vocable> getVocables() {
        return vocables;
    }

    public E getSettings() {
        return settings;
    }

    final void processAnswer(Vocable vocable, Meaning question, Meaning answer, Meaning given, boolean correct) {
        int time = (int) (System.currentTimeMillis() - currentTime);
        TestAnswer testAnswer = new TestAnswer(question, answer, given, correct, time);

        result.addAnswer(testAnswer);
        vocable.processAnswer(correct);
    }

    public void saveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_VOCABLES, vocables);
        outState.putParcelable(STATE_SETTINGS, settings);
        outState.putInt(STATE_POSITION, position);
        outState.putLong(STATE_CURRENT_TIME, currentTime);
        outState.putParcelable(STATE_RESULT, result);
    }

    public final TestResult getResult() {
        return result;
    }

    public abstract String getHint();
}
