package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.rubengees.vocables.core.test.Test;
import com.rubengees.vocables.core.test.TestResult;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.layout.TestSettingsLayout;

/**
 * An Object representing a Mode.
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

    /**
     * Returns the ID of this Mode
     *
     * @return The ID
     */
    public final int getId() {
        return data.getId();
    }

    /**
     * Returns how often this Mode was played.
     *
     * @return The amount
     */
    public final int getPlayed() {
        return data.getPlayed();
    }

    /**
     * Returns how many correct Answers were given.
     *
     * @return The amount of correct Answer
     */
    public final int getCorrect() {
        return data.getCorrect();
    }

    /**
     * Returns how many incorrect Answers were given.
     *
     * @return The amount of incorrect Answer
     */
    public final int getIncorrect() {
        return data.getIncorrect();
    }

    /**
     * Returns how long the current streak of perfect Rounds is (Only correct Answers {@see TestResultFragment}).
     *
     * @return The current streak
     */
    public int getPerfectInRow() {
        return data.getPerfectInRow();
    }

    /**
     * Returns the best time this mode was ever completed in per Vocable.
     *
     * @return The time
     */
    public final int getBestTime() {
        return data.getBestTime();
    }

    /**
     * Returns the average time this mode was completed in per Vocable.
     *
     * @return The time
     */
    public final int getAverageTime() {
        return data.getAverageTime();
    }

    /**
     * Processes a {@link TestResult} and updates the fields according to that.
     *
     * @param result The result
     */
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

    /**
     * Returns the Color of this Mode.
     *
     * @param context The current Context
     * @return The color
     */
    public abstract int getColor(Context context);

    /**
     * Returns a darker version of the Color of this Mode.
     *
     * @param context The current Context
     * @return The color
     */
    public abstract int getDarkColor(Context context);

    /**
     * Returns the minimum amount of Vocables required to play this Mode.
     *
     * @return The amount
     */
    public abstract int getMinAmount();

    /**
     * Returns the Help-text of this Mode
     *
     * @param context The current Context
     * @return The text as a String
     */
    public abstract String getHelpText(Context context);

    /**
     * Returns the title of this mode
     *
     * @param context The current Context
     * @return The title
     */
    public abstract String getTitle(Context context);

    /**
     * Returns a shorter version of the title of this Mode.
     *
     * @param context The current Context
     * @return The title
     */
    public abstract String getShortTitle(Context context);

    /**
     * Returns the Icon of this Mode.
     *
     * @param context The current Context
     * @return The Icon as a Drawable
     */
    public abstract Drawable getIcon(Context context);

    /**
     * Returns the {@link TestSettingsLayout} of this Mode. Every inheritor should return it's own.
     * A Listener should be passed to this Method, so the caller can receive Updates.
     *
     * @param context  The current Context
     * @param listener The Listener for the Test
     * @return The TestSettingsLayout
     */
    public abstract TestSettingsLayout getTestSettingsLayout(@NonNull Context context, @NonNull TestSettingsLayout.OnTestSettingsListener listener);

    /**
     * Returns the {@link Test} of this Mode. Every inheritor should return it's own.
     * A Listener should be passed to this Method, so the caller can receive Updates.
     *
     * @param context  The current Context
     * @param settings The TestSettings
     * @param listener The Listener for the Test
     * @return The Test
     */
    public abstract Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener);

    /**
     * @param context            The current Context
     * @param settings           The TestSettings
     * @param listener           The Listener
     * @param savedInstanceState The Bundle with the last instance state
     * @return The Test
     * @see Mode#getTest(Context, TestSettings, Test.OnTestFinishedListener)
     * The caller passes an additional Bundle, from which the last state of the Test is recovered.
     * Call this Mehod after an Orientation change.
     */
    public abstract Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener, @NonNull Bundle savedInstanceState);

    /**
     * Resets all the data of this Mode. The perfectInRow field will not be affected.
     */
    public void reset() {
        data.reset();
    }
}
