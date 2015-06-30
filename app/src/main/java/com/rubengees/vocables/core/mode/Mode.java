package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.rubengees.vocables.core.test.Test;
import com.rubengees.vocables.core.test.TestResult;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.layout.TestSettingsLayout;

/**
 * Created by Ruben Gees on 12.02.2015.
 */
public abstract class Mode implements Parcelable {

    private ModeData data;

    public Mode(ModeData data) {
        this.data = data;
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
        out.writeParcelable(data, flags);
    }

    private void readFromParcel(Parcel in) {
        data = in.readParcelable(ModeData.class.getClassLoader());
    }

    public final int getId() {
        return data.getId();
    }

    public final int getPlayed() {
        return data.getPlayed();
    }

    public final int getCorrect() {
        return data.getCorrect();
    }

    public final int getIncorrect() {
        return data.getIncorrect();
    }

    public int getPerfectInRow() {
        return data.getPerfectInRow();
    }

    public final int getBestTime() {
        return data.getBestTime();
    }

    public final int getAverageTime() {
        return data.getAverageTime();
    }

    public final void processResult(TestResult result) {
        data.setPlayed(data.getPlayed() + 1);
        data.setCorrect(data.getCorrect() + result.getCorrect());
        data.setIncorrect(data.getIncorrect() + result.getIncorrect());

        if (result.getCorrect() >= getMinAmount() && result.getIncorrect() <= 0) {
            data.setPerfectInRow(data.getPerfectInRow() + 1);
        }

        int averageTime = result.getAverageTime();

        if (data.getBestTime() <= 0 || averageTime < data.getBestTime()) {
            data.setBestTime(averageTime);
        }

        int played = data.getPlayed();

        data.setAverageTime((data.getAverageTime() * (played - 1) + averageTime) / played);
    }

    public abstract int getColor(Context context);

    public abstract int getDarkColor(Context context);

    public abstract int getMinAmount();

    public abstract String getHelpText(Context context);

    public abstract String getTitle(Context context);

    public abstract String getShortTitle(Context context);

    public abstract Drawable getIcon(Context context);

    public abstract TestSettingsLayout getTestSettingsLayout(Context context, TestSettingsLayout.OnTestSettingsListener listener);

    public abstract Test getTest(Context context, TestSettings settings, Test.OnTestFinishedListener listener, Bundle savedInstanceState);

    public abstract Test getTest(Context context, TestSettings settings, Test.OnTestFinishedListener listener);

    public void reset() {
        data.reset();
    }
}
