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
import com.rubengees.vocables.core.test.TrainingTest;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.layout.TestSettingsLayout;
import com.rubengees.vocables.core.testsettings.layout.TrainingTestSettingsLayout;
import com.rubengees.vocables.utils.Utils;

/**
 * Created by Ruben on 20.07.2015.
 */
public class TrainingMode extends Mode {

    public static final Parcelable.Creator<TrainingMode> CREATOR = new Parcelable.Creator<TrainingMode>() {

        public TrainingMode createFromParcel(Parcel in) {
            return new TrainingMode(in);
        }

        public TrainingMode[] newArray(int size) {
            return new TrainingMode[size];
        }
    };

    public static final int MIN_AMOUNT = 1;


    public TrainingMode(ModeData data) {
        super(data);
    }

    public TrainingMode(Parcel in) {
        super(in);
    }

    @Override
    public int getColor(Context context) {
        return Utils.getColor(context, R.color.training_mode);
    }

    @Override
    public int getDarkColor(Context context) {
        return Utils.getColor(context, R.color.training_mode_dark);
    }

    @Override
    public int getMinAmount() {
        return MIN_AMOUNT;
    }

    @Override
    public String getHelpText(Context context) {
        return context.getString(R.string.mode_training_help);
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.mode_training_title);
    }

    @Override
    public String getShortTitle(Context context) {
        return context.getString(R.string.mode_training_title_short);
    }

    @Override
    public Drawable getIcon(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_mode_training);
    }

    @Override
    public TestSettingsLayout getTestSettingsLayout(@NonNull Context context, @NonNull TestSettingsLayout.OnTestSettingsListener listener) {
        return new TrainingTestSettingsLayout(context, listener);
    }

    @Override
    public Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener) {
        return new TrainingTest(context, settings, listener, getColor(context), getDarkColor(context));
    }

    @Override
    public Test getTest(@NonNull Context context, @NonNull TestSettings settings, @NonNull Test.OnTestFinishedListener listener, @NonNull Bundle savedInstanceState) {
        return new TrainingTest(context, settings, listener, getColor(context), getDarkColor(context), savedInstanceState);
    }

    @Override
    public boolean isRelevant() {
        return false;
    }
}
