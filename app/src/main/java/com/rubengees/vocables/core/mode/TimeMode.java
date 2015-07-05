package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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

    public static final Parcelable.Creator<TimeMode> CREATOR = new Parcelable.Creator<TimeMode>() {

        public TimeMode createFromParcel(Parcel in) {
            return new TimeMode(in);
        }

        public TimeMode[] newArray(int size) {
            return new TimeMode[size];
        }

    };
    public static final int MIN_AMOUNT = 10;

    protected TimeMode(Parcel in) {
        super(in);
    }

    public TimeMode(ModeData data) {
        super(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColor(final Context context) {
        return context.getResources().getColor(R.color.time_mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDarkColor(final Context context) {
        return context.getResources().getColor(R.color.time_mode_dark);
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
    @Override
    public TestSettingsLayout getTestSettingsLayout(@NonNull Context context, @NonNull TestSettingsLayout.OnTestSettingsListener listener) {
        return new TimeTestSettingsLayout(context, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener, @NonNull Bundle savedInstanceState) {
        return new TimeTest(context, settings, listener, getColor(context), getDarkColor(context), savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener) {
        return new TimeTest(context, settings, listener, getColor(context), getDarkColor(context));
    }

}
