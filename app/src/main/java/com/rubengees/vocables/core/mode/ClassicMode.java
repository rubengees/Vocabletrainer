package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.test.ClassicTest;
import com.rubengees.vocables.core.test.Test;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.layout.ClassicTestSettingsLayout;
import com.rubengees.vocables.core.testsettings.layout.TestSettingsLayout;

/**
 * An Object storing all the relevant data of the ClassicMode.
 */
public class ClassicMode extends Mode {

    public static final Parcelable.Creator<ClassicMode> CREATOR = new Parcelable.Creator<ClassicMode>() {

        public ClassicMode createFromParcel(Parcel in) {
            return new ClassicMode(in);
        }

        public ClassicMode[] newArray(int size) {
            return new ClassicMode[size];
        }

    };
    public static final int MIN_AMOUNT = 1;

    public ClassicMode(Parcel in) {
        super(in);
    }

    public ClassicMode(ModeData data) {
        super(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColor(final Context context) {
        return context.getResources().getColor(R.color.classic_mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDarkColor(final Context context) {
        return context.getResources().getColor(R.color.classic_mode_dark);
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
        return context.getString(R.string.mode_classic_help);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle(final Context context) {
        return context.getString(R.string.mode_classic_title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getShortTitle(final Context context) {
        return context.getString(R.string.mode_classic_title_short);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Drawable getIcon(final Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_mode_classic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestSettingsLayout getTestSettingsLayout(@NonNull Context context, @NonNull TestSettingsLayout.OnTestSettingsListener listener) {
        return new ClassicTestSettingsLayout(context, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener, @NonNull Bundle savedInstanceState) {
        return new ClassicTest(context, settings, listener, getColor(context), getDarkColor(context), savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener) {
        return new ClassicTest(context, settings, listener, getColor(context), getDarkColor(context));
    }

}
