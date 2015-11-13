package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.test.PairTest;
import com.rubengees.vocables.core.test.Test;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.layout.PairTestSettingsLayout;
import com.rubengees.vocables.core.testsettings.layout.TestSettingsLayout;

/**
 * Created by ruben on 28.04.15.
 */
public class PairMode extends Mode {
    public static final int MIN_AMOUNT = 5;
    public static final Creator<PairMode> CREATOR = new Creator<PairMode>() {
        public PairMode createFromParcel(Parcel source) {
            return new PairMode(source);
        }

        public PairMode[] newArray(int size) {
            return new PairMode[size];
        }
    };

    public PairMode(ModeData data) {
        super(data);
    }

    protected PairMode(Parcel in) {
        super(in);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColor(final Context context) {
        return ContextCompat.getColor(context, R.color.pair_mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDarkColor(final Context context) {
        return ContextCompat.getColor(context, R.color.pair_mode_dark);
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
        return context.getString(R.string.mode_pair_help);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle(final Context context) {
        return context.getString(R.string.mode_pair_title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getShortTitle(final Context context) {
        return context.getString(R.string.mode_pair_title_short);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Drawable getIcon(final Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_mode_pair);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public TestSettingsLayout getTestSettingsLayout(@NonNull Context context, @NonNull TestSettingsLayout.OnTestSettingsListener listener) {
        return new PairTestSettingsLayout(context, listener);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener, @NonNull Bundle savedInstanceState) {
        return new PairTest(context, settings, listener, getColor(context), getDarkColor(context), savedInstanceState);
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
        return new PairTest(context, settings, listener, getColor(context), getDarkColor(context));
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
