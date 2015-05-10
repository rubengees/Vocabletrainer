package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.rubengees.vocables.core.test.Test;
import com.rubengees.vocables.core.test.TestResult;
import com.rubengees.vocables.core.testsettings.layout.TestSettingsLayout;

/**
 * Created by Ruben Gees on 12.02.2015.
 */
public abstract class Mode implements Parcelable {

    private int played;
    private int correct;
    private int incorrect;
    private int perfectInRow = 0;
    private int bestTime;
    private int averageTime;

    protected Mode(int played, int correct, int incorrect, int perfectInRow, int bestTime, int averageTime) {
        this.played = played;
        this.correct = correct;
        this.incorrect = incorrect;
        this.perfectInRow = perfectInRow;
        this.bestTime = bestTime;
        this.averageTime = averageTime;
    }

    protected Mode(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(played);
        out.writeInt(correct);
        out.writeInt(incorrect);
        out.writeInt(perfectInRow);
        out.writeInt(bestTime);
        out.writeInt(averageTime);
    }

    private void readFromParcel(Parcel in) {
        played = in.readInt();
        correct = in.readInt();
        incorrect = in.readInt();
        perfectInRow = in.readInt();
        bestTime = in.readInt();
        averageTime = in.readInt();
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

    public int getPerfectInRow() {
        return perfectInRow;
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
        if (result.getCorrect() >= getMinAmount() && result.getIncorrect() <= 0) {
            perfectInRow++;
        }
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

    public abstract TestSettingsLayout getTestSettingsLayout(Context context, TestSettingsLayout.OnTestSettingsListener listener);

    public abstract Test getTest();
}
