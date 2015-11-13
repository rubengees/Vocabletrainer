package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.test.Test;
import com.rubengees.vocables.core.test.TimeTest;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.layout.TestSettingsLayout;
import com.rubengees.vocables.core.testsettings.layout.TimeTestSettingsLayout;

/**
 * Created by ruben on 28.04.15.
 */
public class TimeMode extends Mode {

    public static final int MIN_AMOUNT = 10;
    public static final Creator<TimeMode> CREATOR = new Creator<TimeMode>() {
        public TimeMode createFromParcel(Parcel source) {
            return new TimeMode(source);
        }

        public TimeMode[] newArray(int size) {
            return new TimeMode[size];
        }
    };

    public TimeMode(ModeData data) {
        super(data);
    }

    protected TimeMode(Parcel in) {
        super(in);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColor(final Context context) {
        return ContextCompat.getColor(context, R.color.time_mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDarkColor(final Context context) {
        return ContextCompat.getColor(context, R.color.time_mode_dark);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinAmount() {
        return MIN_AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpText(final Context context) {
        return context.getString(R.string.mode_time_help);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle(final Context context) {
        return context.getString(R.string.mode_time_title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getShortTitle(final Context context) {
        return context.getString(R.string.mode_time_title_short);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Drawable getIcon(final Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_mode_time);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public TestSettingsLayout getTestSettingsLayout(@NonNull Context context, @NonNull TestSettingsLayout.OnTestSettingsListener listener) {
        return new TimeTestSettingsLayout(context, listener);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener, @NonNull Bundle savedInstanceState) {
        return new TimeTest(context, settings, listener, getColor(context), getDarkColor(context), savedInstanceState);
    }

    @Override
    public boolean isRelevant() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener) {
        return new TimeTest(context, settings, listener, getColor(context), getDarkColor(context));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
