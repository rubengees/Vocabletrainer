package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.rubengees.vocables.core.test.TestResult;

import java.io.Serializable;

/**
 * Created by Ruben Gees on 12.02.2015.
 */
public abstract class Mode implements Serializable {

    private int played;
    private int correct;
    private int incorrect;
    private int bestTime;
    private int averageTime;

    protected Mode(int played, int correct, int incorrect, int bestTime, int averageTime) {
        this.played = played;
        this.correct = correct;
        this.incorrect = incorrect;
        this.bestTime = bestTime;
        this.averageTime = averageTime;
    }

    public final int getPlayed() {
        return played;
    }

    public final int getCorrect() {
        return correct;
    }

    public final int getIncorrect() {
        return incorrect;
    }

    public final int getBestTime() {
        return bestTime;
    }

    public final int getAverageTime() {
        return averageTime;
    }

    public final void processResult(TestResult result) {
        played++;
        correct += result.getCorrect();
        incorrect += result.getIncorrect();
        int avrgTime = result.getAverageTime();
        if (avrgTime < bestTime) {
            bestTime = avrgTime;
        }
        averageTime = (averageTime * (played - 1) + avrgTime) / played;
    }

    public abstract int getColor(Context context);

    public abstract int getDarkColor(Context context);

    public abstract int getMinAmount();

    public abstract String getHelpText(Context context);

    public abstract String getTitle(Context context);

    public abstract String getShortTitle(Context context);

    public abstract Drawable getIcon(Context context);
}
